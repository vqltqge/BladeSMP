package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeCraftManager;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Recipe;

public class BladeCraftListener implements Listener {
    private final BladeSMP plugin;
    private final BladeCraftManager bladeCraftManager;

    public BladeCraftListener(BladeSMP plugin, BladeCraftManager bladeCraftManager) {
        this.plugin = plugin;
        this.bladeCraftManager = bladeCraftManager;
    }

    private String getBladeID(Recipe recipe) {

        if (!(recipe instanceof Keyed keyed)) {
            return null;
        }

        NamespacedKey key = keyed.getKey();

        if (!key.getNamespace().equals("bladesmp")) {
            return null;
        }

        String blade = key.getKey();

        if (!plugin.getConfig().contains(blade + ".limit")) {
            return null;
        }

        return blade;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();

        if (recipe == null) {
            return;
        }

        String blade = getBladeID(recipe);

        if (blade == null) {
            return;
        }

        boolean enabled = plugin.getConfig().getBoolean(blade + ".enabled", true);

        if (!enabled || bladeCraftManager.hasReachedLimit(blade)) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        String blade = getBladeID(event.getRecipe());

        if (blade == null) {
            return;
        }

        boolean enabled = plugin.getConfig().getBoolean(blade + ".enabled", true);

        if (!enabled) {
            event.setCancelled(true);
            return;
        }

        if (bladeCraftManager.hasReachedLimit(blade)) {
            event.setCancelled(true);
            return;
        }

        if (event.isShiftClick()) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                player.sendMessage("§cCaft blades individually");
            }

            return;
        }

        if (event.getAction() == InventoryAction.NOTHING) {
            return;
        }

        bladeCraftManager.incrementCrafted(blade);
    }


}
