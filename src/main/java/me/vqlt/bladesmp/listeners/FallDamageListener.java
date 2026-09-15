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
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isPulseBlade(hand))) {
            return;
        }

        event.setCancelled(true);
    }
}
