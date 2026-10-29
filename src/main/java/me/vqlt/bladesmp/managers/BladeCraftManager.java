package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;

public class BladeCraftManager {
    private final BladeSMP plugin;

    public BladeCraftManager(BladeSMP plugin) {
        this.plugin = plugin;
    }

    public boolean hasReachedLimit(String blade) {
        return (plugin.getRecipesConfig().getInt(blade + ".crafted") >= plugin.getRecipesConfig().getInt(blade + ".limit"));
    }

    public void incrementCrafted(String blade) {
        int crafted = plugin.getRecipesConfig().getInt(blade + ".crafted");
        crafted++;

        plugin.getRecipesConfig().set(blade + ".crafted", crafted);
    }
}
