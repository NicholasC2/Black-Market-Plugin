package com.nick.blackmarket;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.consumable.ConsumableComponent;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBT;
import net.md_5.bungee.api.ChatColor;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Black Market Plugin has been enabled!");

        addListeners();

        ConsumableComponent consumer = new ItemStack(Material.COOKIE).getItemMeta().getConsumable();

        consumer.setConsumeParticles(false);
        
        new BlackMarketItem(this, "Fent", List.of(ChatColor.AQUA+"Exotic"), Material.DIAMOND, (item) -> {
            Bukkit.getLogger().info("You consumed the Fent!");
        }, consumer);
    }

    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onPrepareGrindstone(PrepareInventoryResultEvent event) {
                for(ItemStack item : event.getInventory().getContents()) {
                    NBT.get(item, (nbt) -> {
                        if(nbt.hasTag("BlackMarketItem")) {
                            event.setResult(null);
                        }
                    });
                }
            }
        }, this);
    }
}
