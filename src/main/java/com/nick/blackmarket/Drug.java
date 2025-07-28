package com.nick.blackmarket;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBT;

public class Drug {
    public String name;
    public List<String> description;
    public String rarity;
    
    public ItemStack item;

    private List<String> rarities = List.of(
        ChatColor.BLUE+"Street Grade",
        ChatColor.DARK_BLUE+"Prescription",
        ChatColor.LIGHT_PURPLE+"Designer",
        ChatColor.DARK_RED+"Cartel Exclusive"
    );
    public Drug(String name, List<String> description, int rarity, NamespacedKey texture) {
        this.name = name;
        this.description = description;
        this.rarity = rarities.get(rarity);
        
        this.item = new ItemStack(Material.SUGAR);
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            // add rarity and description to lore
            List<String> lore = new java.util.ArrayList<>();
            if(rarity >= 0 && rarity < rarities.size()) {
                lore.add(rarities.get(rarity));
            } else {
                lore.add(ChatColor.GRAY+"Unknown Rarity");
            }
            lore.add(this.rarity);
            if(description != null && !description.isEmpty()) {
                lore.addAll(this.description);
            }
            meta.setLore(lore);

            meta.setEnchantmentGlintOverride(true);
            if(texture != null) {
                meta.setItemModel(texture);
            }

            this.item.setItemMeta(meta);
        }
        NBT.modify(this.item, (nbt) -> {
            nbt.setString("BlackMarketItem", name);
        });
    }
}
