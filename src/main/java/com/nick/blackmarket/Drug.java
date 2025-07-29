package com.nick.blackmarket;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.tr7zw.nbtapi.NBT;

public class Drug {
    public String name;
    public List<String> description;
    
    private ItemStack item;

    private List<String> rarities = List.of(
        ChatColor.BLUE+"Street Grade",
        ChatColor.DARK_BLUE+"Prescription",
        ChatColor.LIGHT_PURPLE+"Designer",
        ChatColor.DARK_RED+"Cartel Exclusive"
    );

    public ItemStack getItem(int rarity) {
        return getItem(rarity, 1);
    }

    public ItemStack getItem(int rarity, int amount) {
        List<String> lore = new ArrayList<>();

        if (rarity >= 0 && rarity < rarities.size()) {
            lore.add(rarities.get(rarity));
        } else {
            lore.add(ChatColor.GRAY+"Unknown Rarity");
        }
        lore.addAll(description);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        NBT.modify(item, (nbt) -> {
            nbt.setInteger("Rarity", rarity);
        });

        item.setAmount(amount);

        return item;
    }

    public Drug(Plugin plugin, String name, List<String> description, NamespacedKey texture, List<PotionEffectType> effectsHigh, List<PotionEffectType> effectsDown) {
        this.name = name;
        this.description = description;
        
        this.item = new ItemStack(Material.COOKIE);
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET+name);
            meta.setEnchantmentGlintOverride(true);

            if(texture != null) {
                meta.setItemModel(texture);
            }

            this.item.setItemMeta(meta);
        }
        NBT.modify(this.item, (nbt) -> {
            nbt.setString("BlackMarketItem", name);
        });

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onConsume(PlayerItemConsumeEvent event) {
                NBT.get(event.getItem(), (nbt) -> {
                    if(nbt.getString("BlackMarketItem").equalsIgnoreCase(name)) {
                        int rarity = nbt.getInteger("Rarity");

                        List<PotionEffect> potionEffects = new ArrayList<>();
                        effectsHigh.forEach((effect) -> {
                            potionEffects.add(new PotionEffect(effect, (rarity + 1) * 300, rarity));
                        });

                        event.getPlayer().addPotionEffects(potionEffects);
                        PersistentDataContainer playerData = event.getPlayer().getPersistentDataContainer();

                        Integer high = playerData.get(new NamespacedKey(plugin, "high"), PersistentDataType.INTEGER);

                        playerData.set(new NamespacedKey(plugin, "high"), PersistentDataType.INTEGER, high + 1);

                        playerData.set(new NamespacedKey(plugin, "highTimer"), PersistentDataType.INTEGER, (rarity + 1) * 15);

                        playerData.set(new NamespacedKey(plugin, "highDownEffects"), PersistentDataType.STRING, effectsDown.toString());
                    }
                });
            }
        }, plugin);
    }
}
