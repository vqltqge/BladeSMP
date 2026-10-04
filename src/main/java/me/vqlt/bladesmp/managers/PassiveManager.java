package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PassiveManager {
    private final BladeSMP plugin;
    private final HashMap<UUID, Integer> frostHits = new HashMap<UUID, Integer>();
    private final Set<UUID> passiveFreezePlayers = new HashSet<>();
    private final int frostHitsToFreeze;
    private final double passiveFreezeDuration;

    public Set<UUID> getPassiveFreezePlayers() {
        return passiveFreezePlayers;
    }

    public PassiveManager(BladeSMP plugin) {
        this.plugin = plugin;
        this.frostHitsToFreeze = plugin.getConfig().getInt("frost.passive.hits-to-freeze", 10);
        this.passiveFreezeDuration = plugin.getConfig().getDouble("frost.passive.freeze-duration", 0.5) * 20L;
    }

    public void freezePassive(Player target, Player damager) {
        UUID id = damager.getUniqueId();

        int hits = frostHits.getOrDefault(id, 0);

        hits++;

        frostHits.put(id, hits);

        if (hits >= frostHitsToFreeze) {
            passiveFreezePlayers.add(target.getUniqueId());

            new BukkitRunnable() {
                @Override
                public void run() {
                    passiveFreezePlayers.remove(target.getUniqueId());
                }
            }.runTaskLater(plugin, (long) passiveFreezeDuration);

            frostHits.put(id, 0);
        }

    }

    public void flamePassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0));
    }
}
