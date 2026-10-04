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
        Player player = event.getPlayer();

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (bladeManager.isBladeChooser(item)) {
            event.setCancelled(true);
            ChooserGUI gui = new ChooserGUI(bladeManager);
            player.openInventory(gui.getInventory());
        } else if (bladeManager.isRandomiser(item)) {
            event.setCancelled(true);
            item.subtract(1);
            randomBladeManager.rollBlade(player);
        }
    }
}
