package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {

    private final BladeSMP plugin;
    private final BladeManager bladeManager;

    public RecipeManager(BladeSMP plugin, BladeManager bladeManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
    }

    public void registerRecipes() {

    }

    private void registerFlameBladeRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "flame_blade_recipe");

        ShapedRecipe recipe = new ShapedRecipe(key, bladeManager.createFlameBlade());

        recipe.shape("BBB", "BNB", " S ");

        recipe.setIngredient('B', Material.BLAZE_ROD);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('S', Material.NETHERITE_SWORD);

        Bukkit.addRecipe(recipe);
    }
}
