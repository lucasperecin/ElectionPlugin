package com.pecing.electionplugin.managers;

import com.pecing.electionplugin.ElectionSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ElectionManager {

    private final ElectionSystem plugin;
    private File configFile;
    private FileConfiguration electionConfig;

    private Set<String> candidates;
    private Map<String, String> votes; // Player -> Candidate voted for
    private Set<UUID> playersWhoVoted;

    public ElectionManager(ElectionSystem plugin) {
        this.plugin = plugin;
        this.candidates = new HashSet<>();
        this.votes = new HashMap<>();
        this.playersWhoVoted = new HashSet<>();
        loadData();
    }

    public void loadData() {
        configFile = new File(plugin.getDataFolder(), "election.yml");
        if (!configFile.exists()) {
            plugin.saveResource("election.yml", false);
        }
        electionConfig = YamlConfiguration.loadConfiguration(configFile);

        // Load candidates
        if (electionConfig.contains("candidates")) {
            candidates = new HashSet<>(electionConfig.getStringList("candidates"));
        }

        // Load votes
        if (electionConfig.contains("votes")) {
            votes.clear();
            for (String key : electionConfig.getConfigurationSection("votes").getKeys(false)) {
                votes.put(key, electionConfig.getString("votes." + key));
            }
        }

        // Load players who voted
        if (electionConfig.contains("playersWhoVoted")) {
            playersWhoVoted.clear();
            for (String uuidStr : electionConfig.getStringList("playersWhoVoted")) {
                playersWhoVoted.add(UUID.fromString(uuidStr));
            }
        }
    }

    public void saveData() {
        if (electionConfig == null || configFile == null) {
            return;
        }

        try {
            // Save candidates
            electionConfig.set("candidates", new ArrayList<>(candidates));

            // Save votes
            electionConfig.set("votes", null);
            for (Map.Entry<String, String> entry : votes.entrySet()) {
                electionConfig.set("votes." + entry.getKey(), entry.getValue());
            }

            // Save players who voted
            List<String> uuidList = new ArrayList<>();
            for (UUID uuid : playersWhoVoted) {
                uuidList.add(uuid.toString());
            }
            electionConfig.set("playersWhoVoted", uuidList);

            electionConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("❌ Error saving election data: " + e.getMessage());
        }
    }

    public boolean addCandidate(String name) {
        if (candidates.contains(name.toLowerCase())) {
            return false;
        }
        candidates.add(name.toLowerCase());
        saveData();
        return true;
    }

    public boolean removeCandidate(String name) {
        if (!candidates.contains(name.toLowerCase())) {
            return false;
        }
        candidates.remove(name.toLowerCase());
        
        // Remove votes for this candidate
        votes.entrySet().removeIf(entry -> entry.getValue().equalsIgnoreCase(name));
        saveData();
        return true;
    }

    public boolean vote(Player player, String candidate) {
        if (!candidates.contains(candidate.toLowerCase())) {
            return false;
        }

        if (playersWhoVoted.contains(player.getUniqueId())) {
            return false;
        }

        playersWhoVoted.add(player.getUniqueId());
        votes.put(player.getName().toLowerCase(), candidate.toLowerCase());
        saveData();
        return true;
    }

    public boolean hasVoted(Player player) {
        return playersWhoVoted.contains(player.getUniqueId());
    }

    public Set<String> getCandidates() {
        return new HashSet<>(candidates);
    }

    public Map<String, Integer> getResults() {
        Map<String, Integer> results = new HashMap<>();
        
        for (String candidate : candidates) {
            results.put(candidate, 0);
        }

        for (String votedCandidate : votes.values()) {
            results.put(votedCandidate, results.getOrDefault(votedCandidate, 0) + 1);
        }

        return results;
    }

    public void resetElection() {
        candidates.clear();
        votes.clear();
        playersWhoVoted.clear();
        saveData();
    }

    public int getTotalVotes() {
        return votes.size();
    }
}