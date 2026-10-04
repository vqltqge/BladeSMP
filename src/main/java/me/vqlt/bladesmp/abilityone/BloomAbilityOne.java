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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final int maxHearts;
    private final boolean healHearts;

    private static final TextColor BLOOM_COLOR = TextColor.fromHexString("#F50CAB");

    public BloomAbilityOne(BladeSMP plugin, BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;

        this.cooldown = plugin.getConfig().getLong("bloom.ability-one.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("bloom.ability-one.duration", 15) * 1000L;
        this.maxHearts = plugin.getConfig().getInt("bloom.ability-one.max-hearts", 30);
        this.healHearts = plugin.getConfig().getBoolean("bloom.ability-one.heal-hearts", true);

    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "bloomone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "bloomone") / 1000.0);
            player.sendMessage(Component.text("♥ Vital Surge is on cooldown for " + seconds + "s").color(BLOOM_COLOR));
            return;
        }

        if (durationManager.isActive(id, "bloomone")) {
            player.sendMessage(Component.text("♥ Vital Surge is already active").color(BLOOM_COLOR));
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isBloomBlade(hand))) {
            return;
        }

        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        double originalMaxHealth = maxHealth.getBaseValue();

        durationManager.startDuration(id, "bloomone", duration);

        player.sendMessage(Component.text("♥ Vital Surge activated").color(BLOOM_COLOR));
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);
        maxHealth.setBaseValue(maxHearts);
        if (healHearts) {
            player.setHealth(maxHearts);
        }

        durationManager.runAfter((duration / 1000), () -> revertHealth(player, maxHealth, originalMaxHealth, id));
    }

    public void revertHealth(Player player, AttributeInstance maxHealth, Double originalMaxHealth, UUID id) {
        maxHealth.setBaseValue(originalMaxHealth);

        if (player.getHealth() > originalMaxHealth) {
            player.setHealth(originalMaxHealth);
        }

        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        player.sendMessage(Component.text("♥ Vital Surge on cooldown").color(BLOOM_COLOR));
        cooldownManager.startCooldown(id, "bloomone", cooldown);


    }
}
