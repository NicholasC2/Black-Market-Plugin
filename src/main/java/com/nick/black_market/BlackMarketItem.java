package com.nick.black_market;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.consumable.ConsumableComponent;

import de.tr7zw.nbtapi.*;

public class BlackMarketItem {
    public String name;
    public List<String> description;
    public Material texture;

    private ItemStack item;

    public BlackMarketItem(String name, List<String> description, Material texture, Consumer<ItemStack> onConsume, ConsumableComponent consumable) {
        this.name = name;
        this.description = description;
        this.texture = texture;

        item = new ItemStack(texture);
        item.getItemMeta().setDisplayName(name);
        item.getItemMeta().setLore(description);
        item.getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        item.getItemMeta().setConsumable(consumable);

        NBT.modify(item, (nbt) -> {
            nbt.setString("BlackMarketItem", name);
        });

        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onConsume(PlayerItemConsumeEvent event) {
                NBT.get(event.getItem(), (nbt) -> {
                    if(nbt.getString("BlackMarketItem").equals(name)) {
                        onConsume.accept(event.getItem());
                    }
                });
            }
        }, Bukkit.getPluginManager().getPlugin("BlackMarket"));
    }

    public ItemStack getItem() {
        return item;
    }
}
