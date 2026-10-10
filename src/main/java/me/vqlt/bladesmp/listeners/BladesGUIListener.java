package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.other.BladesGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BladesGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get the inventory that was clicked. If it doesn't exist or is not the BladesGUI, return
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) {
            return;
        }

        if (!(clickedInventory.getHolder(false) instanceof BladesGUI)) {
            return;
        }


        // Cancel the inventory click action
        event.setCancelled(true);

        // If a player did not click the inventory, return
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Get the item that was clicked. If it doesn't exist, is air or is a cyan glass pane (the border of the GUI), return
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType() == Material.CYAN_STAINED_GLASS_PANE) {
            return;
        }

        // Copy the blade that will be given to the player
        ItemStack itemToGive = clickedItem.clone();

        // Try to give the player the item, and if it fails (because their inventory is full), add it to this hashmap
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(itemToGive);

        // Loop through all the leftover items that couldn't be given, and drop them naturally on the ground
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }

}
