package com.pecing.electionplugin.commands;

import com.pecing.electionplugin.SistemaEleicoes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EleicaoCommand implements CommandExecutor, TabCompleter {

    private final SistemaEleicoes plugin;

    public EleicaoCommand(SistemaEleicoes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("resetareleicao")) {
            if (!sender.hasPermission("electionplugin.admin")) {
                sender.sendMessage(Component.text("❌ Você não tem permissão para usar este comando!", NamedTextColor.RED));
                return true;
            }
            plugin.getEleicaoManager().resetarEleicao();
            sender.sendMessage(Component.text("✅ Eleição resetada com sucesso!", NamedTextColor.GREEN));
            return true;
        }

        if (!sender.hasPermission("electionplugin.admin")) {
            sender.sendMessage(Component.text("❌ Você não tem permissão para usar este comando!", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            mostrarAjuda(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "criar":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("❌ Uso: /eleicao criar <nome do player>", NamedTextColor.RED));
                    return true;
                }
                if (plugin.getEleicaoManager().adicionarCandidato(args[1])) {
                    sender.sendMessage(Component.text("✅ Candidato " + args[1] + " adicionado com sucesso!", NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text("❌ Este candidato já existe!", NamedTextColor.RED));
                }
                break;

            case "remover":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("❌ Uso: /eleicao remover <nome do player>", NamedTextColor.RED));
                    return true;
                }
                if (plugin.getEleicaoManager().removerCandidato(args[1])) {
                    sender.sendMessage(Component.text("✅ Candidato " + args[1] + " removido com sucesso!", NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text("❌ Candidato não encontrado!", NamedTextColor.RED));
                }
                break;

            default:
                mostrarAjuda(sender);
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
            return Arrays.asList("criar", "remover");
        }

        return new ArrayList<>();
    }

    private void mostrarAjuda(CommandSender sender) {
        sender.sendMessage(Component.text("📋 === COMANDOS ELEIÇÃO ===", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/eleicao criar <player> - Adiciona um candidato", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/eleicao remover <player> - Remove um candidato", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/resetareleicao - Reseta toda a eleição", NamedTextColor.YELLOW));
    }
}