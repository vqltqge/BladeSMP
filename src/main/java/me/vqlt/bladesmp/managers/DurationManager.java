package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.abilityone.StormAbilityOne;
import me.vqlt.bladesmp.abilitytwo.DragonAbilityTwo;
import me.vqlt.bladesmp.abilitytwo.FortuneAbilityTwo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DurationManager {

    private final Map<UUID, Map<String, Long>> durations = new HashMap<>();
    private final JavaPlugin plugin;
    private StormAbilityOne stormAbilityOne;
    private DragonAbilityTwo dragonAbilityTwo;
    private FortuneAbilityTwo fortuneAbilityTwo;

    public DurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setStormAbilityOne(StormAbilityOne ability) {
        this.stormAbilityOne = ability;
    }

    public void setDragonAbilityTwo(DragonAbilityTwo ability) {
        this.dragonAbilityTwo = ability;
    }

    public void setFortuneAbilityTwo(FortuneAbilityTwo ability) {
        this.fortuneAbilityTwo = ability;
    }

    public void startDuration(UUID id, String abilityId, long durationMillis) {
        durations.computeIfAbsent(id, ignored -> new HashMap<>()).put(abilityId, System.currentTimeMillis() + durationMillis);
    }

    public boolean isActive(UUID id, String abilityId) {
        return getRemainingMillis(id, abilityId) > 0;
    }

    public boolean isCharged(UUID id, String abilityID) {
        if (abilityID.equals("stormone")) {
            return stormAbilityOne.getArmedPlayers().contains(id);
        } else if (abilityID.equals("dragontwo")) {
            return dragonAbilityTwo.armedPlayers.contains(id);
        } else if (abilityID.equals("fortunetwo")) {
            return fortuneAbilityTwo.getArmedPlayers().contains(id);
        }

        return false;
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
