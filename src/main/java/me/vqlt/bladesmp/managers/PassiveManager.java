package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class PassiveManager {
    private final BladeSMP plugin;
    private final HashMap<UUID, Integer> frostHits = new HashMap<UUID, Integer>();
    private final int frostHitsToFreeze;

    public PassiveManager(BladeSMP plugin) {
        this.plugin = plugin;
        this.frostHitsToFreeze = plugin.getConfig().getInt("frost.passive.hits-to-freeze", 10);
    }

    public void freezePassive(Player target, Player damager) {
        UUID id = damager.getUniqueId();

        int hits = frostHits.getOrDefault(id, 0);

        hits++;

        frostHits.put(id, hits);

        if (hits >= frostHitsToFreeze) {
            target.setFreezeTicks(120);

            frostHits.put(id, 0);
        }

    }

    public void flamePassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0));
    }
}
