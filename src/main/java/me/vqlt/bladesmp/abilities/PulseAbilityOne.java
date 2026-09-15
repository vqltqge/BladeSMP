package me.vqlt.bladesmp.abilities;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PulseAbilityOne {
    private final CooldownManager cooldownManager;
    private final BladeManager bladeManager;

    private static final long COOLDOWN = 15000;

    public PulseAbilityOne(CooldownManager cooldownManager, BladeManager bladeManager) {
        this.cooldownManager = cooldownManager;
        this.bladeManager = bladeManager;
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "pulseone")) {
            player.sendMessage("Ability One is on cooldown");
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isPulseBlade(hand))) {
            return;
        }

        player.setVelocity(player.getLocation().getDirection().multiply(2));

        cooldownManager.startCooldown(id, "pulseone", COOLDOWN);
    }
}
