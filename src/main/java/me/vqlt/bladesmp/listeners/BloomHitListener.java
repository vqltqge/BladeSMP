package me.vqlt.bladesmp.listeners;

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

    private final BladeManager bladeManager;
    private final PassiveManager passiveManager;
    private final HashMap<UUID, Integer> bloomHits = new HashMap<UUID, Integer>();
    int count = 0;

    public BloomHitListener(BladeManager bladeManager, PassiveManager passiveManager) {
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

        ItemStack hand = damager.getInventory().getItemInMainHand();

        AttributeInstance maxHealthAttribute =
                damager.getAttribute(Attribute.MAX_HEALTH);

        if (maxHealthAttribute == null) {
            return;
        }

        double maxHealth = maxHealthAttribute.getValue();

        if (!(bladeManager.isBloomBlade(hand))) {
            return;
        }

        UUID id = damager.getUniqueId();

        int hits = bloomHits.getOrDefault(id, 0);

        hits++;

        bloomHits.put(id, hits);

        if (hits >= 10) {
            damager.setHealth(Math.min(damager.getHealth() + event.getFinalDamage(), damager.getMaxHealth()));

            bloomHits.put(id, 0);
        }


    }
}
