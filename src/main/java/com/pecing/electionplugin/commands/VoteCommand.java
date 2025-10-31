package com.pecing.electionplugin.commands;

import com.pecing.electionplugin.ElectionSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {

    private final ElectionSystem plugin;

    public VoteCommand(ElectionSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("❌ This command can only be executed by players!", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (plugin.getElectionManager().hasVoted(player)) {
            player.sendMessage(Component.text("❌ You have already voted in this election!", NamedTextColor.RED));
            return true;
        }

        if (args.length == 1) {
            // Vote for specific candidate
            String candidate = args[0];
            if (plugin.getElectionManager().vote(player, candidate)) {
                player.sendMessage(Component.text("✅ You voted for " + candidate + "!", NamedTextColor.GREEN));
            } else {
                player.sendMessage(Component.text("❌ Candidate not found or you have already voted!", NamedTextColor.RED));
            }
            return true;
        }

        // Show clickable candidate list
        showCandidates(player);
        return true;
    }

    private void showCandidates(Player player) {
        var candidates = plugin.getElectionManager().getCandidates();
        
        if (candidates.isEmpty()) {
            player.sendMessage(Component.text("ℹ️ There are no candidates in the current election!", NamedTextColor.YELLOW));
            return;
        }

        Component message = Component.text()
                .append(Component.text("🗳️ === ELECTION CANDIDATES ===", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.newline())
                .build();

        for (String candidate : candidates) {
            Component line = Component.text()
                    .append(Component.text("• " + candidate, NamedTextColor.WHITE))
                    .append(Component.space())
                    .append(Component.text("[VOTE]", NamedTextColor.GREEN, TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/vote " + candidate))
                            .hoverEvent(Component.text("Click to vote for " + candidate, NamedTextColor.YELLOW)))
                    .append(Component.newline())
                    .build();
            
            message = message.append(line);
        }

        player.sendMessage(message);
        player.sendMessage(Component.text("💡 Click [VOTE] next to the candidate to vote!", NamedTextColor.GRAY));
    }
}