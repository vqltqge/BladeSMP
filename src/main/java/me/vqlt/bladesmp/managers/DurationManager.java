package me.vqlt.bladesmp.managers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DurationManager {

    private final Map<UUID, Map<String, Long>> durations = new HashMap<>();
    private final JavaPlugin plugin;

    public DurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startDuration(UUID id, String abilityId, long durationMillis) {
        durations.computeIfAbsent(id, ignored -> new HashMap<>()).put(abilityId, System.currentTimeMillis() + durationMillis);
    }

    public boolean isActive(UUID id, String abilityId) {
        return getRemainingMillis(id, abilityId) > 0;
    }

    public long getRemainingMillis(UUID id, String abilityId) {
        Map<String, Long> playerDurations = durations.get(id);

        if (playerDurations == null) {
            return 0;
        }

        Long expiryTime = playerDurations.get(abilityId);

        if (expiryTime == null) {
            return 0;
        }

        long remaining = expiryTime - System.currentTimeMillis();

        if (remaining <= 0) {
            playerDurations.remove(abilityId);

            if (playerDurations.isEmpty()) {
                durations.remove(id);
            }

            return 0;
        }

        return remaining;
    }

    public void runAfter(long seconds, Runnable task) {
        Bukkit.getScheduler().runTaskLater(plugin, task, 20L * seconds);
    }
}
