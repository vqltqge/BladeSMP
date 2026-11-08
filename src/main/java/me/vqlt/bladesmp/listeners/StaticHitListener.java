package me.vqlt.bladesmp.listeners;


import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.DurationManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import me.vqlt.bladesmp.managers.TrustManager;
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
    private final BladeSMP plugin;
    private final DurationManager durationManager;
    private final TrustManager trustManager;

    public StaticHitListener(BladeManager bladeManager, PassiveManager passiveManager, BladeSMP plugin, DurationManager durationManager, TrustManager trustManager) {
        this.bladeManager = bladeManager;
        this.passiveManager = passiveManager;
        this.plugin = plugin;
        this.durationManager = durationManager;
        this.trustManager = trustManager;
    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {

        if (!plugin.getConfig().getBoolean("storm.passive.enabled", true)) {
            return;
        }

        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }


        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }


        Location location = event.getEntity().getLocation();
        World world = event.getEntity().getWorld();


        ItemStack hand = damager.getInventory().getItemInMainHand();


        if (!(bladeManager.isStormBlade(hand))) {
            return;
        }

        if (durationManager.isActive(damager.getUniqueId(), "stormone") || durationManager.isActive(damager.getUniqueId(), "stormtwo")) {
            return;
        }

        if (trustManager.isTrusted(damager.getUniqueId(), victim.getUniqueId())) {
            return;
        }

        passiveManager.staticPassive(damager, location, world);
    }
}
