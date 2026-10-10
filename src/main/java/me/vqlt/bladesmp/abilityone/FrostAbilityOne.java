package me.vqlt.bladesmp.abilityone;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FrostAbilityOne {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // A set that stores which players are frozen
    private final Set<UUID> freezePlayers = new HashSet<>();

    // A getter for the set
    public Set<UUID> getFreezePlayers() {
        return freezePlayers;
    }

    // Variables for the config values
    private final long cooldown;
    private final double dashVelocity;
    private final long freezeDuration;
    private final double damage;

    // Variable for the colour for the messages
    private static final TextColor FROST_COLOR = TextColor.fromHexString("#6EE7FF");

    public FrostAbilityOne(BladeSMP plugin, DurationManager durationManager, CooldownManager cooldownManager, BladeManager bladeManager) {
        this.plugin = plugin;
        this.durationManager = durationManager;
        this.cooldownManager = cooldownManager;
        this.bladeManager = bladeManager;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("frost.ability-one.cooldown", 45) * 1000L;
        this.freezeDuration = plugin.getConfig().getLong("frost.ability-one.freeze-duration", 3) * 20L;
        this.dashVelocity = plugin.getConfig().getDouble("frost.ability-one.dash-velocity", 2);
        this.damage = plugin.getConfig().getDouble("frost.ability-one.damage", 16.0);

    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "frostone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "frostone") / 1000.0);
            player.sendMessage(Component.text("✻ Frozen Dash is on cooldown for " + seconds + "s").color(FROST_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the frost blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFrostBlade(hand))) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.sendMessage(Component.text("✻ Frozen Dash activated").color(FROST_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.25f, 1.8f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 0.55f, 1.5f);

        // Increase the players velocity by the config value in the direction they are looking
        player.setVelocity(player.getLocation().getDirection().multiply(dashVelocity));

        // Create a set to store all the players that have been hit
        Set<UUID> hitPlayers = new HashSet<>();

        new BukkitRunnable() {
            // Ticks to count how many times the tasks has run
            int ticks = 0;

            @Override
            public void run() {
                // If the ticks exceed 20 stop the repeating task
                if (ticks >= 20) {
                    cancel();
                    return;
                }

                // Loop through all players in the world
                for (Player other : player.getWorld().getPlayers()) {
                    // Skip the player that activated the ability
                    if (other.equals(player)) {
                        continue;
                    }

                    // Skip the players that have already been hit
                    if (hitPlayers.contains(other.getUniqueId())) {
                        continue;
                    }

                    // Check if the two players hitboxes overlap
                    if (player.getBoundingBox().overlaps(other.getBoundingBox())) {
                        // If so add them to the list of hit players and players that are frozen
                        hitPlayers.add(other.getUniqueId());
                        freezePlayers.add(other.getUniqueId());

                        // Deal damage to the player and use the original player as the damage source
                        other.damage(damage, player);

                        // Remove the player from the list of frozen players at the end of the duration
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                freezePlayers.remove(other.getUniqueId());
                            }
                        }.runTaskLater(plugin, freezeDuration);

                    }
                }

                // Increase the counter of how many times the task has run
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // schedules the task to happen every tick

        // No need to say it is on cooldown as the ability is instant
        cooldownManager.startCooldown(id, "frostone", cooldown);
    }
}
