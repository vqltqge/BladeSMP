package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DragonAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final BladeSMP plugin;

    private static final TextColor DRAGON_COLOR = TextColor.fromHexString("#5A189A");

    private final long cooldown;
    private final long duration;

    // A set that stores frozen players
    public final Set<UUID> activePlayers = new HashSet<>();

    public DragonAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("dragon.ability-two.cooldown", 120) * 1000L;
        this.duration = plugin.getConfig().getLong("dragon.ability-two.duration", 10) * 1000L;
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "dragontwo")) {
            // Get the milliseconds remaining from config, divide by 1000 to turn it into seconds, and round it up to the nearest whole number
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "dragontwo") / 1000.0);
            player.sendMessage(Component.text("☄ Dragon's Fury is on cooldown for " + seconds + "s").color(DRAGON_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the flame blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!bladeManager.isDragonBlade(hand)) {
            return;
        }

        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 0.8f);

        activePlayers.add(player.getUniqueId());


    }
}
