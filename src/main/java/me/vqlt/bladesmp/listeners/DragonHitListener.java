package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.abilitytwo.DragonAbilityTwo;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class DragonHitListener implements Listener {
    private final DragonAbilityTwo dragonAbilityTwo;

    public DragonHitListener(DragonAbilityTwo dragonAbilityTwo) {
        this.dragonAbilityTwo = dragonAbilityTwo;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        UUID id = event.getDamager().getUniqueId();

        // Returns if the entity who dealt the damage is not a player or the entity that was damaged is not a player
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (dragonAbilityTwo.armedPlayers.contains(id)) {
            dragonAbilityTwo.removeCharge(attacker, id);
        }

        if (!dragonAbilityTwo.activePlayers.contains(attacker.getUniqueId())) {
            return;
        }

        if (event.isCritical()) {
            return;
        }

        event.setDamage(event.getDamage() * 1.5);

        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f);

        victim.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0.0, victim.getHeight() / 2.0, 0.0), 10, 0.25, 0.35, 0.25, 0.05);
    }

}
