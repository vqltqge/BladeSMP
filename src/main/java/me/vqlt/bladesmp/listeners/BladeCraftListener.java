package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeCraftManager;
import me.vqlt.bladesmp.managers.RitualManager;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class BladeCraftListener implements Listener {

    private final BladeSMP plugin;
    private final BladeCraftManager bladeCraftManager;
    private final RitualManager ritualManager;

    public BladeCraftListener(BladeSMP plugin,
                              BladeCraftManager bladeCraftManager,
                              RitualManager ritualManager) {
        this.plugin = plugin;
        this.bladeCraftManager = bladeCraftManager;
        this.ritualManager = ritualManager;
    }

    public String getBladeID(Recipe recipe) {

        if (!(recipe instanceof Keyed keyed)) {
            return null;
        }

        NamespacedKey key = keyed.getKey();

        if (!key.getNamespace().equals("bladesmp")) {
            return null;
        }

        String blade = key.getKey();

        if (!blade.endsWith("_blade")) {
            return null;
        }

        blade = blade.substring(0, blade.length() - "_blade".length());

        if (!plugin.getRecipesConfig().contains(blade + ".limit")) {
            return null;
        }

        return blade;
    }

    private boolean requiresRitual(String blade) {

        if (!plugin.getConfig().getBoolean("rituals-enabled", false)) {
            return false;
        }

        // Only the configured first blade requires a ritual
        if (plugin.getConfig().getBoolean("rituals.first-blade-only", false)) {

            String firstBlade = plugin.getConfig().getString("rituals.first-blade", "flame");

            return blade.equalsIgnoreCase(firstBlade);
        }

        // All blades require rituals
        return true;
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

        boolean enabled = plugin.getRecipesConfig().getBoolean(blade + ".enabled", true);

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

        boolean enabled = plugin.getRecipesConfig().getBoolean(blade + ".enabled", true);

        if (!enabled || bladeCraftManager.hasReachedLimit(blade)) {
            event.setCancelled(true);
            return;
        }

        if (event.isShiftClick()) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player player) {
                player.sendMessage("§cCraft blades individually");
            }

            return;
        }

        if (event.getAction() == InventoryAction.NOTHING) {
            return;
        }

        // Check whether this blade requires a ritual
        if (requiresRitual(blade)) {

            event.setCancelled(true);

            if (!(event.getWhoClicked() instanceof Player player)) {
                return;
            }

            if (ritualManager == null) {
                player.sendMessage("§cRituals are currently unavailable.");
                return;
            }

            CraftingInventory inventory = event.getInventory();

            // Get the crafting location
            Location location = inventory.getLocation();

            // Player inventory crafting has no block location
            if (location == null) {
                location = player.getLocation();
            } else {
                location = location.clone().add(0.5, 1, 0.5);
            }

            // Get the blade being crafted
            ItemStack result = event.getRecipe().getResult().clone();

            // Start the ritual
            boolean started = ritualManager.startRitual(
                    location,
                    result,
                    () -> bladeCraftManager.incrementCrafted(blade)
            );

            if (!started) {
                player.sendMessage("§cA ritual is already active here.");
                return;
            }

            // Consume the crafting ingredients
            ItemStack[] matrix = inventory.getMatrix();

            for (int i = 0; i < matrix.length; i++) {

                ItemStack item = matrix[i];

                if (item == null) {
                    continue;
                }

                if (item.getAmount() <= 1) {
                    matrix[i] = null;
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            }

            inventory.setMatrix(matrix);
            player.updateInventory();

            return;
        }

        // Normal crafting (no ritual required)
        bladeCraftManager.incrementCrafted(blade);
    }
}
