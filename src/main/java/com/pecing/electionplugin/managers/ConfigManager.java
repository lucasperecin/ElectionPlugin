package com.pecing.electionplugin.managers;

import com.pecing.electionplugin.ElectionSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ElectionSystem plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(ElectionSystem plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        
        // Set default values if they don't exist
        setDefaults();
    }

    private void setDefaults() {
        if (!config.contains("settings.default-language")) {
            config.set("settings.default-language", "en_US");
        }
        if (!config.contains("settings.auto-save-interval")) {
            config.set("settings.auto-save-interval", 300);
        }
        if (!config.contains("election.max-candidates")) {
            config.set("election.max-candidates", 10);
        }
        if (!config.contains("election.allow-candidate-removal")) {
            config.set("election.allow-candidate-removal", true);
        }
        if (!config.contains("election.broadcast-results")) {
            config.set("election.broadcast-results", true);
        }
        if (!config.contains("messages.use-action-bar")) {
            config.set("messages.use-action-bar", false);
        }
        if (!config.contains("messages.show-vote-confirmations")) {
            config.set("messages.show-vote-confirmations", true);
        }
        
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("❌ Error saving config.yml: " + e.getMessage());
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        setDefaults();
    }

    // Getters for configuration values
    public String getDefaultLanguage() {
        return config.getString("settings.default-language", "en_US");
    }

    public int getAutoSaveInterval() {
        return config.getInt("settings.auto-save-interval", 300);
    }

    public int getMaxCandidates() {
        return config.getInt("election.max-candidates", 10);
    }

    public boolean allowCandidateRemoval() {
        return config.getBoolean("election.allow-candidate-removal", true);
    }

    public boolean broadcastResults() {
        return config.getBoolean("election.broadcast-results", true);
    }

    public boolean useActionBar() {
        return config.getBoolean("messages.use-action-bar", false);
    }

    public boolean showVoteConfirmations() {
        return config.getBoolean("messages.show-vote-confirmations", true);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}