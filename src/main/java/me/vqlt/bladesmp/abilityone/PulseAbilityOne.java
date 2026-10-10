package me.vqlt.bladesmp.abilityone;

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
    // Variables for the classes with the methods needed for the blades abilities
    private final CooldownManager cooldownManager;
    private final BladeManager bladeManager;
    private final BladeSMP plugin;

    // Variables for the config values
    private final long cooldown;
    private final double dashVelocity;

    // Variable for the colour for the messages
    private static final TextColor PULSE_COLOR = TextColor.fromHexString("#A855F7");

    public PulseAbilityOne(BladeSMP plugin, CooldownManager cooldownManager, BladeManager bladeManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.bladeManager = bladeManager;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("pulse.ability-one.cooldown", 30) * 1000L;
        this.dashVelocity = plugin.getConfig().getDouble("pulse.ability-one.dash-velocity", 2.0);
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "pulseone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "pulseone") / 1000.0);
            player.sendMessage(Component.text("➤ Velocity is on cooldown for " + seconds + "s").color(PULSE_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the pulse blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isPulseBlade(hand))) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.sendMessage(Component.text("➤ Velocity activated").color(PULSE_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.45f, 1.6f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 0.7f, 1.4f);

        // Set players velocity to the config value in the direction they are looking
        player.setVelocity(player.getLocation().getDirection().multiply(dashVelocity));

        // No need to say it is on cooldown as the ability is instant
        cooldownManager.startCooldown(id, "pulseone", cooldown);
    }
}
