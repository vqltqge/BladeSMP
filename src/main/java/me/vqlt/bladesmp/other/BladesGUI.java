package me.vqlt.bladesmp.other;

import me.vqlt.bladesmp.managers.BladeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BladesGUI implements InventoryHolder {
    private final Inventory inventory;
    private final BladeManager bladeManager;

    public BladesGUI(BladeManager bladeManager) {
        this.bladeManager = bladeManager;

        inventory = Bukkit.createInventory(this, 27, Component.text("Blades").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true));

        setupItems();
    }

    private void setupItems() {
        for (int i = 0; i <= 10; i++) {
            inventory.setItem(i, new ItemStack(Material.CYAN_STAINED_GLASS_PANE));
        }

        for (int i = 16; i <= 26; i++) {
            inventory.setItem(i, new ItemStack(Material.CYAN_STAINED_GLASS_PANE));
        }

        inventory.setItem(11, bladeManager.createFlameBlade());
        inventory.setItem(12, bladeManager.createFrostBlade());
        inventory.setItem(13, bladeManager.createBloomBlade());
        inventory.setItem(14, bladeManager.createPulseBlade());
        inventory.setItem(15, bladeManager.createFortuneBlade());

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
