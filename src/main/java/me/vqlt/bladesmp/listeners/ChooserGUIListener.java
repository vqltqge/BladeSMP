package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.other.ChooserGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class ChooserGUIListener implements Listener {
    private final BladeManager bladeManager;

    public ChooserGUIListener(BladeManager bladeManager) {
        this.bladeManager = bladeManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Check if the open GUI is the Chooser GUI
        if (!(event.getView().getTopInventory().getHolder(false) instanceof ChooserGUI)) {
            return;
        }

        // Cancel all clicks while the Chooser GUI is open
        event.setCancelled(true);

        // Only process clicks inside the top inventory
        if (event.getRawSlot() < 0 || event.getRawSlot() >= event.getView().getTopInventory().getSize()) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();

        if (!bladeManager.isAnyBlade(clickedItem)) {
            return;
        }

        ItemStack itemToGive = clickedItem.asOne();

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);

            if (bladeManager.isBladeChooser(item)) {
                player.getInventory().setItem(i, itemToGive);
                break;
            }
        }

        player.closeInventory();
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder(false) instanceof ChooserGUI) {
            event.setCancelled(true);
        }
    }
}
