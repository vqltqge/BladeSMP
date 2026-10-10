package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PulseAbilityTwo {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // Variables for the config values
    private final long cooldown;
    private final double upwardVelocity;
    private final double downwardVelocity;
    private final double damage;
    private final double radius;
    private final double knockback;
    private final double upwardKnockback;

    // Variable for the colour for the messages
    private static final TextColor PULSE_COLOR = TextColor.fromHexString("#A855F7");

    public PulseAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("pulse.ability-two.cooldown", 60) * 1000L;
        this.upwardVelocity = plugin.getConfig().getDouble("pulse.ability-two.upward-velocity", 1.5);
        this.downwardVelocity = plugin.getConfig().getDouble("pulse.ability-two.downward-velocity", 1.5);
        this.damage = plugin.getConfig().getDouble("pulse.ability-two.damage", 24.0);
        this.radius = plugin.getConfig().getDouble("pulse.ability-two.radius", 3);
        this.knockback = plugin.getConfig().getDouble("pulse.ability-two.knockback", 1.2);
        this.upwardKnockback = plugin.getConfig().getDouble("pulse.ability-two.upward-knockback", 0.7);
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        // Get the item the player is holding and return if it is not the fortune blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isPulseBlade(hand))) {
            return;
        }

        if (cooldownManager.isOnCooldown(id, "pulsetwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "pulsetwo") / 1000.0);
            player.sendMessage(Component.text("➤ Shockwave is on cooldown for " + seconds + "s").color(PULSE_COLOR));
            return;
        }

        if (durationManager.isActive(id, "pulsetwo")) {
            player.sendMessage(Component.text("➤ Shockwave is already active").color(PULSE_COLOR));
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 0.8f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.45f, 0.65f);
        player.sendMessage(Component.text("➤ Shockwave activated").color(PULSE_COLOR));
        durationManager.startDuration(id, "pulsetwo", 3000);

        // Schedule to start the cooldown for the ability
        durationManager.runAfter((3), () -> {
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
            player.sendMessage(Component.text("➤ Shockwave on cooldown").color(PULSE_COLOR));
            cooldownManager.startCooldown(id, "pulsetwo", cooldown);
        });

        // Launch the player upwards
        player.setVelocity(new Vector(0, upwardVelocity, 0));

        // Calculate how long it will take the player to reach the peak of their upwards momentum
        long ticksToPeak = calculateTicksToPeak(upwardVelocity);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            // Launch the player downwards
            player.setVelocity(new Vector(0, -downwardVelocity, 0));

            // Start a scheduler to listen for the player landing
            new BukkitRunnable() {

                // Store the amount of times the plugin has checked for if the player has landed
                int ticks = 0;

                @Override
                public void run() {
                    // Increase the count
                    ticks++;

                    // If the player logs out, then cancel the task
                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }

                    // If the count exceeds 60, then cancel the task
                    if (ticks >= 60) {
                        cancel();
                        return;
                    }


                    // Then end this check but dont cancel the entire task
                    if (!hasLanded(player)) {
                        return;
                    }

                    // Get the location the player lands at
                    Location center = player.getLocation();

                    // For every entity in the configured radius
                    for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {

                        // If the entity is not living, ignore it
                        if (!(entity instanceof LivingEntity target)) {
                            continue;
                        }

                        // If the entity is not within the circle of configured radius, ignore it
                        if (entity.getLocation().distanceSquared(center) > radius * radius) {
                            continue;
                        }

                        // Damage the entity, with the damage source of the player
                        target.damage(damage, player);

                        // Get the location of the target as a vector, and subtract it from the vector of the center to find the direction from the center
                        // Normalise it, then multiply it by the knockback
                        Vector direction = target.getLocation().toVector().subtract(center.toVector()).normalize().multiply(knockback);

                        // Add upwards knockback to the vector
                        direction.setY(upwardKnockback);

                        // Apply the vector to the target
                        target.setVelocity(direction);
                    }

                    // Finish the repeating task as the ability has finished
                    cancel();
                }

                // Check if the player has landed every tick. There is a 1 tick delay as the upward velocity may not have applied yet
            }.runTaskTimer(plugin, 1L, 1L);

            // Launch the player downwards after the estimated time for the player to reach the peak
        }, ticksToPeak);
    }

    private long calculateTicksToPeak(double upwardVelocity) {
        // Make a copy of the upward velocity
        double velocity = upwardVelocity;
        // Ticks is a counter
        long ticks = 0;

        // Keep doing this while the player is still moving upwards
        while (velocity > 0) {
            // Reduces the velocity to estimate the effects of gravity and air resistance
            velocity = (velocity - 0.08) * 0.98;
            // Increase the number ticks taken
            ticks++;
        }

        // Return the estimated number of ticks it will take for the player to land
        return ticks;
    }

    private boolean hasLanded(Player player) {
        // Get the players hitbox
        BoundingBox box = player.getBoundingBox();

        // Subtracting 0.05 checks a bit below the players feet
        double y = box.getMinY() - 0.05;

        // Get the rightmost and leftmost X and Z coordinates. use math.floor to round down to the nearest whole number
        int minX = (int) Math.floor(box.getMinX());
        int maxX = (int) Math.floor(box.getMaxX());
        int minZ = (int) Math.floor(box.getMinZ());
        int maxZ = (int) Math.floor(box.getMaxZ());

        // Start x at the minimum x coordinate, and loop until it reaches the max x coordinate
        for (int x = minX; x <= maxX; x++) {
            // Start at the minimum z coordinate, and loop until it reaches the max z coordinate
            for (int z = minZ; z <= maxZ; z++) {
                // Get the block at those coordinates
                Block block = player.getWorld().getBlockAt(x, (int) Math.floor(y), z);
                // If it is a solid block then the player has landed
                if (block.getType().isSolid()) {
                    return true;
                }
            }
        }

        // The player has not landed
        return false;
    }
}
