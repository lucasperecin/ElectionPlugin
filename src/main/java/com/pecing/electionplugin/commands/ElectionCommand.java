package com.pecing.electionplugin.commands;

import com.pecing.electionplugin.ElectionSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionCommand implements CommandExecutor, TabCompleter {

    private final ElectionSystem plugin;

    public ElectionCommand(ElectionSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("resetelection")) {
            if (!sender.hasPermission("electionplugin.admin")) {
                sender.sendMessage(Component.text("❌ You don't have permission to use this command!", NamedTextColor.RED));
                return true;
            }
            plugin.getElectionManager().resetElection();
            sender.sendMessage(Component.text("✅ Election reset successfully!", NamedTextColor.GREEN));
            return true;
        }

        if (!sender.hasPermission("electionplugin.admin")) {
            sender.sendMessage(Component.text("❌ You don't have permission to use this command!", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "lang":
            case "language":
                if (!(sender instanceof Player)) {
                    // Use Component.text instead of getComponent for fallback messages
                    sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                    return true;
                }
                if (args.length < 2) {
                    // Show current language and available languages
                    Player player = (Player) sender;
                    String currentLang = plugin.getLanguageManager().getPlayerLanguage(player);
                    String availableLangs = plugin.getLanguageManager().getAvailableLanguages();
                    
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("language", currentLang);
                    placeholders.put("languages", availableLangs);
                    
                    player.sendMessage(plugin.getLanguageManager().getComponent("language.current", player, placeholders));
                    player.sendMessage(plugin.getLanguageManager().getComponent("language.available", player, placeholders));
                    player.sendMessage(Component.text("Usage: /election lang <language>", NamedTextColor.YELLOW));
                } else {
                    Player player = (Player) sender;
                    plugin.getLanguageManager().setPlayerLanguage(player, args[1]);
                }
                break;
            case "create":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("❌ Usage: /election create <player name>", NamedTextColor.RED));
                    return true;
                }
                if (plugin.getElectionManager().addCandidate(args[1])) {
                    sender.sendMessage(Component.text("✅ Candidate " + args[1] + " added successfully!", NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text("❌ This candidate already exists!", NamedTextColor.RED));
                }
                break;

            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("❌ Usage: /election remove <player name>", NamedTextColor.RED));
                    return true;
                }
                if (plugin.getElectionManager().removeCandidate(args[1])) {
                    sender.sendMessage(Component.text("✅ Candidate " + args[1] + " removed successfully!", NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text("❌ Candidate not found!", NamedTextColor.RED));
                }
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    @Override
public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (!sender.hasPermission("electionplugin.admin")) {
        return new ArrayList<>();
    }

    if (args.length == 1) {
        return Arrays.asList("create", "remove", "lang");
    } else if (args.length == 2 && args[0].equalsIgnoreCase("lang")) {
        return Arrays.asList("pt_BR", "en_US");
    }

    return new ArrayList<>();
}

    private void showHelp(CommandSender sender) {
        sender.sendMessage(Component.text("📋 === ELECTION COMMANDS ===", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/election create <player> - Adds a candidate", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/election remove <player> - Removes a candidate", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/resetelection - Resets the entire election", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/election lang <language> - Sets your preferred language", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/vote - Cast your vote", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/results - Shows the election results", NamedTextColor.YELLOW));
        
    }
}