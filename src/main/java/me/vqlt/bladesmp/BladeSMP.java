package me.vqlt.bladesmp;

import me.vqlt.bladesmp.abilityone.*;
import me.vqlt.bladesmp.abilitytwo.*;
import me.vqlt.bladesmp.commands.*;
import me.vqlt.bladesmp.listeners.*;
import me.vqlt.bladesmp.managers.*;
import me.vqlt.bladesmp.other.PassiveTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BladeSMP extends JavaPlugin {

    private FileConfiguration recipesConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Creates config.yml from the default config if it doesn't exist
        saveDefaultConfig();

        // Create recipes.yml if it doesn't exist and load the contents
        saveResource("recipes.yml", false);
        recipesConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "recipes.yml"));

        // Initialise managers
        BladeManager bladeManager = new BladeManager(this);
        PassiveManager passiveManager = new PassiveManager(this);
        DurationManager durationManager = new DurationManager(this);
        CooldownManager cooldownManager = new CooldownManager();
        RandomBladeManager randomBladeManager = new RandomBladeManager(this, bladeManager);

        // Initialise the recipe manager and register the custom recipes
        RecipeManager recipeManager = new RecipeManager(this, bladeManager);
        recipeManager.registerRecipes();

        // Initialise every ability one
        PulseAbilityOne pulseAbilityOne = new PulseAbilityOne(this, cooldownManager, bladeManager);
        BloomAbilityOne bloomAbilityOne = new BloomAbilityOne(this, bladeManager, cooldownManager, durationManager);
        FlameAbilityOne flameAbilityOne = new FlameAbilityOne(bladeManager, cooldownManager, durationManager, this);
        FrostAbilityOne frostAbilityOne = new FrostAbilityOne(this, durationManager, cooldownManager, bladeManager);
        FortuneAbilityOne fortuneAbilityOne = new FortuneAbilityOne(bladeManager, cooldownManager, durationManager, this);

        // Initialise every ability two
        FlameAbilityTwo flameAbilityTwo = new FlameAbilityTwo(bladeManager, cooldownManager, durationManager, this);
        FrostAbilityTwo frostAbilityTwo = new FrostAbilityTwo(bladeManager, cooldownManager, durationManager, this);
        BloomAbilityTwo bloomAbilityTwo = new BloomAbilityTwo(bladeManager, cooldownManager, durationManager, this);
        FortuneAbilityTwo fortuneAbilityTwo = new FortuneAbilityTwo(bladeManager, cooldownManager, durationManager, this);
        PulseAbilityTwo pulseAbilityTwo = new PulseAbilityTwo(bladeManager, cooldownManager, durationManager, this);

        // Initialise the actionbar manager and passive task, and start them
        ActionBarManager actionBarManager = new ActionBarManager(cooldownManager, durationManager, bladeManager, this);
        actionBarManager.start();
        PassiveTask passiveTask = new PassiveTask(this, passiveManager, bladeManager);
        passiveTask.start();

        // Initialise the commands
        getCommand("blades").setExecutor(new BladesCommand(bladeManager));
        getCommand("ability1").setExecutor(new AbilityOneCommand(bladeManager, pulseAbilityOne, bloomAbilityOne, flameAbilityOne, frostAbilityOne, fortuneAbilityOne));
        getCommand("ability2").setExecutor(new AbilityTwoCommand(bladeManager, flameAbilityTwo, frostAbilityTwo, bloomAbilityTwo, fortuneAbilityTwo, pulseAbilityTwo));
        getCommand("random").setExecutor(new RandomCommand(randomBladeManager, this));
        getCommand("cooldown").setExecutor(new CooldownCommand(cooldownManager));

        // Initialise the listeners
        getServer().getPluginManager().registerEvents(new BladesGUIListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(bladeManager), this);
        getServer().getPluginManager().registerEvents(new FrostHitListener(bladeManager, passiveManager, frostAbilityTwo), this);
        getServer().getPluginManager().registerEvents(new BloomHitListener(this, bladeManager, passiveManager), this);
        getServer().getPluginManager().registerEvents(new FortuneExperienceListener(this, bladeManager, passiveManager), this);
        getServer().getPluginManager().registerEvents(new FrostMoveListener(frostAbilityOne, frostAbilityTwo, passiveManager), this);
        getServer().getPluginManager().registerEvents(new PlayerConsumeListener(fortuneAbilityOne, this), this);
        getServer().getPluginManager().registerEvents(fortuneAbilityTwo, this);
        getServer().getPluginManager().registerEvents(new ChooserGUIListener(bladeManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(bladeManager, randomBladeManager), this);
        getServer().getPluginManager().registerEvents(new RandomInventoryListener(randomBladeManager), this);
    }

    // Create a getter for the recipe config
    public FileConfiguration getRecipesConfig() {
        return recipesConfig;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

