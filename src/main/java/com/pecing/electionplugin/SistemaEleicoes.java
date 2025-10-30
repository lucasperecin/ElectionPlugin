package com.pecing.electionplugin;

import com.pecing.electionplugin.commands.EleicaoCommand;
import com.pecing.electionplugin.commands.ResultadoCommand;
import com.pecing.electionplugin.commands.VotarCommand;
import com.pecing.electionplugin.managers.EleicaoManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SistemaEleicoes extends JavaPlugin {

    private EleicaoManager eleicaoManager;

    @Override
    public void onEnable() {
        // Criar pasta do plugin se não existir
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Inicializar manager
        this.eleicaoManager = new EleicaoManager(this);
        
        // Registrar comandos
        getCommand("votar").setExecutor(new VotarCommand(this));
        getCommand("eleicao").setExecutor(new EleicaoCommand(this));
        getCommand("resultado").setExecutor(new ResultadoCommand(this));
        getCommand("resetareleicao").setExecutor(new EleicaoCommand(this));
        
        getLogger().info("✅ Sistema de Eleições ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        if (eleicaoManager != null) {
            eleicaoManager.saveData();
        }
        getLogger().info("❌ Sistema de Eleições desativado!");
    }

    public EleicaoManager getEleicaoManager() {
        return eleicaoManager;
    }
}