package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StormAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final double radius;
    private final int strikeInterval;
    private final int regenAmplifier;
    private final int slownessAmplifier;
    private final int weaknessAmplifier;

    private static final TextColor STORM_COLOR = TextColor.fromHexString("#FFE44D");

    public StormAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("storm.ability-two.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("storm.ability-two.duration", 10) * 1000L;
        this.radius = plugin.getConfig().getDouble("storm.ability-two.radius", 10);
        this.strikeInterval = plugin.getConfig().getInt("storm.ability-two.strike-interval", 5);
        this.regenAmplifier = plugin.getConfig().getInt("storm.ability-two.regen-amplifier", 1);
        this.slownessAmplifier = plugin.getConfig().getInt("storm.ability-two.slowness-amplifier", 0);
        this.weaknessAmplifier = plugin.getConfig().getInt("storm.ability-two.weakness-amplifier", 0);
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isStormBlade(hand))) {
            return;
        }

        if (cooldownManager.isOnCooldown(id, "stormtwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "stormtwo") / 1000.0);
            player.sendMessage(Component.text("⚡ Thunderfield is on cooldown for " + seconds + "s").color(STORM_COLOR));
            return;
        }

        if (durationManager.isActive(id, "stormtwo")) {
            player.sendMessage(Component.text("⚡ Thunderfield is already active").color(STORM_COLOR));
            return;
        }

        durationManager.startDuration(id, "stormtwo", duration);

        player.sendMessage(Component.text("⚡ Thunderfield activated").color(STORM_COLOR));
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);

        // Cooldown begins when Thunderfield ends, so the action bar shows Active first.
        durationManager.runAfter(duration / 1000, () -> {
            cooldownManager.startCooldown(id, "stormtwo", cooldown);
            if (player.isOnline()) {
                player.sendMessage(Component.text("⚡ Thunderfield on cooldown").color(STORM_COLOR));
                player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
            }
        });

        List<Player> targets = new ArrayList<>();

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof Player target)) {
                continue;
            }

            if (entity.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }


            targets.add(target);
        }

        new BukkitRunnable() {

            int index = 0;

            @Override
            public void run() {

                // Stop when Thunderfield duration ends
                if (!durationManager.isActive(player.getUniqueId(), "stormtwo")) {
                    cancel();
                    return;
                }

                // Refresh the field so players who enter after activation can also be struck.
                targets.clear();
                for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    if (entity instanceof Player target
                            && target.isOnline()
                            && !target.isDead()
                            && target.getLocation().distanceSquared(player.getLocation()) <= radius * radius) {
                        targets.add(target);
                    }
                }

                if (targets.isEmpty()) {
                    index = 0;
                    return;
                }

                // Make sure index is still valid after removing players
                if (index >= targets.size()) {
                    index = 0;
                }

                Player target = targets.get(index);

                target.getWorld().strikeLightning(target.getLocation());

                index++;

                if (index >= targets.size()) {
                    index = 0;
                }
            }

        }.runTaskTimer(plugin, 0L, Math.max(1, strikeInterval));
    }
}
