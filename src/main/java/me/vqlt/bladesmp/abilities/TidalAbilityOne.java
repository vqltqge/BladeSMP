package me.vqlt.bladesmp.abilities;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TidalAbilityOne {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final int resistanceAmplifier;
    private final double radius;

    private static final TextColor TIDAL_COLOR = TextColor.fromHexString("#20BFFF");

    public TidalAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("tidal.ability-one.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("tidal.ability-one.duration", 10) * 1000L;
        this.resistanceAmplifier = plugin.getConfig().getInt("tidal.ability-one.resistance-amplifier", 2) - 1;
        this.radius = plugin.getConfig().getDouble("tidal.ability-one.radius", 3);
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "tidalone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "tidalone") / 1000.0);
            player.sendMessage(Component.text("≈ Drowning Field is on cooldown for " + seconds + "s").color(TIDAL_COLOR));
            return;
        }

        if (durationManager.isActive(id, "tidalone")) {
            player.sendMessage(Component.text("≈ Drowning Field is already active").color(TIDAL_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isTidalBlade(hand))) {
            return;
        }

        player.sendMessage(Component.text("≈ Drowning Field activated").color(TIDAL_COLOR));
        durationManager.startDuration(id, "tidalone", duration);
        durationManager.runAfter(duration / 1000, () -> cooldownManager.startCooldown(id, "tidalone", cooldown));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, ((int) duration / 1000) * 20, resistanceAmplifier));
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 0.7f, 0.7f);
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.45f, 1.3f);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {

            if (!(entity instanceof Player target)) {
                continue;
            }

            new BukkitRunnable() {
                int ticksleft = ((int) duration / 1000) * 20;

                @Override
                public void run() {
                    if (ticksleft <= 0 || !target.isOnline()) {
                        cancel();
                        return;
                    }

                    target.setRemainingAir(0);

                    if (ticksleft % 20 == 0) {
                        target.damage(2.0, player);
                        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 1, 1);
                    }

                    ticksleft--;
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }
}
