package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.RandomBladeManager;
import me.vqlt.bladesmp.other.ChooserGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final BladeManager bladeManager;
    private final RandomBladeManager randomBladeManager;

    public PlayerInteractListener(BladeManager bladeManager, RandomBladeManager randomBladeManager) {
        this.bladeManager = bladeManager;
        this.randomBladeManager = randomBladeManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // If the player interacted with their offhand, return
        Player player = event.getPlayer();

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        // If the player did not right-click the air or a block, return
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // If the item they right-clicked with doesn't exist return
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (bladeManager.isBladeChooser(item)) {
            // If they right-clicked the blade chooser item open the blade chooser GUI. Also cancel the action
            event.setCancelled(true);
            ChooserGUI gui = new ChooserGUI(bladeManager);
            player.openInventory(gui.getInventory());
        } else if (bladeManager.isRandomiser(item)) {
            // If they right-clicked the randomiser item open the randomiser GUI. Also cancel the action
            event.setCancelled(true);

            if (randomBladeManager.isRolling(player)) {
                return;
            }

            item.subtract(1);

            randomBladeManager.rollBlade(player);
        }
    }
}
