package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.other.ChooserGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChooserGUIListener implements Listener {
    private final BladeManager bladeManager;

    public ChooserGUIListener(BladeManager bladeManager) {
        this.bladeManager = bladeManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get the inventory that was clicked and return if it does not exist or it is not the chooser gui
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) {
            return;
        }

        if (!(clickedInventory.getHolder(false) instanceof ChooserGUI)) {
            return;
        }

        // Cancel the click action
        event.setCancelled(true);

        // If a player did not click the GUI, return
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Get the item that was clicked, and if it doesn't exist, it is air or it is a cyan glass pane (the border of the gui), return
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType() == Material.CYAN_STAINED_GLASS_PANE) {
            return;
        }

        // Make an item stack of the blade that the player clicked on, as a single item
        ItemStack itemToGive = clickedItem.asOne();

        // For every slot in the inventory
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            // Get the item in the slot, and if it is the blade chooser item, replace it with the blade the player clicked on
            ItemStack item = player.getInventory().getItem(i);

            if (bladeManager.isBladeChooser(item)) {
                player.getInventory().setItem(i, itemToGive);
                break;
            }
        }

        // Close the GUI
        player.closeInventory();
    }
}
