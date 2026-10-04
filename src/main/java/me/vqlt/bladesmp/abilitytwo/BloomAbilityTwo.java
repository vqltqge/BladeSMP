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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final double radius;
    private final long cooldown;
    private final long duration;
    private final int minHP;

    private static final TextColor BLOOM_COLOR = TextColor.fromHexString("#F50CAB");

    public BloomAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.radius = plugin.getConfig().getDouble("bloom.ability-two.radius", 3.0);
        this.cooldown = plugin.getConfig().getLong("bloom.ability-two.cooldown", 100) * 1000L;
        this.duration = plugin.getConfig().getLong("bloom.ability-two.duration", 10) * 1000L;
        this.minHP = plugin.getConfig().getInt("bloom.ability-two.min-hp", 8);
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isBloomBlade(hand))) {
            return;
        }

        if (cooldownManager.isOnCooldown(id, "bloomtwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "bloomtwo") / 1000.0);
            player.sendMessage(Component.text("♥ Lifebind is on cooldown for " + seconds + "s").color(BLOOM_COLOR));
            return;
        }

        if (durationManager.isActive(id, "bloomtwo")) {
            player.sendMessage(Component.text("♥ Lifebind is already active").color(BLOOM_COLOR));
            return;
        }

        durationManager.startDuration(id, "bloomtwo", duration);

        player.sendMessage(Component.text("♥ Lifebind activated").color(BLOOM_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof LivingEntity target)) {
                continue;
            }

            if (entity.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }

            AttributeInstance maxHealth = target.getAttribute(Attribute.MAX_HEALTH);

            if (maxHealth == null) {
                continue;
            }

            double originalMaxHealth = maxHealth.getBaseValue();

            double currentHealth = target.getHealth();

            double newMaxHealth = Math.max(currentHealth, minHP);
            newMaxHealth = Math.min(newMaxHealth, originalMaxHealth);

            maxHealth.setBaseValue(newMaxHealth);

            durationManager.runAfter((duration / 1000), () ->
                    revertHealth(target, maxHealth, originalMaxHealth));
        }

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
        maxHealth.setBaseValue(originalMaxHealth);
    }
}