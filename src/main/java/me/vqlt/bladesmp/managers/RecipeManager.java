package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
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
        registerRecipe("flame", bladeManager.createFlameBlade());
        registerRecipe("frost", bladeManager.createFrostBlade());
        registerRecipe("bloom", bladeManager.createBloomBlade());
        registerRecipe("pulse", bladeManager.createPulseBlade());
        registerRecipe("fortune", bladeManager.createFortuneBlade());
    }

    private void registerRecipe(String blade, ItemStack result) {

        // Get recipe shape
        List<String> shape = plugin.getRecipesConfig().getStringList(blade + ".shape");

        // Make sure there are exactly 3 rows
        if (shape.size() != 3) {
            plugin.getLogger().warning("Invalid recipe shape for " + blade + ". Expected exactly 3 rows.");
            return;
        }

        // Make sure each row is exactly 3 characters
        for (String row : shape) {
            if (row.length() != 3) {
                plugin.getLogger().warning("Invalid recipe shape for " + blade + ". Each row must contain exactly 3 characters.");
                return;
            }
        }

        // Get ingredients section
        ConfigurationSection ingredients = plugin.getRecipesConfig().getConfigurationSection(blade + ".ingredients");

        if (ingredients == null) {
            plugin.getLogger().warning("Missing ingredients section for " + blade + ".");
            return;
        }

        for (String row : shape) {
            for (char character : row.toCharArray()) {
                if (character == ' ') {
                    continue;
                }

                if (!ingredients.contains(String.valueOf(character))) {
                    plugin.getLogger().warning("Missing ingredient '" + character + "' in " + blade + " recipe.");
                    return;
                }
            }
        }

        NamespacedKey recipeKey = new NamespacedKey(plugin, blade + "_blade");

        ShapedRecipe recipe = new ShapedRecipe(recipeKey, result);

        recipe.shape(shape.get(0), shape.get(1), shape.get(2));

        // Add every ingredient from recipes.yml
        for (String key : ingredients.getKeys(false)) {

            // Recipe keys should be one character, e.g. A, B, C
            if (key.length() != 1) {
                plugin.getLogger().warning("Invalid ingredient key '" + key + "' in " + blade + " recipe.");
                continue;
            }

            String materialName = ingredients.getString(key);

            if (materialName == null) {
                plugin.getLogger().warning("Missing material for ingredient '" + key + "' in " + blade + " recipe.");
                continue;
            }

            Material material = Material.matchMaterial(materialName);

            if (material == null) {
                plugin.getLogger().warning("Invalid material '" + materialName + "' in " + blade + " recipe.");
                return;
            }

            recipe.setIngredient(
                    key.charAt(0),
                    material
            );
        }

        plugin.getServer().addRecipe(recipe);
    }
}