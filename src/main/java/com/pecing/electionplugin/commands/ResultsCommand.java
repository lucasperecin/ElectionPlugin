package com.pecing.electionplugin.commands;

import com.pecing.electionplugin.ElectionSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ResultsCommand implements CommandExecutor {

    private final ElectionSystem plugin;

    public ResultsCommand(ElectionSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var results = plugin.getElectionManager().getResults();
        int totalVotes = plugin.getElectionManager().getTotalVotes();

        if (results.isEmpty()) {
            sender.sendMessage(Component.text("ℹ️ There are no candidates or votes in the current election!", NamedTextColor.YELLOW));
            return true;
        }

        Component message = Component.text()
                .append(Component.text("📊 === ELECTION RESULTS ===", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.newline())
                .append(Component.text("Total votes: " + totalVotes, NamedTextColor.YELLOW))
                .append(Component.newline())
                .build();

        // Create sorted list
        List<Map.Entry<String, Integer>> sortedResults = new ArrayList<>(results.entrySet());
        sortedResults.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        // Add each candidate to the message
        for (Map.Entry<String, Integer> entry : sortedResults) {
            String candidate = entry.getKey();
            int votes = entry.getValue();
            double percentage = totalVotes > 0 ? (votes * 100.0) / totalVotes : 0;

            Component line = Component.text()
                    .append(Component.text("• " + candidate + ": ", NamedTextColor.WHITE))
                    .append(Component.text(votes + " votes", NamedTextColor.GREEN))
                    .append(Component.text(" (" + String.format("%.1f", percentage) + "%)", NamedTextColor.GRAY))
                    .append(Component.newline())
                    .build();

            message = message.append(line);
        }

        sender.sendMessage(message);
        return true;
    }
}