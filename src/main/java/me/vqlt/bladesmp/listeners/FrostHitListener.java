package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.abilitytwo.FrostAbilityTwo;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class FrostHitListener implements Listener {

    private final BladeManager bladeManager;
    private final PassiveManager passiveManager;
    private final FrostAbilityTwo frostAbilityTwo;

    public FrostHitListener(BladeManager bladeManager, PassiveManager passiveManager, FrostAbilityTwo frostAbilityTwo) {
        this.bladeManager = bladeManager;
        this.passiveManager = passiveManager;
        this.frostAbilityTwo = frostAbilityTwo;
    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {

        // If the entity that dealt the damage is not a player, return
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        // If the entity that was damaged is not a player, return
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        // If the player who dealt the damage is not holding the frost blade, return
        ItemStack hand = attacker.getInventory().getItemInMainHand();

        if (!bladeManager.isFrostBlade(hand)) {
            return;
        }

        // If the victim is in the set of frozen players for frost ability 2, multiply the damage they took by the damage multiplier
        if (frostAbilityTwo.getFreezePlayers().contains(victim.getUniqueId())) {
            event.setDamage(event.getDamage() * frostAbilityTwo.getDamageMultiplier());
        }

        // Call the method from passive manager to freeze the player
        passiveManager.freezePassive(victim, attacker);
    }
}
