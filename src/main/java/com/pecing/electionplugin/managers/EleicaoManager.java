package com.pecing.electionplugin.managers;

import com.pecing.electionplugin.SistemaEleicoes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EleicaoManager {

    private final SistemaEleicoes plugin;
    private File configFile;
    private FileConfiguration eleicaoConfig;

    private Set<String> candidatos;
    private Map<String, String> votos; // Jogador -> Candidato votado
    private Set<UUID> jogadoresQueVotaram;

    public EleicaoManager(SistemaEleicoes plugin) {
        this.plugin = plugin;
        this.candidatos = new HashSet<>();
        this.votos = new HashMap<>();
        this.jogadoresQueVotaram = new HashSet<>();
        loadData();
    }

    public void loadData() {
        configFile = new File(plugin.getDataFolder(), "eleicao.yml");
        if (!configFile.exists()) {
            plugin.saveResource("eleicao.yml", false);
        }
        eleicaoConfig = YamlConfiguration.loadConfiguration(configFile);

        // Carregar candidatos
        if (eleicaoConfig.contains("candidatos")) {
            candidatos = new HashSet<>(eleicaoConfig.getStringList("candidatos"));
        }

        // Carregar votos
        if (eleicaoConfig.contains("votos")) {
            votos.clear();
            for (String key : eleicaoConfig.getConfigurationSection("votos").getKeys(false)) {
                votos.put(key, eleicaoConfig.getString("votos." + key));
            }
        }

        // Carregar jogadores que votaram
        if (eleicaoConfig.contains("jogadoresQueVotaram")) {
            jogadoresQueVotaram.clear();
            for (String uuidStr : eleicaoConfig.getStringList("jogadoresQueVotaram")) {
                jogadoresQueVotaram.add(UUID.fromString(uuidStr));
            }
        }
    }

    public void saveData() {
        if (eleicaoConfig == null || configFile == null) {
            return;
        }

        try {
            // Salvar candidatos
            eleicaoConfig.set("candidatos", new ArrayList<>(candidatos));

            // Salvar votos
            eleicaoConfig.set("votos", null);
            for (Map.Entry<String, String> entry : votos.entrySet()) {
                eleicaoConfig.set("votos." + entry.getKey(), entry.getValue());
            }

            // Salvar jogadores que votaram
            List<String> uuidList = new ArrayList<>();
            for (UUID uuid : jogadoresQueVotaram) {
                uuidList.add(uuid.toString());
            }
            eleicaoConfig.set("jogadoresQueVotaram", uuidList);

            eleicaoConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("❌ Erro ao salvar dados da eleição: " + e.getMessage());
        }
    }

    public boolean adicionarCandidato(String nome) {
        if (candidatos.contains(nome.toLowerCase())) {
            return false;
        }
        candidatos.add(nome.toLowerCase());
        saveData();
        return true;
    }

    public boolean removerCandidato(String nome) {
        if (!candidatos.contains(nome.toLowerCase())) {
            return false;
        }
        candidatos.remove(nome.toLowerCase());
        
        // Remover votos para este candidato
        votos.entrySet().removeIf(entry -> entry.getValue().equalsIgnoreCase(nome));
        saveData();
        return true;
    }

    public boolean votar(Player jogador, String candidato) {
        if (!candidatos.contains(candidato.toLowerCase())) {
            return false;
        }

        if (jogadoresQueVotaram.contains(jogador.getUniqueId())) {
            return false;
        }

        jogadoresQueVotaram.add(jogador.getUniqueId());
        votos.put(jogador.getName().toLowerCase(), candidato.toLowerCase());
        saveData();
        return true;
    }

    public boolean jaVotou(Player jogador) {
        return jogadoresQueVotaram.contains(jogador.getUniqueId());
    }

    public Set<String> getCandidatos() {
        return new HashSet<>(candidatos);
    }

    public Map<String, Integer> getResultados() {
        Map<String, Integer> resultados = new HashMap<>();
        
        for (String candidato : candidatos) {
            resultados.put(candidato, 0);
        }

        for (String candidatoVotado : votos.values()) {
            resultados.put(candidatoVotado, resultados.getOrDefault(candidatoVotado, 0) + 1);
        }

        return resultados;
    }

    public void resetarEleicao() {
        candidatos.clear();
        votos.clear();
        jogadoresQueVotaram.clear();
        saveData();
    }

    public int getTotalVotos() {
        return votos.size();
    }
}