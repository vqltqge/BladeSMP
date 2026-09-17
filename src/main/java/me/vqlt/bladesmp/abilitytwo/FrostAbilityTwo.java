package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FrostAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final Set<UUID> freezePlayers = new HashSet<>();

    public Set<UUID> getFreezePlayers() {
        return freezePlayers;
    }

    private final long cooldown;
    private final long duration;
    private final double radius;
    private final double damageMultiplier;

    private static final TextColor FROST_COLOR = TextColor.fromHexString("#6EE7FF");

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public FrostAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("frost.ability-two.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("frost.ability-two.duration", 5);
        this.radius = plugin.getConfig().getDouble("frost.ability-two.radius", 3);
        this.damageMultiplier = plugin.getConfig().getDouble("frost.ability-two.damage-multiplier", 2);
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "frosttwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "frosttwo") / 1000.0);
            player.sendMessage(Component.text("✻ Frozen Domain is on cooldown for " + seconds + "s").color(FROST_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFrostBlade(hand))) {
            return;
        }

        player.sendMessage(Component.text("✻ Frozen Domain activated").color(FROST_COLOR));
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1, 0.7f);
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.65f, 0.7f);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof Player target)) {
                continue;
            }

            if (target.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }

            UUID targetid = target.getUniqueId();

            freezePlayers.add(targetid);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                freezePlayers.remove(targetid);
            }, duration * 20L);
        }

    }
}
