package me.vqlt.bladesmp.abilities;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PulseAbilityOne {
    private final CooldownManager cooldownManager;
    private final BladeManager bladeManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double dashVelocity;

    public PulseAbilityOne(BladeSMP plugin, CooldownManager cooldownManager, BladeManager bladeManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.bladeManager = bladeManager;

        this.cooldown = plugin.getConfig().getLong("pulse.ability-one.cooldown", 30) * 1000L;
        this.dashVelocity = plugin.getConfig().getLong("pulse.ability-one.dash-velocity", 2);
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

        player.setVelocity(player.getLocation().getDirection().multiply(dashVelocity));

        cooldownManager.startCooldown(id, "pulseone", cooldown);
    }
}
