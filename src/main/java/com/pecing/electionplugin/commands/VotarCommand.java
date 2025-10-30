package com.pecing.electionplugin.commands;

import com.pecing.electionplugin.SistemaEleicoes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotarCommand implements CommandExecutor {

    private final SistemaEleicoes plugin;

    public VotarCommand(SistemaEleicoes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("❌ Este comando só pode ser executado por jogadores!", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (plugin.getEleicaoManager().jaVotou(player)) {
            player.sendMessage(Component.text("❌ Você já votou nesta eleição!", NamedTextColor.RED));
            return true;
        }

        if (args.length == 1) {
            // Votar no candidato específico
            String candidato = args[0];
            if (plugin.getEleicaoManager().votar(player, candidato)) {
                player.sendMessage(Component.text("✅ Você votou em " + candidato + "!", NamedTextColor.GREEN));
            } else {
                player.sendMessage(Component.text("❌ Candidato não encontrado ou você já votou!", NamedTextColor.RED));
            }
            return true;
        }

        // Mostrar lista de candidatos clicáveis
        mostrarCandidatos(player);
        return true;
    }

    private void mostrarCandidatos(Player player) {
        var candidatos = plugin.getEleicaoManager().getCandidatos();
        
        if (candidatos.isEmpty()) {
            player.sendMessage(Component.text("ℹ️ Não há candidatos na eleição atual!", NamedTextColor.YELLOW));
            return;
        }

        Component mensagem = Component.text()
                .append(Component.text("🗳️ === CANDIDATOS DA ELEIÇÃO ===", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.newline())
                .build();

        for (String candidato : candidatos) {
            Component linha = Component.text()
                    .append(Component.text("• " + candidato, NamedTextColor.WHITE))
                    .append(Component.space())
                    .append(Component.text("[VOTAR]", NamedTextColor.GREEN, TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/votar " + candidato))
                            .hoverEvent(Component.text("Clique para votar em " + candidato, NamedTextColor.YELLOW)))
                    .append(Component.newline())
                    .build();
            
            mensagem = mensagem.append(linha);
        }

        player.sendMessage(mensagem);
        player.sendMessage(Component.text("💡 Clique em [VOTAR] ao lado do candidato para votar!", NamedTextColor.GRAY));
    }
}