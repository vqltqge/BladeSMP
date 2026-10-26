package me.vqlt.bladesmp.listeners;


import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FallDamageListener implements Listener {

    private final BladeManager bladeManager;

    public FallDamageListener(BladeManager bladeManager) {
        this.bladeManager = bladeManager;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        // If the entity that was damaged is not a player, return
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        // If the cause of the damage was not fall damage, return
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        // If the player is not holding the pulse blade, return
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isPulseBlade(hand))) {
            return;
        }

        // Cancel the fall damage
        event.setCancelled(true);
    }
}
