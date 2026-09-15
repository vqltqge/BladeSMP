package me.vqlt.bladesmp.abilities;

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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final double damage;
    private final double knockback;
    private final long cooldown;
    private final double range;

    private static final TextColor FLAME_COLOR = TextColor.fromHexString("#FF4A1C");

    public FlameAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.damage = plugin.getConfig().getDouble("flame.ability-one.damage");
        this.knockback = plugin.getConfig().getDouble("flame.ability-one.knockback");
        this.cooldown = plugin.getConfig().getLong("flame.ability-one.cooldown") * 1000L;
        this.range = plugin.getConfig().getDouble("flame.ability-one.range");
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "flameone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "flameone") / 1000.0);
            player.sendMessage(Component.text("🔥 Flame Sweep is on cooldown for " + seconds + "s").color(FLAME_COLOR));
            return;
        }

        if (durationManager.isActive(id, "flameone")) {
            player.sendMessage(Component.text("🔥 Flame Sweep is already active").color(FLAME_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFlameBlade(hand))) {
            return;
        }

        player.sendMessage(Component.text("🔥 Flame Sweep activated").color(FLAME_COLOR));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        Vector direction = player.getLocation().getDirection().normalize();

        for (Entity entity : player.getNearbyEntities(range, range, range)) {

            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            Vector toTarget = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();

            double angle = Math.toDegrees(direction.angle(toTarget));

            if (angle > 60) {
                continue;
            }

            target.damage(damage, player);

            Vector knockbackVelocity = toTarget.multiply(knockback);
            knockbackVelocity.setY(0.4);

            target.setVelocity(knockbackVelocity);
        }

        cooldownManager.startCooldown(id, "flameone", cooldown);


    }
}
