package me.vqlt.bladesmp.abilities;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PulseAbilityOne {
    private final CooldownManager cooldownManager;
    private final BladeManager bladeManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double dashVelocity;

    private static final TextColor PULSE_COLOR = TextColor.fromHexString("#A855F7");

    public PulseAbilityOne(BladeSMP plugin, CooldownManager cooldownManager, BladeManager bladeManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.bladeManager = bladeManager;

        this.cooldown = plugin.getConfig().getLong("pulse.ability-one.cooldown", 30) * 1000L;
        this.dashVelocity = plugin.getConfig().getDouble("pulse.ability-one.dash-velocity", 2.0);
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "pulseone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "pulseone") / 1000.0);
            player.sendMessage(Component.text("➤ Velocity is on cooldown for " + seconds + "s").color(PULSE_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isPulseBlade(hand))) {
            return;
        }

        player.sendMessage(Component.text("➤ Velocity activated").color(PULSE_COLOR));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.45f, 1.6f);
        player.playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 0.7f, 1.4f);
        player.setVelocity(player.getLocation().getDirection().multiply(dashVelocity));

        cooldownManager.startCooldown(id, "pulseone", cooldown);
    }
}
