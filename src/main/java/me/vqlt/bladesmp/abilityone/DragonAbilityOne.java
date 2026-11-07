package me.vqlt.bladesmp.abilityone;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DragonAbilityOne {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final BladeSMP plugin;

    private final long cooldown;

    private static final TextColor DRAGON_COLOR = TextColor.fromHexString("#5A189A");

    public DragonAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("dragon.ability-one.cooldown", 60) * 1000L;
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "dragonone")) {
            // Get the milliseconds remaining from config, divide by 1000 to turn it into seconds, and round it up to the nearest whole number
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "dragonone") / 1000.0);
            player.sendMessage(Component.text("☄ Dragon's Wrath is on cooldown for " + seconds + "s").color(DRAGON_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the flame blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!bladeManager.isDragonBlade(hand)) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        player.sendMessage(Component.text("☄ Dragon's Wrath activated").color(DRAGON_COLOR));

//        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.85f);
//        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.55f, 1.35f);
//        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 0.45f, 0.8f);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getEyeLocation().add(player.getLocation().getDirection().multiply(2)), 1);

        
    }
}
