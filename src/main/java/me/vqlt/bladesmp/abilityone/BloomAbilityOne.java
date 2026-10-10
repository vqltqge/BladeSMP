package me.vqlt.bladesmp.abilityone;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BloomAbilityOne {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // Variables for the config values
    private final long cooldown;
    private final long duration;
    private final int maxHearts;
    private final boolean healHearts;

    // Variable for the colour for the messages
    private static final TextColor BLOOM_COLOR = TextColor.fromHexString("#F50CAB");

    public BloomAbilityOne(BladeSMP plugin, BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("bloom.ability-one.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("bloom.ability-one.duration", 15) * 1000L;
        this.maxHearts = plugin.getConfig().getInt("bloom.ability-one.max-hearts", 30);
        this.healHearts = plugin.getConfig().getBoolean("bloom.ability-one.heal-hearts", true);

    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "bloomone")) {
            // Get the milliseconds remaining from config, divide by 1000 to turn it into seconds, and round it up to the nearest whole number
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "bloomone") / 1000.0);
            player.sendMessage(Component.text("♥ Vital Surge is on cooldown for " + seconds + "s").color(BLOOM_COLOR));
            return;
        }

        if (durationManager.isActive(id, "bloomone")) {
            player.sendMessage(Component.text("♥ Vital Surge is already active").color(BLOOM_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the bloom blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isBloomBlade(hand))) {
            return;
        }

        // Gets the players maximum health and check if it exists. If so, store the player's current maximum health
        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        double originalMaxHealth = maxHealth.getBaseValue();

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        durationManager.startDuration(id, "bloomone", duration);
        player.sendMessage(Component.text("♥ Vital Surge activated").color(BLOOM_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);

        // Set the players maximum hearts to the config value, and if it is enabled in the config, heal the player up to those hearts
        maxHealth.setBaseValue(maxHearts);
        if (healHearts) {
            player.setHealth(maxHearts);
        }

        // Schedule the revert health method at the time of the end of the duration
        durationManager.runAfter((duration / 1000), () -> revertHealth(player, maxHealth, originalMaxHealth, id));
    }

    public void revertHealth(Player player, AttributeInstance maxHealth, Double originalMaxHealth, UUID id) {
        // Reset the player's max health to the original max health
        maxHealth.setBaseValue(originalMaxHealth);

        // Set the players health to the original maximum health if they have more health than the original max health
        if (player.getHealth() > originalMaxHealth) {
            player.setHealth(originalMaxHealth);
        }

        // Start the abilities cooldown and tell the player the ability is on cooldown. Play a deactivation sound at the players location
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        player.sendMessage(Component.text("♥ Vital Surge on cooldown").color(BLOOM_COLOR));
        cooldownManager.startCooldown(id, "bloomone", cooldown);


    }
}
