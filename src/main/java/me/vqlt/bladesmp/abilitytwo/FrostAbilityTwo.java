package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FrostAbilityTwo {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // A set that stores frozen players
    private final Set<UUID> freezePlayers = new HashSet<>();

    // A getter for the set
    public Set<UUID> getFreezePlayers() {
        return freezePlayers;
    }

    // Variables for the config values
    private final long cooldown;
    private final long duration;
    private final double radius;
    private final double damageMultiplier;

    // Variable for the colour for the messages
    private static final TextColor FROST_COLOR = TextColor.fromHexString("#6EE7FF");

    // A getter for the damage multiplier
    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public FrostAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("frost.ability-two.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("frost.ability-two.duration", 5) * 1000L;
        this.radius = plugin.getConfig().getDouble("frost.ability-two.radius", 3);
        this.damageMultiplier = plugin.getConfig().getDouble("frost.ability-two.damage-multiplier", 1.1);
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        // Get the item the player is holding and return if it is not the fortune blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFrostBlade(hand))) {
            return;
        }

        if (cooldownManager.isOnCooldown(id, "frosttwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "frosttwo") / 1000.0);
            player.sendMessage(Component.text("✻ Frozen Domain is on cooldown for " + seconds + "s").color(FROST_COLOR));
            return;
        }

        if (durationManager.isActive(id, "frosttwo")) {
            player.sendMessage(Component.text("✻ Frozen Domain is already active").color(FROST_COLOR));
            return;
        }

        // Start the duration for the ability
        durationManager.startDuration(id, "frosttwo", duration);

        // Tells the player the ability has been activated. Play an activation sound at the players location
        player.sendMessage(Component.text("✻ Frozen Domain activated").color(FROST_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1, 0.7f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.65f, 0.7f);

        // Check all entities in the square radius around the player
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            // If the entity is not a player, ignore it
            if (!(entity instanceof Player target)) {
                continue;
            }

            // If the target is not in the circle of the same radius, ignore it
            if (target.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }

            // Get the targets UUID, and add them to the freeze players set
            UUID targetid = target.getUniqueId();

            freezePlayers.add(targetid);

            // Schedule to remove the target from the set after the duration
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                freezePlayers.remove(targetid);
            }, duration / 1000 * 20L);
        }

        // After the duration, start the cooldown, tell the player the ability is on cooldown, and play a sound
        durationManager.runAfter((duration / 1000), () -> {
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
            player.sendMessage(Component.text("✻ Frozen Domain on cooldown").color(FROST_COLOR));
            cooldownManager.startCooldown(id, "frosttwo", cooldown);
        });

    }
}
