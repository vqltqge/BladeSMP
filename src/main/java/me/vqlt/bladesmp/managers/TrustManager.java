package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TrustManager {
    private final BladeSMP plugin;
    private final File file;
    private final FileConfiguration config;

    public TrustManager(BladeSMP plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "trust.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create trust.yml");
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void trustPlayer(UUID owner, UUID target) {
        List<String> trusted = config.getStringList("trusted-players." + owner);

        if (!trusted.contains(target.toString())) {
            trusted.add(target.toString());
        }

        config.set("trusted-players." + owner, trusted);
        saveTrusts();
    }

    public boolean isTrusted(UUID owner, UUID target) {
        List<String> trusted = config.getStringList("trusted-players." + owner);

        return trusted.contains(target.toString());
    }

    public void saveTrusts() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save trust.yml");
            e.printStackTrace();
            ;
        }
    }
}
