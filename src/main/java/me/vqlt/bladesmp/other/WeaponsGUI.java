package me.vqlt.bladesmp.other;

import me.vqlt.bladesmp.managers.BladeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class WeaponsGUI implements InventoryHolder {
    private final Inventory inventory;
    private final BladeManager bladeManager;

    public WeaponsGUI(BladeManager bladeManager) {
        this.bladeManager = bladeManager;

        inventory = Bukkit.createInventory(this, 9, Component.text("Weapons"));

        setupItems();
    }

    private void setupItems() {
        inventory.setItem(1, bladeManager.createFlameBlade());
        inventory.setItem(2, bladeManager.createFrostBlade());
        inventory.setItem(3, bladeManager.createBloomBlade());
        inventory.setItem(4, bladeManager.createTidalBlade());
        inventory.setItem(5, bladeManager.createPulseBlade());
        inventory.setItem(6, bladeManager.createStormBlade());
        inventory.setItem(7, bladeManager.createFortuneBlade());

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
