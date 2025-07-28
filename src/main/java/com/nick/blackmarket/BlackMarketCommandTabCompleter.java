package com.nick.blackmarket;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BlackMarketCommandTabCompleter implements TabCompleter {
    
    private final List<Drug> drugs;

    public BlackMarketCommandTabCompleter(List<Drug> drugs) {
        this.drugs = drugs;
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
            return drugs.stream()
                .map(drug -> drug.name)
                .toList();
        } else if (args.length >= 4 && "give".equalsIgnoreCase(args[0])) {
            return List.of();
        }
        return List.of();
    }

}
