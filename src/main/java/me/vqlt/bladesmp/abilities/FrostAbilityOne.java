package me.vqlt.bladesmp.abilities;

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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final Set<UUID> freezePlayers = new HashSet<>();

    public Set<UUID> getFreezePlayers() {
        return freezePlayers;
    }

    private final long cooldown;
    private final double dashVelocity;
    private final long freezeDuration;
    private final double damage;

    private static final TextColor FROST_COLOR = TextColor.fromHexString("#6EE7FF");

    public FrostAbilityOne(BladeSMP plugin, DurationManager durationManager, CooldownManager cooldownManager, BladeManager bladeManager) {
        this.plugin = plugin;
        this.durationManager = durationManager;
        this.cooldownManager = cooldownManager;
        this.bladeManager = bladeManager;

        this.cooldown = plugin.getConfig().getLong("frost.ability-one.cooldown", 45) * 1000L;
        this.freezeDuration = plugin.getConfig().getLong("frost.ability-one.freeze-duration", 3) * 20L;
        this.dashVelocity = plugin.getConfig().getDouble("frost.ability-one.dash-velocity", 2);
        this.damage = plugin.getConfig().getDouble("frost.ability-one.damage", 16.0);

    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "frostone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "frostone") / 1000.0);
            player.sendMessage(Component.text("✻ Frozen Dash is on cooldown for " + seconds + "s").color(FROST_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFrostBlade(hand))) {
            return;
        }

        player.sendMessage(Component.text("✻ Frozen Dash activated").color(FROST_COLOR));
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.25f, 1.8f);
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 0.55f, 1.5f);

        player.setVelocity(player.getLocation().getDirection().multiply(dashVelocity));

        Set<UUID> hitPlayers = new HashSet<>();

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 20) {
                    cancel();
                    return;
                }

                for (Player other : player.getWorld().getPlayers()) {
                    if (other.equals(player)) {
                        continue;
                    }

                    if (hitPlayers.contains(other.getUniqueId())) {
                        continue;
                    }

                    if (player.getBoundingBox().overlaps(other.getBoundingBox())) {
                        hitPlayers.add(other.getUniqueId());
                        freezePlayers.add(other.getUniqueId());

                        other.damage(damage);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                freezePlayers.remove(other.getUniqueId());
                            }
                        }.runTaskLater(plugin, freezeDuration);

                    }
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);

        // No need to say it is on cooldown as the ability is instant
        cooldownManager.startCooldown(id, "frostone", cooldown);
    }
}
