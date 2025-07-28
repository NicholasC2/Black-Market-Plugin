package com.nick.blackmarket;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBT;

public class Plugin extends JavaPlugin {
    List<Drug> drugs = new ArrayList<Drug>();

    @Override
    public void onEnable() {
        getLogger().info("Black Market Plugin has been enabled!");

        addListeners();
        registerDrugs();
        registerCommands();
    }

    public void registerDrugs() {
        Drug cocaine = new Drug("Cocaine", null, 0, new NamespacedKey("minecraft", "sugar"));
        drugs.add(cocaine);
        Bukkit.getServer().addRecipe(new ShapelessRecipe(new NamespacedKey(this, "cocaine"), cocaine.item)
            .addIngredient(Material.SUGAR)
            .addIngredient(Material.POPPY)
            .addIngredient(Material.BONE_MEAL)
        );

        Drug heroin = new Drug("Heroin", null, 1, new NamespacedKey("minecraft", "tripwire_hook"));
        drugs.add(heroin);
        Bukkit.getServer().addRecipe(new ShapelessRecipe(new NamespacedKey(this, "herion"), heroin.item)
            .addIngredient(Material.SUGAR)
            .addIngredient(Material.RED_MUSHROOM)
            .addIngredient(Material.BLAZE_POWDER)
        );
    }

    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onPrepareInventoryResult(PrepareInventoryResultEvent event) {
                for(ItemStack item : event.getInventory().getContents()) {
                    if(item != null) {
                        NBT.get(item, (nbt) -> {
                            if(nbt.hasTag("BlackMarketItem")) {
                                event.setResult(null);
                            }
                        });
                    }
                }
            }
        }, this);
    }

    public void registerCommands() {
        Bukkit.getPluginCommand("blackmarket").setExecutor(new BlackMarketCommandExecutor(drugs));
        Bukkit.getPluginCommand("blackmarket").setTabCompleter(new BlackMarketCommandTabCompleter(drugs));
    }
}
