package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TidalAbilityTwo {

    private static final TextColor TIDAL_COLOR = TextColor.fromHexString("#20BFFF");

    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double radius;
    private final double pullVelocity;
    private final double knockback;
    private final double damage;

    public TidalAbilityTwo(
            BladeManager bladeManager,
            CooldownManager cooldownManager,
            DurationManager durationManager,
            BladeSMP plugin
    ) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong(
                "tidal.ability-two.cooldown", 60
        ) * 1000L;

        this.radius = plugin.getConfig().getDouble(
                "tidal.ability-two.radius", 8
        );

        this.knockback = plugin.getConfig().getDouble(
                "tidal.ability-two.knockback", 2
        );

        this.damage = plugin.getConfig().getDouble(
                "tidal.ability-two.damage", 8
        );

        this.pullVelocity = plugin.getConfig().getDouble(
                "tidal.ability-two.pull-velocity", 1
        );
    }

    public void activate(Player player) {

        UUID id = player.getUniqueId();

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!bladeManager.isTidalBlade(hand)) {
            return;
        }

        // Check cooldown
        if (cooldownManager.isOnCooldown(id, "tidaltwo")) {
            double remaining = cooldownManager.getRemainingMillis(id, "tidaltwo") / 1000.0;

            player.sendMessage(Component.text("≋ Riptide is on cooldown for " + String.format("%.1f", remaining) + "s").color(TIDAL_COLOR));

            return;
        }

        cooldownManager.startCooldown(id, "tidaltwo", cooldown);
        player.sendMessage(Component.text("≋ Riptide activated").color(TIDAL_COLOR));

        // Entities that have been caught in the riptide
        Set<LivingEntity> caughtEntities = new HashSet<>();

        player.playSound(
                player.getLocation(),
                Sound.ITEM_TRIDENT_RIPTIDE_3,
                1.0f,
                0.8f
        );

        new BukkitRunnable() {

            int ticks = 0;

            @Override
            public void run() {

                // Pull for 30 ticks = 1.5 seconds
                if (ticks >= 30) {

                    launchEntities();
                    cancel();
                    return;
                }

                for (Entity entity : player.getNearbyEntities(
                        radius,
                        radius,
                        radius
                )) {

                    if (!(entity instanceof LivingEntity target)) {
                        continue;
                    }

                    if (target.equals(player)) {
                        continue;
                    }

                    // Direction FROM target TO player
                    Vector direction = player.getLocation()
                            .toVector()
                            .subtract(target.getLocation().toVector());

                    double distance = direction.length();

                    if (distance == 0) {
                        continue;
                    }

                    direction.normalize();

                    /*
                     * Reduce the pull when they get very close.
                     * This stops them violently flying through the player.
                     */
                    double strength = Math.min(
                            pullVelocity,
                            distance * 0.25
                    );

                    direction.multiply(strength);

                    // Small upward movement makes the pull smoother
                    direction.setY(direction.getY() + 0.1);

                    target.setVelocity(direction);

                    caughtEntities.add(target);
                }

                ticks++;
            }

            private void launchEntities() {

                player.playSound(
                        player.getLocation(),
                        Sound.ITEM_TRIDENT_RIPTIDE_3,
                        1.2f,
                        1.2f
                );

                for (LivingEntity target : caughtEntities) {

                    if (!target.isValid() || target.isDead()) {
                        continue;
                    }

                    /*
                     * Direction FROM player TO target,
                     * so this launches them away.
                     */
                    Vector direction = target.getLocation()
                            .toVector()
                            .subtract(player.getLocation().toVector());

                    if (direction.lengthSquared() == 0) {
                        direction = player.getLocation()
                                .getDirection()
                                .clone();
                    }

                    direction.normalize();
                    direction.multiply(knockback);

                    // Add some upward launch
                    direction.setY(0.5);

                    target.setVelocity(direction);
                    target.damage(damage, player);
                }
            }

        }.runTaskTimer(plugin, 0L, 1L);
    }
}