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
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) {
            return;
        }

        if (!(clickedInventory.getHolder(false) instanceof ChooserGUI)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir() || clickedItem.getType() == Material.CYAN_STAINED_GLASS_PANE) {
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
}
