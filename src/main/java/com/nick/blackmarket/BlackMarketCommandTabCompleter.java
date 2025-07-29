package com.nick.blackmarket;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BlackMarketCommandTabCompleter implements TabCompleter {
    
    private final List<Drug> drugs;
    private final List<Drink> drinks;

    public BlackMarketCommandTabCompleter(List<Drug> drugs, List<Drink> drinks) {
        this.drugs = drugs;
        this.drinks = drinks;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("give");
        } else if (args.length == 2 && "give".equalsIgnoreCase(args[0])) {
            List<String> playerNames = new java.util.ArrayList<>(
                Bukkit.getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .toList()
            );
            
            playerNames.addAll(List.of("@e", "@a", "@p", "@r", "@s"));
            return playerNames;
            
        } else if (args.length == 3 && "give".equalsIgnoreCase(args[0])) {
            List<String> drugNames = new java.util.ArrayList<>(
                drugs.stream()
                    .map(drug -> drug.name)
                    .toList()
            );
            drugNames.addAll(drinks.stream().map(drink -> drink.name).toList());

            return drugNames.stream().filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase())).map(name -> name.replaceAll(" ", "").toLowerCase()).toList();
        }
        return List.of();
    }

}
