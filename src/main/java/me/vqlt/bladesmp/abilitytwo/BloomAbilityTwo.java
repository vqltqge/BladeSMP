package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BloomAbilityTwo {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // Variables for the config values
    private final double radius;
    private final long cooldown;
    private final long duration;
    private final int minHP;

    // Variable for the colour for the messages
    private static final TextColor BLOOM_COLOR = TextColor.fromHexString("#F50CAB");

    public BloomAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.radius = plugin.getConfig().getDouble("bloom.ability-two.radius", 3.0);
        this.cooldown = plugin.getConfig().getLong("bloom.ability-two.cooldown", 100) * 1000L;
        this.duration = plugin.getConfig().getLong("bloom.ability-two.duration", 10) * 1000L;
        this.minHP = plugin.getConfig().getInt("bloom.ability-two.min-hp", 8);
    }

    public void activate(Player player) {
        // Get the item the player is holding and return if it is not the bloom blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isBloomBlade(hand))) {
            return;
        }

        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "bloomtwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "bloomtwo") / 1000.0);
            player.sendMessage(Component.text("♥ Lifebind is on cooldown for " + seconds + "s").color(BLOOM_COLOR));
            return;
        }

        if (durationManager.isActive(id, "bloomtwo")) {
            player.sendMessage(Component.text("♥ Lifebind is already active").color(BLOOM_COLOR));
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location

        durationManager.startDuration(id, "bloomtwo", duration);
        player.sendMessage(Component.text("♥ Lifebind activated").color(BLOOM_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);

        // Loop through all entities in the configured radius of the player
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            // If the entity is not a living entity, ignore it. For example an arrow
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            // If the entity is not in a configured radius circle of the player, ignore it
            if (entity.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }

            // Get the target's maximum health, and ignore the entity if it doesn't exist
            AttributeInstance maxHealth = target.getAttribute(Attribute.MAX_HEALTH);

            if (maxHealth == null) {
                continue;
            }

            // Store the original max health of the player
            double originalMaxHealth = maxHealth.getBaseValue();

            // Get the current health of the player
            double currentHealth = target.getHealth();

            // Defines the new maximum health of the player to their current health, if that is less than the configured minimum hp, set it to the config minimum hp
            double newMaxHealth = Math.max(currentHealth, minHP);
            // If the current health is higher than the original max health, set it to the original
            newMaxHealth = Math.min(newMaxHealth, originalMaxHealth);

            // Set the new maximum health of the player
            maxHealth.setBaseValue(newMaxHealth);

            // After the duration, revert the health
            durationManager.runAfter((duration / 1000), () ->
                    revertHealth(target, maxHealth, originalMaxHealth));
        }

        // Schedule the following: remove the player from the conservation players set, and plays the deactivation sound, tell them their ability is on cooldown and start their cooldown
        durationManager.runAfter((duration / 1000), () -> {
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);

            player.sendMessage(Component.text("♥ Lifebind on cooldown").color(BLOOM_COLOR));

            cooldownManager.startCooldown(id, "bloomtwo", cooldown);
        });
    }

    public void revertHealth(
            LivingEntity target,
            AttributeInstance maxHealth,
            Double originalMaxHealth
    ) {
        // Set their new maximum health to the original
        maxHealth.setBaseValue(originalMaxHealth);
    }
}