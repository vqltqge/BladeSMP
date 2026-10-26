package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.RandomBladeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class RandomInventoryListener implements Listener {
    private final RandomBladeManager randomBladeManager;

    public RandomInventoryListener(RandomBladeManager randomBladeManager) {
        this.randomBladeManager = randomBladeManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // If the GUI was not clicked by a player, return
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // If the player is already rolling a blade, cancel the inventory click event
        if (randomBladeManager.isRolling(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // If the GUI was not clicked by a player, return
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // If the player is already rolling a blade, cancel the inventory click event
        if (randomBladeManager.isRolling(player)) {
            event.setCancelled(true);
        }
    }
}
