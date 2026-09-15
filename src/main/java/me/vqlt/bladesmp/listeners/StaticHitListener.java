package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class StaticHitListener implements Listener {

    private final BladeManager bladeManager;
    private final PassiveManager passiveManager;

    public StaticHitListener(BladeManager bladeManager, PassiveManager passiveManager) {
        this.bladeManager = bladeManager;
        this.passiveManager = passiveManager;
    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Location location = event.getEntity().getLocation();
        World world = event.getEntity().getWorld();

        ItemStack hand = damager.getInventory().getItemInMainHand();

        if (!(bladeManager.isStormBlade(hand))) {
            return;
        }

        passiveManager.staticPassive(damager, location, world);
    }
}
