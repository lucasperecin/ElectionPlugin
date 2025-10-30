package com.pecing.electionplugin;

import com.pecing.electionplugin.commands.ElectionCommand;
import com.pecing.electionplugin.commands.ResultsCommand;
import com.pecing.electionplugin.commands.VoteCommand;
import com.pecing.electionplugin.managers.ElectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElectionSystem extends JavaPlugin {

    private ElectionManager electionManager;

    @Override
    public void onEnable() {
        // Create plugin folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize manager
        this.electionManager = new ElectionManager(this);
        
        // Register commands
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("election").setExecutor(new ElectionCommand(this));
        getCommand("results").setExecutor(new ResultsCommand(this));
        getCommand("resetelection").setExecutor(new ElectionCommand(this));
        
        getLogger().info("✅ Election System activated successfully!");
    }

    @Override
    public void onDisable() {
        if (electionManager != null) {
            electionManager.saveData();
        }
        getLogger().info("❌ Election System deactivated!");
    }

    public ElectionManager getElectionManager() {
        return electionManager;
    }
}