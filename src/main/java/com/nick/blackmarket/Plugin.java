package com.nick.blackmarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    PersistentDataContainer playerData = player.getPersistentDataContainer();
                    
                    int highTimer = playerData.get(new NamespacedKey(Plugin.this, "highTimer"), PersistentDataType.INTEGER);

                    if(highTimer <= 0) {
                        int high = playerData.get(new NamespacedKey(Plugin.this, "high"), PersistentDataType.INTEGER);
                        String highDownEffects = playerData.get(new NamespacedKey(Plugin.this, "highDownEffects"), PersistentDataType.STRING);

                        List<String> parsedEffects = Arrays.asList(highDownEffects.split(","));
                        List<PotionEffectType> potionEffectTypes = new ArrayList<>();
                        parsedEffects.forEach(parsedEffect -> {
                            potionEffectTypes.add(PotionEffectType.getByName(parsedEffect));
                        });

                        List<PotionEffect> potionEffects = new ArrayList<>();

                        potionEffectTypes.forEach((effect) -> {
                            potionEffects.add(new PotionEffect(effect, (high + 1) * 300, high - 1));
                        });

                        if(high >= 20) {
                            potionEffects.add(new PotionEffect(PotionEffectType.WITHER, PotionEffect.INFINITE_DURATION, 5));
                        }

                        for(PotionEffect potion : player.getActivePotionEffects()) {
                            player.removePotionEffect(potion.getType());
                        }
                        
                        player.addPotionEffects(potionEffects);

                        playerData.remove(new NamespacedKey(Plugin.this, "high"));
                        playerData.remove(new NamespacedKey(Plugin.this, "highTimer"));
                        playerData.remove(new NamespacedKey(Plugin.this, "highDownEffects"));
                    } else {
                        playerData.set(new NamespacedKey(Plugin.this, "highTimer"), PersistentDataType.INTEGER, highTimer - 1);
                    }
                });
            }
        }.runTaskTimer(this, 0, 20);
    }

    private Drug registerDrug(String name, List<String> description, Material texture, List<PotionEffectType> effectsHigh, List<PotionEffectType> effectsDown) {
        Drug drug = new Drug(this, name, description, new NamespacedKey("minecraft", texture.name().toLowerCase()),effectsHigh,effectsDown);
        drugs.add(drug);
        return drug;
    }

    private Drink registerDrink(String name, List<String> description, Color color, List<PotionEffectType> effectsHigh, List<PotionEffectType> effectsDown) {
        Drink drink = new Drink(this, name, description, color, effectsHigh, effectsDown);
        drinks.add(drink);
        return drink;
    }

    public void initDrugs() {
        Drug weed = registerDrug("Weed",
            List.of(ChatColor.GRAY + "Mellow regen and slowed vision."),
            Material.FERN,
            List.of(PotionEffectType.STRENGTH, PotionEffectType.SLOWNESS),
            List.of(PotionEffectType.NAUSEA, PotionEffectType.HUNGER)
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "weed"), weed.getItem(0, 4))
            .addIngredient(Material.DRIED_KELP)
            .addIngredient(Material.BLAZE_POWDER)
            .addIngredient(Material.SUGAR_CANE)
        );

        Drug cocaine = registerDrug("Cocaine",
            List.of(ChatColor.GRAY + "Fast, sharp high. Brutal crash."),
            Material.SUGAR,
            List.of(PotionEffectType.SPEED, PotionEffectType.HASTE),
            List.of(PotionEffectType.SLOWNESS, PotionEffectType.POISON)
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "cocaine"), cocaine.getItem(0, 3))
            .addIngredient(Material.SUGAR)
            .addIngredient(Material.BONE_MEAL)
            .addIngredient(Material.GUNPOWDER)
        );

        Drug heroin = registerDrug("Heroin",
            List.of(ChatColor.GRAY + "Heavy numbness. Time fades."),
            Material.TRIPWIRE_HOOK,
            List.of(PotionEffectType.SLOWNESS, PotionEffectType.ABSORPTION),
            List.of(PotionEffectType.BLINDNESS, PotionEffectType.NAUSEA)
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "heroin"), heroin.getItem(0, 7))
            .addIngredient(Material.BLAZE_ROD)
            .addIngredient(Material.GHAST_TEAR)
            .addIngredient(Material.FERMENTED_SPIDER_EYE)
        );

        Drug ecstacy = registerDrug("Ecstacy",
            List.of("Euphoric, social high. Intense feelings."),
            Material.BEETROOT_SEEDS,
            List.of(PotionEffectType.SATURATION, PotionEffectType.REGENERATION),
            List.of(PotionEffectType.SLOWNESS, PotionEffectType.BLINDNESS)
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "ecstacy"), ecstacy.getItem(0, 5))
            .addIngredient(Material.GLOW_INK_SAC)
            .addIngredient(Material.CHORUS_FRUIT)
            .addIngredient(Material.SUGAR)
        );

        Drink lean = registerDrink("Lean",
            List.of(ChatColor.GRAY + "Purple drank. Slow and dreamy."),
            Color.fromRGB(185, 50, 220),
            List.of(PotionEffectType.ABSORPTION, PotionEffectType.SLOWNESS),
            List.of(PotionEffectType.SLOWNESS, PotionEffectType.BLINDNESS)
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
            List.of(PotionEffectType.STRENGTH, PotionEffectType.SATURATION),
            List.of(PotionEffectType.SLOWNESS, PotionEffectType.DARKNESS)
        );

        Bukkit.addRecipe(new ShapelessRecipe(new NamespacedKey(this, "beer"), beer.getItem(0, 3))
            .addIngredient(Material.WHEAT)
            .addIngredient(Material.HONEY_BOTTLE)
            .addIngredient(Material.FERMENTED_SPIDER_EYE)
        );

        Drink redWine = registerDrink("Red Wine",
            List.of(ChatColor.GRAY + "Rich and smooth. Relaxing."),
            Color.fromRGB(128, 0, 32),
            List.of(PotionEffectType.SPEED, PotionEffectType.HASTE, PotionEffectType.DARKNESS),
            List.of(PotionEffectType.BLINDNESS, PotionEffectType.NAUSEA)
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
        Bukkit.getPluginCommand("blackmarket").setExecutor(new BlackMarketCommandExecutor(drugs, drinks));
        Bukkit.getPluginCommand("blackmarket").setTabCompleter(new BlackMarketCommandTabCompleter(drugs, drinks));
    }
}
