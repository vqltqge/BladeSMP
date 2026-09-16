package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ActionBarManager {
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeManager bladeManager;
    private final BladeSMP plugin;

    private static final TextColor FLAME_COLOR = TextColor.fromHexString("#FF7A2F");
    private static final TextColor READY_COLOR = TextColor.fromHexString("#00ff00");
    private static final TextColor ACTIVE_COLOR = TextColor.fromHexString("#ff0000");
    private static final TextColor COOLDOWN_COLOR = TextColor.color(NamedTextColor.GRAY);


    public ActionBarManager(CooldownManager cooldownManager, DurationManager durationManager, BladeManager bladeManager, BladeSMP plugin) {
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.bladeManager = bladeManager;
        this.plugin = plugin;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID id = player.getUniqueId();
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    if (bladeManager.isBloomBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "bloomone");
                        boolean oneActive = durationManager.isActive(id, "bloomone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "bloomtwo");
                        boolean twoActive = durationManager.isActive(id, "bloomtwo");

                        if (oneActive) {
                            if (twoActive) {
                                // active - active

                            } else if (twoCooldown) {
                                // active - cooldown
                            } else {
                                // active - ready
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                            } else if (twoCooldown) {
                                // cooldown - cooldown
                            } else {
                                // cooldown - ready
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                            } else if (twoCooldown) {
                                // ready - cooldown
                            } else {
                                // ready - ready
                            }
                        }
                    }
                    if (bladeManager.isFlameBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "flameone");
                        boolean oneActive = durationManager.isActive(id, "flameone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "flametwo");
                        boolean twoActive = durationManager.isActive(id, "flametwo");

                        if (oneActive) {
                            // IMPORTANT CHANGE LATER
                            if (twoActive) {
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text("Active").color(ACTIVE_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text("Active").color(ACTIVE_COLOR).decorate(TextDecoration.BOLD)));


                            } else if (twoCooldown) {
                                // active - cooldown
                                player.sendActionBar(Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                        .append(Component.text("Active").color(ACTIVE_COLOR).decorate(TextDecoration.BOLD))
                                        .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                        .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                        .append(Component.text(String.format("%.1fs", cooldownManager.getRemainingMillis(id, "flametwo") / 1000.0)).color(COOLDOWN_COLOR).decorate(TextDecoration.BOLD))
                                );

                            } else {
                                // active - ready
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text("Active").color(ACTIVE_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text("Ready").color(READY_COLOR).decorate(TextDecoration.BOLD)));
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text(String.format("%.1fs", cooldownManager.getRemainingMillis(id, "flameone") / 1000.0)).color(COOLDOWN_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text("Active").color(ACTIVE_COLOR).decorate(TextDecoration.BOLD)));

                            } else if (twoCooldown) {
                                // cooldown - cooldown
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text(String.format("%.1fs", cooldownManager.getRemainingMillis(id, "flameone") / 1000.0)).color(COOLDOWN_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text(String.format("%.1fs", cooldownManager.getRemainingMillis(id, "flametwo") / 1000.0)).color(COOLDOWN_COLOR).decorate(TextDecoration.BOLD)));
                            } else {
                                // cooldown - ready
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text(String.format("%.1fs", cooldownManager.getRemainingMillis(id, "flameone") / 1000.0)).color(COOLDOWN_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text("Ready").color(READY_COLOR).decorate(TextDecoration.BOLD)));
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text("Ready").color(READY_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text("Active").color(ACTIVE_COLOR).decorate(TextDecoration.BOLD)));
                            } else if (twoCooldown) {
                                // ready - cooldown
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text("Ready").color(READY_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text(String.format("%.1fs", cooldownManager.getRemainingMillis(id, "flametwo") / 1000.0)).color(COOLDOWN_COLOR).decorate(TextDecoration.BOLD)));
                            } else {
                                // ready - ready
                                player.sendActionBar(
                                        Component.text("🔥 Flame Sweep ").color(FLAME_COLOR)
                                                .append(Component.text("Ready").color(READY_COLOR).decorate(TextDecoration.BOLD))
                                                .append(Component.text("  |  ").color(NamedTextColor.DARK_GRAY))
                                                .append(Component.text("🔥 Inferno ").color(FLAME_COLOR))
                                                .append(Component.text("Ready").color(READY_COLOR).decorate(TextDecoration.BOLD)));
                            }
                        }
                    }
                    if (bladeManager.isStormBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "stormone");
                        boolean oneActive = durationManager.isActive(id, "stormone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "stormtwo");
                        boolean twoActive = durationManager.isActive(id, "stormtwo");

                        if (oneActive) {
                            if (twoActive) {
                                // active - active
                            } else if (twoCooldown) {
                                // active - cooldown
                            } else {
                                // active - ready
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                            } else if (twoCooldown) {
                                // cooldown - cooldown
                            } else {
                                // cooldown - ready
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                            } else if (twoCooldown) {
                                // ready - cooldown
                            } else {
                                // ready - ready
                            }
                        }
                    }
                    if (bladeManager.isTidalBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "tidalone");
                        boolean oneActive = durationManager.isActive(id, "tidalone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "tidaltwo");
                        boolean twoActive = durationManager.isActive(id, "tidaltwo");

                        if (oneActive) {
                            if (twoActive) {
                                // active - active
                            } else if (twoCooldown) {
                                // active - cooldown
                            } else {
                                // active - ready
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                            } else if (twoCooldown) {
                                // cooldown - cooldown
                            } else {
                                // cooldown - ready
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                            } else if (twoCooldown) {
                                // ready - cooldown
                            } else {
                                // ready - ready
                            }
                        }
                    }
                    if (bladeManager.isFortuneBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "fortuneone");
                        boolean oneActive = durationManager.isActive(id, "fortuneone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "fortunetwo");
                        boolean twoActive = durationManager.isActive(id, "fortunetwo");

                        if (oneActive) {
                            if (twoActive) {
                                // active - active
                            } else if (twoCooldown) {
                                // active - cooldown
                            } else {
                                // active - ready
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                            } else if (twoCooldown) {
                                // cooldown - cooldown
                            } else {
                                // cooldown - ready
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                            } else if (twoCooldown) {
                                // ready - cooldown
                            } else {
                                // ready - ready
                            }
                        }
                    }
                    if (bladeManager.isFrostBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "frostone");
                        boolean oneActive = durationManager.isActive(id, "frostone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "frosttwo");
                        boolean twoActive = durationManager.isActive(id, "frosttwo");

                        if (oneActive) {
                            if (twoActive) {
                                // active - active
                            } else if (twoCooldown) {
                                // active - cooldown
                            } else {
                                // active - ready
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                            } else if (twoCooldown) {
                                // cooldown - cooldown
                            } else {
                                // cooldown - ready
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                            } else if (twoCooldown) {
                                // ready - cooldown
                            } else {
                                // ready - ready
                            }
                        }
                    }
                    if (bladeManager.isPulseBlade(hand)) {
                        boolean oneCooldown = cooldownManager.isOnCooldown(id, "pulseone");
                        boolean oneActive = durationManager.isActive(id, "pulseone");

                        boolean twoCooldown = cooldownManager.isOnCooldown(id, "pulsetwo");
                        boolean twoActive = durationManager.isActive(id, "pulsetwo");

                        if (oneActive) {
                            if (twoActive) {
                                // active - active
                            } else if (twoCooldown) {
                                // active - cooldown
                            } else {
                                // active - ready
                            }
                        } else if (oneCooldown) {
                            if (twoActive) {
                                // cooldown - active
                            } else if (twoCooldown) {
                                // cooldown - cooldown
                            } else {
                                // cooldown - ready
                            }
                        } else {
                            if (twoActive) {
                                // ready - active
                            } else if (twoCooldown) {
                                // ready - cooldown
                            } else {
                                // ready - ready
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
