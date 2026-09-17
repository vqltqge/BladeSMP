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

        Player damager = (Player) event.getDamager();

        Player target = (Player) event.getEntity();

        if (frostAbilityTwo.getFreezePlayers().contains(target.getUniqueId())) {
            event.setDamage(event.getDamage() * frostAbilityTwo.getDamageMultiplier());
        }

        ItemStack hand = damager.getInventory().getItemInMainHand();

        if (!(bladeManager.isFrostBlade(hand))) {
            return;
        }

        passiveManager.freezePassive(target, damager);
    }
}
