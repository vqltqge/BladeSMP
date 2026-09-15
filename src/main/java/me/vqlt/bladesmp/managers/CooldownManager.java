package me.vqlt.bladesmp.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public void startCooldown(UUID id, String abilityId, long cooldownMillis) {
        cooldowns.computeIfAbsent(id, ignored -> new HashMap<>()).put(abilityId, System.currentTimeMillis() + cooldownMillis);
    }

    public boolean isOnCooldown(UUID id, String abilityId) {
        return getRemainingMillis(id, abilityId) > 0;
    }

    public long getRemainingMillis(UUID id, String abilityId) {
        Map<String, Long> playerCooldowns = cooldowns.get(id);

        if (playerCooldowns == null) {
            return 0;
        }

        Long expiryTime = playerCooldowns.get(abilityId);

        if (expiryTime == null) {
            return 0;
        }

        long remaining = expiryTime - System.currentTimeMillis();

        if (remaining <= 0) {
            playerCooldowns.remove(abilityId);

            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(id);
            }

            return 0;
        }

        return remaining;
    }

    public void clearCooldowns(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

}
