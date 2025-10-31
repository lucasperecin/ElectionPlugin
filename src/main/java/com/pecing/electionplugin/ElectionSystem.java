package com.pecing.electionplugin;

import com.pecing.electionplugin.commands.ElectionCommand;
import com.pecing.electionplugin.commands.ResultsCommand;
import com.pecing.electionplugin.commands.VoteCommand;
import com.pecing.electionplugin.managers.ConfigManager;
import com.pecing.electionplugin.managers.ElectionManager;
import com.pecing.electionplugin.managers.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElectionSystem extends JavaPlugin {

    private ConfigManager configManager;
    private ElectionManager electionManager;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        // Create plugin folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize managers in correct order
        this.configManager = new ConfigManager(this);
        this.electionManager = new ElectionManager(this);
        this.languageManager = new LanguageManager(this, configManager);
        
        // Register commands
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("election").setExecutor(new ElectionCommand(this));
        getCommand("results").setExecutor(new ResultsCommand(this));
        getCommand("resetelection").setExecutor(new ElectionCommand(this));
        
        // Start auto-save task if enabled
        startAutoSaveTask();
        
        getLogger().info("✅ Election System activated successfully!");
        getLogger().info("✅ Default language: " + configManager.getDefaultLanguage());
    }

    @Override
    public void onDisable() {
        if (electionManager != null) {
            electionManager.saveData();
        }
        getLogger().info("❌ Election System deactivated!");
    }

    private void startAutoSaveTask() {
        int interval = configManager.getAutoSaveInterval();
        if (interval > 0) {
            getServer().getScheduler().runTaskTimer(this, () -> {
                if (electionManager != null) {
                    electionManager.saveData();
                    getLogger().info("💾 Election data auto-saved");
                }
            }, interval * 20L, interval * 20L); // Convert seconds to ticks
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ElectionManager getElectionManager() {
        return electionManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}