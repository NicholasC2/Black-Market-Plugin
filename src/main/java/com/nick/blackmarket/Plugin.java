package com.nick.blackmarket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBT;

public class Plugin extends JavaPlugin {
    List<Drug> drugs = new ArrayList<Drug>();
    List<Drink> drinks = new ArrayList<Drink>();

    @Override
    public void onEnable() {
        getLogger().info("Black Market Plugin has been enabled!");

        addListeners();
        initDrugs();
        registerCommands();
    }

    private Drug registerDrug(String name, List<String> description, Material texture, BiConsumer<Integer, Player> onConsume) {
        Drug drug = new Drug(this, name, description, new NamespacedKey("minecraft", texture.name().toLowerCase()), onConsume);
        drugs.add(drug);
        return drug;
    }

    private Drink registerDrink(String name, List<String> description, Color color, BiConsumer<Integer, Player> onConsume) {
        Drink drink = new Drink(this, name, description, color, onConsume);
        drinks.add(drink);
        return drink;
    }

    public void initDrugs() {
        Drug weed = registerDrug("Weed",
            List.of(ChatColor.GRAY + "Mellow regen and slowed vision."),
            Material.FERN,
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, rarity * 300, rarity));
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, (rarity * 300) / 2, 0));
                if (rarity == 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (rarity * 300) / 2, 0));
                }
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, rarity == 0 ? 2 : 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                            if (rarity == 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
                            }
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "weed"), weed.getItem(0, 4))
            .addIngredient(Material.DRIED_KELP)
            .addIngredient(Material.BLAZE_POWDER)
            .addIngredient(Material.SUGAR_CANE)
        );

        Drug cocaine = registerDrug("Cocaine",
            List.of(ChatColor.GRAY + "Fast, sharp high. Brutal crash."),
            Material.SUGAR,
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, rarity * 300, rarity + 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, (rarity * 300) / 2, rarity + 1));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, rarity == 0 ? 2 : 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                            if (rarity == 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
                            }
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "cocaine"), cocaine.getItem(0, 3))
            .addIngredient(Material.SUGAR)
            .addIngredient(Material.BONE_MEAL)
            .addIngredient(Material.GUNPOWDER)
        );

        Drug heroin = registerDrug("Heroin",
            List.of(ChatColor.GRAY + "Heavy numbness. Time fades."),
            Material.TRIPWIRE_HOOK,
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, rarity * 300, rarity));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, rarity * 300, 1));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, rarity == 0 ? 2 : 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                            if (rarity == 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
                            }
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "heroin"), heroin.getItem(0, 7))
            .addIngredient(Material.BLAZE_ROD)
            .addIngredient(Material.GHAST_TEAR)
            .addIngredient(Material.FERMENTED_SPIDER_EYE)
        );

        Drug meth = registerDrug("Meth",
            List.of(ChatColor.GRAY + "Intense energy. Mind spirals."),
            Material.BLUE_STAINED_GLASS_PANE,
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, rarity * 300, rarity + 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, rarity * 300, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, rarity * 300, 1));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, rarity == 0 ? 2 : 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                            if (rarity == 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
                            }
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "meth"), meth.getItem(0, 3))
            .addIngredient(Material.SUGAR)
            .addIngredient(Material.GLOWSTONE_DUST)
            .addIngredient(Material.BLAZE_POWDER)
        );

        Drug ecstacy = registerDrug("Ecstacy",
            List.of("Euphoric, social high. Intense feelings."),
            Material.BEETROOT_SEEDS, 
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, rarity * 300, rarity + 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, rarity * 300, 1));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, rarity == 0 ? 2 : 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                            if (rarity == 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
                            }
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "ecstacy"), ecstacy.getItem(0, 5))
            .addIngredient(Material.GLOW_INK_SAC)
            .addIngredient(Material.CHORUS_FRUIT)
            .addIngredient(Material.SUGAR)
        );

        Drink lean = registerDrink("Lean",
            List.of(ChatColor.GRAY + "Purple drank. Slow and dreamy."),
            Color.fromRGB(185, 50, 220),
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, rarity * 400, rarity + 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, rarity * 400, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, rarity * 400, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, rarity * 200, 0));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 300, 2));
                        }
                    }.runTaskLater(this, rarity * 400);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "lean"), lean.getItem(0, 7))
            .addIngredient(Material.SWEET_BERRIES)
            .addIngredient(Material.POPPED_CHORUS_FRUIT)
            .addIngredient(Material.GLASS_BOTTLE)
            .addIngredient(Material.GLISTERING_MELON_SLICE)
        );

        Drink beer = registerDrink("Beer",
            List.of(ChatColor.GRAY + "Classic brew. Light buzz."),
            Color.fromRGB(231, 193, 69),
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, rarity * 300, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, rarity * 300, 0));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "beer"), beer.getItem(0, 3))
            .addIngredient(Material.WHEAT)
            .addIngredient(Material.HONEY_BOTTLE)
            .addIngredient(Material.FERMENTED_SPIDER_EYE)
        );

        Drink redWine = registerDrink("Red Wine",
            List.of(ChatColor.GRAY + "Rich and smooth. Relaxing."),
            Color.fromRGB(128, 0, 32),
            (rarity, player) -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, rarity * 300, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, rarity * 300, 0));
                if (rarity <= 1) {
                    new BukkitRunnable() {
                        @Override public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 400, 1));
                        }
                    }.runTaskLater(this, rarity * 300);
                }
            }
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "red_wine"), redWine.getItem(0, 3))
            .addIngredient(Material.SWEET_BERRIES)
            .addIngredient(Material.POTION)
            .addIngredient(Material.FERMENTED_SPIDER_EYE)
        );
    }

    public void addListeners() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
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
