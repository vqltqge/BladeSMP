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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double upwardVelocity;
    private final double downwardVelocity;
    private final double damage;
    private final double radius;
    private final double knockback;
    private final double upwardKnockback;

    private static final TextColor PULSE_COLOR = TextColor.fromHexString("#A855F7");

    public PulseAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("pulse.ability-two.cooldown", 60) * 1000L;
        this.upwardVelocity = plugin.getConfig().getDouble("pulse.ability-two.upward-velocity", 1.5);
        this.downwardVelocity = plugin.getConfig().getDouble("pulse.ability-two.downward-velocity", 1.5);
        this.damage = plugin.getConfig().getDouble("pulse.ability-two.damage", 24.0);
        this.radius = plugin.getConfig().getDouble("pulse.ability-two.radius", 3);
        this.knockback = plugin.getConfig().getDouble("pulse.ability-two.knockback", 1.2);
        this.upwardKnockback = plugin.getConfig().getDouble("pulse.ability-two.upward-knockback", 0.7);
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

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

        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 0.8f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.45f, 0.65f);
        player.sendMessage(Component.text("➤ Shockwave activated").color(PULSE_COLOR));
        durationManager.startDuration(id, "pulsetwo", 3000);

        durationManager.runAfter((3), () -> {
            player.playSound(
                    player.getLocation(),
                    Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE,
                    1,
                    1
            );

            player.sendMessage(
                    Component.text("➤ Shockwave on cooldown").color(PULSE_COLOR)
            );

            cooldownManager.startCooldown(id, "pulsetwo", cooldown);
        });

        player.setVelocity(new Vector(0, upwardVelocity, 0));

        long ticksToPeak = calculateTicksToPeak(upwardVelocity);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            // Launch downward
            player.setVelocity(new Vector(0, -downwardVelocity, 0));

            // NOW start checking for landing
            new BukkitRunnable() {

                int ticks = 0;

                @Override
                public void run() {

                    ticks++;

                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }

                    // Failsafe
                    if (ticks >= 60) {
                        cancel();
                        return;
                    }

                    if (!hasLanded(player)) {
                        return;
                    }

                    Location center = player.getLocation();

                    for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {

                        if (!(entity instanceof LivingEntity target)) {
                            continue;
                        }

                        if (entity.getLocation().distanceSquared(center) > radius * radius) {
                            continue;
                        }

                        target.damage(damage, player);

                        if (!target.isDead()) {
                            target.playHurtAnimation(0);
                        }

                        Vector direction = target.getLocation()
                                .toVector()
                                .subtract(center.toVector())
                                .normalize()
                                .multiply(knockback);

                        direction.setY(upwardKnockback);

                        target.setVelocity(direction);
                    }

                    cancel();
                }

            }.runTaskTimer(plugin, 1L, 1L);

        }, ticksToPeak);
    }

    private long calculateTicksToPeak(double upwardVelocity) {
        double velocity = upwardVelocity;
        long ticks = 0;

        while (velocity > 0) {
            velocity = (velocity - 0.08) * 0.98;
            ticks++;
        }

        return ticks;
    }

    private boolean hasLanded(Player player) {
        BoundingBox box = player.getBoundingBox();

        double y = box.getMinY() - 0.05;

        int minX = (int) Math.floor(box.getMinX());
        int maxX = (int) Math.floor(box.getMaxX());
        int minZ = (int) Math.floor(box.getMinZ());
        int maxZ = (int) Math.floor(box.getMaxZ());

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {

                Block block = player.getWorld().getBlockAt(
                        x,
                        (int) Math.floor(y),
                        z
                );

                if (block.getType().isSolid()) {
                    return true;
                }
            }
        }

        return false;
    }
}
