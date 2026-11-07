package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DragonAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private static final TextColor DRAGON_COLOR = TextColor.fromHexString("#5A189A");

    private final long cooldown;
    private final long duration;
    private final long chargeDuration;

    // A set that stores frozen players
    public final Set<UUID> activePlayers = new HashSet<>();
    public final Set<UUID> armedPlayers = new HashSet<>();

    public DragonAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.plugin = plugin;
        this.durationManager = durationManager;

        this.cooldown = plugin.getConfig().getLong("dragon.ability-two.cooldown", 120) * 1000L;
        this.duration = plugin.getConfig().getLong("dragon.ability-two.duration", 10) * 1000L;
        this.chargeDuration = plugin.getConfig().getLong("dragon.ability-two.charge-duration", 10) * 1000L;
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        // Get the item the player is holding and return if it is not the flame blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!bladeManager.isDragonBlade(hand)) {
            return;
        }

        if (armedPlayers.contains(id)) {
            player.sendMessage(Component.text("☄ Dragon's Fury is already charged").color(DRAGON_COLOR));
            return;
        }

        if (cooldownManager.isOnCooldown(id, "dragontwo")) {
            // Get the milliseconds remaining from config, divide by 1000 to turn it into seconds, and round it up to the nearest whole number
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "dragontwo") / 1000.0);
            player.sendMessage(Component.text("☄ Dragon's Fury is on cooldown for " + seconds + "s").color(DRAGON_COLOR));
            return;
        }

        if (durationManager.isActive(id, "dragontwo")) {
            player.sendMessage(Component.text("☄ Dragon's Fury is already active").color(DRAGON_COLOR));
            return;
        }

        // Adds the players to the armed players set and sends the player a message saying their ability is charged. Play an activation sound at the players location
        player.sendMessage(Component.text("☄ Dragon's Fury charged").color(DRAGON_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.5F, 1.5F);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.25F, 2);
        armedPlayers.add(id);

        // Removes the players from the armed players set after the charge duration
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (armedPlayers.remove(id)) {
                player.sendMessage(Component.text("☄ Dragon's Fury charge expired").color(DRAGON_COLOR));
                player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.5F, 0.8F);
            }
        }, chargeDuration / 50);
    }

    public void startCooldown(UUID id, Player player) {
        // Start the ability cooldown, play a sound and send the player a message
        activePlayers.remove(id);
        cooldownManager.startCooldown(id, "dragontwo", cooldown);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        player.sendMessage(Component.text("☄ Dragon's Fury on cooldown").color(DRAGON_COLOR));
    }

    public void removeCharge(Player attacker, UUID id) {
        attacker.sendMessage(Component.text("☄ Dragon's Fury activated").color(DRAGON_COLOR));
        durationManager.startDuration(id, "dragontwo", duration);
        durationManager.runAfter(duration / 1000, () -> startCooldown(id, attacker));
        armedPlayers.remove(id);
        activePlayers.add(id);
    }
}
