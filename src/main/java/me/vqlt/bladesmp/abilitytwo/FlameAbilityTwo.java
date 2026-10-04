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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

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

        this.radius = plugin.getConfig().getDouble("flame.ability-two.radius", 3.0);
        this.damage = plugin.getConfig().getDouble("flame.ability-two.damage", 40.0);
        this.knockback = plugin.getConfig().getDouble("flame.ability-two.knockback", 2.0);
        this.upwardKnockback = plugin.getConfig().getDouble("flame.ability-two.upward-knockback", 0.7);
        this.cooldown = plugin.getConfig().getLong("flame.ability-two.cooldown", 90) * 1000L;
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "flametwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "flametwo") / 1000.0);
            player.sendMessage(Component.text("🔥 Inferno is on cooldown for " + seconds + "s").color(FLAME_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFlameBlade(hand))) {
            return;
        }

        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 0.8f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.45f, 0.65f);
        player.getLocation().getWorld().sendMessage(Component.text("🔥 Inferno activated").color(FLAME_COLOR));
        // play sound

        Location center = player.getLocation();

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            if (entity.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }

            target.damage(damage, player);

            Vector direction = target.getLocation().toVector().subtract(center.toVector()).normalize().multiply(knockback);

            direction.setY(upwardKnockback);

            target.setVelocity(direction);
        }

        cooldownManager.startCooldown(id, "flametwo", cooldown);
    }
}
