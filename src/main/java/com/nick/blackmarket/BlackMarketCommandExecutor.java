package com.nick.blackmarket;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlackMarketCommandExecutor implements CommandExecutor {

    private final List<Drug> drugs;

    public BlackMarketCommandExecutor(List<Drug> drugs) {
        this.drugs = drugs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /blackmarket give <player|selector> <item> <amount>");
            return false;
        }

        String subCommand = args[0].toLowerCase();

        if ("give".equals(subCommand)) {
            if (args.length < 4) {
                sender.sendMessage("Usage: /blackmarket give <player|selector> <item> <amount>");
                return false;
            }

            String selector = args[1];
            String drugName = args[2];
            int amount;
            try {
                amount = Integer.parseInt(args[3]);
                if (amount < 1) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount: " + args[3]);
                return false;
            }

            Drug wantedDrug = drugs.stream()
                .filter(drug -> drug.name.equalsIgnoreCase(drugName))
                .findFirst()
                .orElse(null);

            if (wantedDrug == null) {
                sender.sendMessage("Unknown drug: " + drugName);
                return false;
            }

            ItemStack itemToGive = wantedDrug.item.clone();
            itemToGive.setAmount(amount);

            switch (selector) {
                case "@e":
                case "@a":
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Map<Integer, ItemStack> leftovers = p.getInventory().addItem(itemToGive.clone());
                        leftovers.values().forEach(item -> p.getWorld().dropItemNaturally(p.getLocation(), item));
                    }
                    sender.sendMessage("Gave drug to all online players.");
                    break;

                case "@p":
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Player closest = Bukkit.getOnlinePlayers().stream()
                            .filter(p -> !p.equals(player))
                            .min((p1, p2) -> Double.compare(
                                player.getLocation().distance(p1.getLocation()),
                                player.getLocation().distance(p2.getLocation())))
                            .orElse(null);
                        if (closest != null) {
                            Map<Integer, ItemStack> leftovers = closest.getInventory().addItem(itemToGive.clone());
                            leftovers.values().forEach(item -> closest.getWorld().dropItemNaturally(closest.getLocation(), item));
                            sender.sendMessage("Gave drug to " + closest.getName() + ".");
                        } else {
                            if(sender instanceof Player) {
                                Player selfPlayer = (Player) sender;

                                Map<Integer, ItemStack> leftovers = selfPlayer.getInventory().addItem(itemToGive.clone());
                                leftovers.values().forEach(item -> selfPlayer.getWorld().dropItemNaturally(selfPlayer.getLocation(), item));
                                sender.sendMessage("Gave drug to " + selfPlayer.getName() + ".");
                            }
                            sender.sendMessage("No players found nearby.");
                        }
                    } else {
                        sender.sendMessage("This selector can only be used by players.");
                    }
                    break;

                case "@r":
                    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                    if (players.length > 0) {
                        Player randomPlayer = players[(int) (Math.random() * players.length)];
                        Map<Integer, ItemStack> leftovers = randomPlayer.getInventory().addItem(itemToGive.clone());
                        leftovers.values().forEach(item -> randomPlayer.getWorld().dropItemNaturally(randomPlayer.getLocation(), item));
                        sender.sendMessage("Gave drug to " + randomPlayer.getName() + ".");
                    } else {
                        sender.sendMessage("No players online to give items to.");
                    }
                    break;

                case "@s":
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(itemToGive.clone());
                        leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
                        sender.sendMessage("Gave drug to yourself.");
                    } else {
                        sender.sendMessage("This selector can only be used by players.");
                    }
                    break;

                default:
                    Player targetPlayer = Bukkit.getPlayer(selector);
                    if (targetPlayer != null && targetPlayer.isOnline()) {
                        Map<Integer, ItemStack> leftovers = targetPlayer.getInventory().addItem(itemToGive.clone());
                        leftovers.values().forEach(item -> targetPlayer.getWorld().dropItemNaturally(targetPlayer.getLocation(), item));
                        sender.sendMessage("Gave drug to " + targetPlayer.getName() + ".");
                    } else {
                        sender.sendMessage("Player not found: " + selector);
                    }
                    break;
            }
            return true;
        }

        return false;
    }
}