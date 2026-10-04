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
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (randomBladeManager.isRolling(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (randomBladeManager.isRolling(player)) {
            event.setCancelled(true);
        }
    }
}
