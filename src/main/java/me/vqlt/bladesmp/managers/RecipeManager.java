package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

public class RecipeManager {

    private final BladeSMP plugin;
    private final BladeManager bladeManager;

    public RecipeManager(BladeSMP plugin, BladeManager bladeManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
    }

    public void registerRecipes() {
        ShapedRecipe flameRecipe = new ShapedRecipe(new NamespacedKey(plugin, "flame_blade_recipe"), bladeManager.createFlameBlade());
        registerRecipe("flame", flameRecipe);

        ShapedRecipe frostRecipe = new ShapedRecipe(new NamespacedKey(plugin, "frost_blade_recipe"), bladeManager.createFrostBlade());
        registerRecipe("frost", frostRecipe);

        ShapedRecipe bloomRecipe = new ShapedRecipe(new NamespacedKey(plugin, "bloom_blade_recipe"), bladeManager.createBloomBlade());
        registerRecipe("bloom", bloomRecipe);

        ShapedRecipe pulseRecipe = new ShapedRecipe(new NamespacedKey(plugin, "pulse_blade_recipe"), bladeManager.createPulseBlade());
        registerRecipe("pulse", pulseRecipe);

        ShapedRecipe fortuneRecipe = new ShapedRecipe(new NamespacedKey(plugin, "fortune_blade_recipe"), bladeManager.createFortuneBlade());
        registerRecipe("fortune", fortuneRecipe);
    }

    private void registerRecipe(String blade, ShapedRecipe recipe) {

        List<String> shape = plugin.getRecipesConfig().getStringList(blade + ".shape");

        recipe.shape(shape.get(0), shape.get(1), shape.get(2));

        for (String ingredient : plugin.getRecipesConfig().getConfigurationSection(blade + ".ingredients").getKeys(false)) {

            Material material = Material.matchMaterial(plugin.getRecipesConfig().getString(blade + ".ingredients." + ingredient));

            recipe.setIngredient(ingredient.charAt(0), material);
        }

        Bukkit.addRecipe(recipe);
    }
}
