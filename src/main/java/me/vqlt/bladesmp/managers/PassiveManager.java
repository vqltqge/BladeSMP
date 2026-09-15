package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class PassiveManager {
    private final BladeSMP plugin;
    private final HashMap<UUID, Integer> frostHits = new HashMap<UUID, Integer>();
    private final HashMap<UUID, Integer> staticHits = new HashMap<UUID, Integer>();

    public PassiveManager(BladeSMP plugin) {
        this.plugin = plugin;
    }

    public void freezePassive(Player target, Player damager) {
        UUID id = damager.getUniqueId();

        int hits = frostHits.getOrDefault(id, 0);

        hits++;

        frostHits.put(id, hits);

        if (hits >= 10) {
            target.setFreezeTicks(120);

            frostHits.put(id, 0);
        }

    }

    public void staticPassive(Player damager, Location location, World world) {
        UUID id = damager.getUniqueId();

        int hits = staticHits.getOrDefault(id, 0);

        hits++;

        staticHits.put(id, hits);

        if (hits >= 10) {
            world.strikeLightning(location);

            staticHits.put(id, 0);
        }
    }

    public void tidalPassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 40, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 40, 0));
    }

    public void flamePassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0));
    }
}
