package com.pecing.electionplugin.commands;

import com.pecing.electionplugin.SistemaEleicoes;
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

public class ResultadoCommand implements CommandExecutor {

    private final SistemaEleicoes plugin;

    public ResultadoCommand(SistemaEleicoes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var resultados = plugin.getEleicaoManager().getResultados();
        int totalVotos = plugin.getEleicaoManager().getTotalVotos();

        if (resultados.isEmpty()) {
            sender.sendMessage(Component.text("ℹ️ Não há candidatos ou votos na eleição atual!", NamedTextColor.YELLOW));
            return true;
        }

        Component mensagem = Component.text()
                .append(Component.text("📊 === RESULTADOS DA ELEIÇÃO ===", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.newline())
                .append(Component.text("Total de votos: " + totalVotos, NamedTextColor.YELLOW))
                .append(Component.newline())
                .build();

        // Criar lista ordenada
        List<Map.Entry<String, Integer>> resultadosOrdenados = new ArrayList<>(resultados.entrySet());
        resultadosOrdenados.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        // Adicionar cada candidato à mensagem
        for (Map.Entry<String, Integer> entry : resultadosOrdenados) {
            String candidato = entry.getKey();
            int votos = entry.getValue();
            double percentual = totalVotos > 0 ? (votos * 100.0) / totalVotos : 0;

            Component linha = Component.text()
                    .append(Component.text("• " + candidato + ": ", NamedTextColor.WHITE))
                    .append(Component.text(votos + " votos", NamedTextColor.GREEN))
                    .append(Component.text(" (" + String.format("%.1f", percentual) + "%)", NamedTextColor.GRAY))
                    .append(Component.newline())
                    .build();

            mensagem = mensagem.append(linha);
        }

        sender.sendMessage(mensagem);
        return true;
    }
}