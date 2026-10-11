package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class BloomHitListener implements Listener {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeSMP plugin;
    private final BladeManager bladeManager;
    private final PassiveManager passiveManager;

    // Hashmap to store the players UUID and the number of hits
    private final HashMap<UUID, Integer> bloomHits = new HashMap<UUID, Integer>();

    // Variables for the config values
    private final int activateHits;

    public BloomHitListener(BladeSMP plugin, BladeManager bladeManager, PassiveManager passiveManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
        this.passiveManager = passiveManager;

        // Fetches the config values and stores them in variables
        this.activateHits = plugin.getConfig().getInt("bloom.passive.hits-to-activate");
    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {

        // Returns if the entity who dealt the damage is not a player or the entity that was damaged is not a player
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        // Get the player who dealt the damage and checks if they are holding the bloom blade
        Player damager = (Player) event.getDamager();

        ItemStack hand = damager.getInventory().getItemInMainHand();

        if (!(bladeManager.isBloomBlade(hand))) {
            return;
        }

        // Gets the damagers maximum health, and returns if it does not exist
        AttributeInstance maxHealthAttribute = damager.getAttribute(Attribute.MAX_HEALTH);

        if (maxHealthAttribute == null) {
            return;
        }

        double maxHealth = maxHealthAttribute.getValue();

        // Get the damagers UUID and get the stored number of hits, if it doesn't exist use 0
        UUID id = damager.getUniqueId();

        int hits = bloomHits.getOrDefault(id, 0);

        // Increase their hit count by one, and store it
        hits++;

        bloomHits.put(id, hits);

        // If the hits is greater than or equal to the number of hits required to do the ability
        if (hits >= activateHits) {
            // Increase the damagers health by the amount they damaged, and if it is greater than their maximum health set their health to their max health
            damager.setHealth(Math.min(damager.getHealth() + event.getFinalDamage(), damager.getMaxHealth()));

            // Reset their hit count to 0
            bloomHits.put(id, 0);
        }


    }
}
