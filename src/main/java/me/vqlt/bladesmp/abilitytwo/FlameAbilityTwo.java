package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class FlameAbilityTwo {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // Variables for the config values
    private final double radius;
    private final double damage;
    private final double knockback;
    private final double upwardKnockback;
    private final long cooldown;

    private static final TextColor FLAME_COLOR = TextColor.fromHexString("#FF4A1C");

    public FlameAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.radius = plugin.getConfig().getDouble("flame.ability-two.radius", 3.0);
        this.damage = plugin.getConfig().getDouble("flame.ability-two.damage", 40.0);
        this.knockback = plugin.getConfig().getDouble("flame.ability-two.knockback", 2.0);
        this.upwardKnockback = plugin.getConfig().getDouble("flame.ability-two.upward-knockback", 0.7);
        this.cooldown = plugin.getConfig().getLong("flame.ability-two.cooldown", 90) * 1000L;
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "flametwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "flametwo") / 1000.0);
            player.sendMessage(Component.text("🔥 Inferno is on cooldown for " + seconds + "s").color(FLAME_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the flame blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFlameBlade(hand))) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 0.8f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.45f, 0.65f);
        player.getLocation().getWorld().sendMessage(Component.text("🔥 Inferno activated").color(FLAME_COLOR));

        // Get the center of the explosion at the player's current location
        Location center = player.getLocation();

        // Check all entities in the configured radius
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            // If the entity is not a living entity for example an arrow ignore it
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            // If the entity is not in a configured radius circle of the player, ignore it
            if (entity.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }

            // Damage the entity
            target.damage(damage, player);

            // Calculate the players location as a vector and subtract the centers vector from it. Normalize the vector and multiply it by the knockback
            Vector direction = target.getLocation().toVector().subtract(center.toVector()).normalize().multiply(knockback);

            // Give the vector upwards knockback
            direction.setY(upwardKnockback);

            // Apply the knockback
            target.setVelocity(direction);
        }

        // Start the cooldown
        cooldownManager.startCooldown(id, "flametwo", cooldown);
    }
}
