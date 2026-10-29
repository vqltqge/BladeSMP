package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeCraftManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.inventory.Recipe;

public class CrafterCraftListener implements Listener {

    private final BladeSMP plugin;
    private final BladeCraftManager bladeCraftManager;
    private final BladeCraftListener bladeCraftListener;

    public CrafterCraftListener(
            BladeSMP plugin,
            BladeCraftManager bladeCraftManager,
            BladeCraftListener bladeCraftListener
    ) {
        this.plugin = plugin;
        this.bladeCraftManager = bladeCraftManager;
        this.bladeCraftListener = bladeCraftListener;
    }

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {

        Recipe recipe = event.getRecipe();

        if (recipe == null) {
            return;
        }

        String blade = bladeCraftListener.getBladeID(recipe);

        if (blade == null) {
            return;
        }

        boolean enabled = plugin.getRecipesConfig().getBoolean(blade + ".enabled", true);

        if (!enabled || bladeCraftManager.hasReachedLimit(blade)) {
            event.setCancelled(true);
            return;
        }

        bladeCraftManager.incrementCrafted(blade);
    }
}