package me.vqlt.bladesmp.abilityone;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class FlameAbilityOne {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // Variables for the config values
    private final double damage;
    private final double knockback;
    private final long cooldown;
    private final double range;

    // Variable for the colour for the messages
    private static final TextColor FLAME_COLOR = TextColor.fromHexString("#FF4A1C");

    public FlameAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.damage = plugin.getConfig().getDouble("flame.ability-one.damage", 24.0);

        this.knockback = plugin.getConfig().getDouble("flame.ability-one.knockback", 1.2);

        this.cooldown = plugin.getConfig().getLong("flame.ability-one.cooldown", 60) * 1000L;

        this.range = plugin.getConfig().getDouble("flame.ability-one.range", 4);
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "flameone")) {
            // Get the milliseconds remaining from config, divide by 1000 to turn it into seconds, and round it up to the nearest whole number
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "flameone") / 1000.0);
            player.sendMessage(Component.text("🔥 Flame Sweep is on cooldown for " + seconds + "s").color(FLAME_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the flame blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!bladeManager.isFlameBlade(hand)) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.sendMessage(Component.text("🔥 Flame Sweep activated").color(FLAME_COLOR));

        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.85f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.55f, 1.35f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 0.45f, 0.8f);

        // Gets the direction the player is facing and normalises the vector
        Vector direction = player.getLocation().getDirection().normalize();

        // Get all entities in the range (from config) of the player
        for (Entity entity : player.getNearbyEntities(range, range, range)) {

            // Ignores any entities that aren't living (eg arrows)
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            // Calculate the vector from the player to the target, and normalises it
            Vector toTarget = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();

            // Calculates the angle between the direction the player is facing and the direction from the player to the target
            // then converts it from radians to degrees
            double targetAngle = Math.toDegrees(direction.angle(toTarget));

            // Checks if the angle is greater than 60 degrees, if so then the player is ignored. 60 degrees as it is 60 degrees for either side the player is facing
            if (targetAngle > 60) {
                continue;
            }

            // Damages the player with the config value
            target.damage(damage, player);

            // Fetches the knockback value from config, then sets the Y velocity to 0.4
            Vector knockbackVelocity = toTarget.multiply(knockback);

            knockbackVelocity.setY(0.4);

            // Set the players velocity to the value calculated earlier
            target.setVelocity(knockbackVelocity);
        }

        // Start the cooldown
        cooldownManager.startCooldown(id, "flameone", cooldown);
    }
}