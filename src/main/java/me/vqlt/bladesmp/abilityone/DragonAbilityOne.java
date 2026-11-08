package me.vqlt.bladesmp.abilityone;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.TrustManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class DragonAbilityOne {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final TrustManager trustManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double radius;
    private final double damage;
    private final double knockback;
    private final double upwardKnockback;

    private static final TextColor DRAGON_COLOR = TextColor.fromHexString("#5A189A");

    public DragonAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, TrustManager trustManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.trustManager = trustManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("dragon.ability-one.cooldown", 60) * 1000L;
        this.radius = plugin.getConfig().getDouble("dragon.ability-one.radius", 4.0);
        this.damage = plugin.getConfig().getDouble("dragon.ability-one.damage", 50.0);
        this.knockback = plugin.getConfig().getDouble("dragon.ability-one.knockback", 2.0);
        this.upwardKnockback = plugin.getConfig().getDouble("dragon.ability-one.upward-knockback", 0.7);
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "dragonone")) {
            // Get the milliseconds remaining from config, divide by 1000 to turn it into seconds, and round it up to the nearest whole number
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "dragonone") / 1000.0);
            player.sendMessage(Component.text("☄ Dragon's Wrath is on cooldown for " + seconds + "s").color(DRAGON_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the flame blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!bladeManager.isDragonBlade(hand)) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.sendMessage(Component.text("☄ Dragon's Wrath activated").color(DRAGON_COLOR));

//        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.85f);
//        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.55f, 1.35f);
//        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 0.45f, 0.8f);

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

            if (entity instanceof Player victim && trustManager.isTrusted(player.getUniqueId(), victim.getUniqueId())) {
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
        cooldownManager.startCooldown(id, "dragonone", cooldown);


    }
}
