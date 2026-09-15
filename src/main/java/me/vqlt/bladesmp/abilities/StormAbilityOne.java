package me.vqlt.bladesmp.abilities;


import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StormAbilityOne implements Listener {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final int strikeHits;
    private final Set<UUID> armedPlayers = new HashSet<>();

    private final Map<UUID, Integer> hitCount = new HashMap<>();


    public StormAbilityOne(BladeSMP plugin, BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;

        this.cooldown = plugin.getConfig().getLong("storm.ability-one.cooldown", 45) * 1000L;
        this.duration = plugin.getConfig().getLong("storm.ability-one.duration", 10) * 1000L;
        this.strikeHits = plugin.getConfig().getInt("storm.ability-one.strike-hits", 1);
    }


    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "stormone")) {
            player.sendMessage("Ability One is on cooldown");
            return;
        }

        armedPlayers.add(id);


    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        UUID id = attacker.getUniqueId();
        Location location = event.getEntity().getLocation();
        World world = event.getEntity().getWorld();

        ItemStack hand = attacker.getInventory().getItemInMainHand();

        if (!(bladeManager.isStormBlade(hand))) {
            return;
        }

        if (armedPlayers.contains(id)) {
            durationManager.startDuration(id, "stormone", duration);
            // When I make the config - define COOLDOWN as the config value plus the value of the duration, as this starts the cooldown when the duration starts
            cooldownManager.startCooldown(id, "stormone", cooldown);
            armedPlayers.remove(id);

            hitCount.put(id, 0);
        }

        if (!durationManager.isActive(id, "stormone")) {
            return;
        }

        int hits = hitCount.getOrDefault(id, 0) + 1;

        if (hits >= strikeHits) {
            world.strikeLightning(location);
            hitCount.put(id, 0);
        } else {
            hitCount.put(id, hits);
        }

    }
}
