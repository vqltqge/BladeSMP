package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.BladeCraftManager;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class BladeCraftListener implements Listener {
    private final BladeCraftManager bladeCraftManager;
    private final BladeManager bladeManager;

    public BladeCraftListener(BladeCraftManager bladeCraftManager, BladeManager bladeManager) {
        this.bladeCraftManager = bladeCraftManager;
        this.bladeManager = bladeManager;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getRecipe() != null ? event.getRecipe().getResult() : null;

        if (result == null) {
            return;
        }


    }

}
