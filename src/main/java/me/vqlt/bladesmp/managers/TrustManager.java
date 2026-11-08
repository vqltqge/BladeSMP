package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TrustManager {

    private final BladeSMP plugin;

    public TrustManager(BladeSMP plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Set<UUID>> trustedPlayers = new HashMap<>();

    public boolean trustPlayer(UUID owner, UUID target) {
        if (owner.equals(target)) {
            return false;
        }

        trustedPlayers.putIfAbsent(owner, new HashSet<>());

        return trustedPlayers.get(owner).add(target);
    }

    public boolean untrustPlayer(UUID owner, UUID target) {
        if (owner.equals(target)) {
            return false;
        }

        if (!trustedPlayers.containsKey(owner)) {
            return false;
        }

        return trustedPlayers.get(owner).remove(target);
    }

    public boolean isTrusted(UUID owner, UUID target) {
        return !owner.equals(target) && trustedPlayers.containsKey(owner) && trustedPlayers.get(owner).contains(target);
    }

    public void saveTrustData() {
        File file = new File(plugin.getDataFolder(), "trust.yml");
        FileConfiguration config = new YamlConfiguration();

        for (UUID owner : trustedPlayers.keySet()) {
            List<String> targets = trustedPlayers.get(owner).stream().map(UUID::toString).toList();

            config.set("players." + owner + ".trusted", targets);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTrustData() {
        File file = new File(plugin.getDataFolder(), "trust.yml");

        if (!file.exists()) {
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = config.getConfigurationSection("players");

        if (section == null) {
            return;
        }

        for (String ownerString : section.getKeys(false)) {
            UUID owner = UUID.fromString(ownerString);

            List<String> targets = config.getStringList("players." + ownerString + ".trusted");

            Set<UUID> trusted = new HashSet<>();

            for (String target : targets) {
                trusted.add(UUID.fromString(target));
            }

            trustedPlayers.put(owner, trusted);
        }
    }
}
