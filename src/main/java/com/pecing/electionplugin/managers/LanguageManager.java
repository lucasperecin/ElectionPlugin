package com.pecing.electionplugin.managers;

import com.pecing.electionplugin.ElectionSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LanguageManager {

    private final ElectionSystem plugin;
    private final ConfigManager configManager;
    private final Map<String, FileConfiguration> languages;
    private final Map<UUID, String> playerLanguages;

    public LanguageManager(ElectionSystem plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.languages = new HashMap<>();
        this.playerLanguages = new HashMap<>();
        loadLanguages();
    }

    private void loadLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
            // Save default language files
            plugin.saveResource("lang/messages_pt_BR.yml", false);
            plugin.saveResource("lang/messages_en_US.yml", false);
        }

        // Load all language files
        File[] languageFiles = langFolder.listFiles((dir, name) -> name.startsWith("messages_") && name.endsWith(".yml"));
        if (languageFiles != null) {
            for (File file : languageFiles) {
                String langCode = file.getName().replace("messages_", "").replace(".yml", "");
                languages.put(langCode, YamlConfiguration.loadConfiguration(file));
                plugin.getLogger().info("✅ Language loaded: " + langCode);
            }
        }

        if (languages.isEmpty()) {
            plugin.getLogger().warning("❌ No language files found!");
        }
    }

    public void setPlayerLanguage(Player player, String language) {
        if (languages.containsKey(language)) {
            playerLanguages.put(player.getUniqueId(), language);
            // Send confirmation message in the new language
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("language", language);
            player.sendMessage(getComponent("language.changed", player, placeholders));
        } else {
            player.sendMessage(getComponent("language.not_found", player));
        }
    }

    public String getPlayerLanguage(Player player) {
        return playerLanguages.getOrDefault(player.getUniqueId(), configManager.getDefaultLanguage());
    }

    public String getMessage(String path, Player player) {
        return getMessage(path, player, null);
    }

    public String getMessage(String path, Player player, Map<String, String> placeholders) {
        String langCode = player != null ? getPlayerLanguage(player) : configManager.getDefaultLanguage();
        FileConfiguration langConfig = languages.get(langCode);
        
        // Fallback to default language if selected language not found
        if (langConfig == null) {
            langConfig = languages.get(configManager.getDefaultLanguage());
            if (langConfig == null) {
                return "§cMessage not found: " + path;
            }
        }

        String message = langConfig.getString(path);
        if (message == null) {
            // Try default language as final fallback
            if (!langCode.equals(configManager.getDefaultLanguage())) {
                langConfig = languages.get(configManager.getDefaultLanguage());
                if (langConfig != null) {
                    message = langConfig.getString(path);
                }
            }
            if (message == null) {
                return "§cMessage not found: " + path;
            }
        }

        // Apply placeholders
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        return message;
    }

    public Component getComponent(String path, Player player) {
        return getComponent(path, player, null);
    }

    public Component getComponent(String path, Player player, Map<String, String> placeholders) {
        String message = getMessage(path, player, placeholders);
        return MiniMessage.miniMessage().deserialize(message);
    }

    public void reloadLanguages() {
        languages.clear();
        loadLanguages();
    }

    public boolean isLanguageAvailable(String language) {
        return languages.containsKey(language);
    }

    public String getAvailableLanguages() {
        return String.join(", ", languages.keySet());
    }
}