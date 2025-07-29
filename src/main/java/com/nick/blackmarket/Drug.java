package com.nick.blackmarket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

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

    public Drug(Plugin plugin, String name, List<String> description, NamespacedKey texture, BiConsumer<Integer, Player> onConsume) {
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
                        onConsume.accept(nbt.getInteger("Rarity"), event.getPlayer());
                    }
                });
            }
        }, plugin);
    }
}
