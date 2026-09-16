package me.vqlt.bladesmp;

import me.vqlt.bladesmp.abilities.*;
import me.vqlt.bladesmp.commands.AbilityOneCommand;
import me.vqlt.bladesmp.commands.ConfigCommand;
import me.vqlt.bladesmp.commands.CooldownCommand;
import me.vqlt.bladesmp.commands.WeaponsCommand;
import me.vqlt.bladesmp.listeners.*;
import me.vqlt.bladesmp.managers.*;
import me.vqlt.bladesmp.other.PassiveTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class BladeSMP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        BladeManager bladeManager = new BladeManager(this);
        PassiveManager passiveManager = new PassiveManager(this);
        DurationManager durationManager = new DurationManager(this);
        CooldownManager cooldownManager = new CooldownManager();
        StormAbilityOne stormAbilityOne = new StormAbilityOne(this, bladeManager, cooldownManager, durationManager);
        PulseAbilityOne pulseAbilityOne = new PulseAbilityOne(this, cooldownManager, bladeManager);
        BloomAbilityOne bloomAbilityOne = new BloomAbilityOne(this, bladeManager, cooldownManager, durationManager);
        FlameAbilityOne flameAbilityOne = new FlameAbilityOne(bladeManager, cooldownManager, durationManager, this);
        FrostAbilityOne frostAbilityOne = new FrostAbilityOne(this, durationManager, cooldownManager, bladeManager);
        FortuneAbilityOne fortuneAbilityOne = new FortuneAbilityOne(bladeManager, cooldownManager, durationManager, this);
        TidalAbilityOne tidalAbilityOne = new TidalAbilityOne(bladeManager, cooldownManager, durationManager, this);

        ActionBarManager actionBarManager = new ActionBarManager(cooldownManager, durationManager, bladeManager, this);
        actionBarManager.start();
        PassiveTask passiveTask = new PassiveTask(this, passiveManager, bladeManager);
        passiveTask.start();

        getCommand("weapons").setExecutor(new WeaponsCommand(bladeManager));
        getCommand("ability1").setExecutor(new AbilityOneCommand(bladeManager, stormAbilityOne, pulseAbilityOne, bloomAbilityOne, flameAbilityOne, frostAbilityOne, fortuneAbilityOne, tidalAbilityOne));
        getCommand("cooldown").setExecutor(new CooldownCommand(cooldownManager));
        getCommand("config").setExecutor(new ConfigCommand(this));

        getServer().getPluginManager().registerEvents(new WeaponsGUIListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(bladeManager), this);
        getServer().getPluginManager().registerEvents(new FrostHitListener(bladeManager, passiveManager), this);
        getServer().getPluginManager().registerEvents(new BloomHitListener(this, bladeManager, passiveManager), this);
        getServer().getPluginManager().registerEvents(new StaticHitListener(bladeManager, passiveManager), this);
        getServer().getPluginManager().registerEvents(new FortuneExperienceListener(this, bladeManager, passiveManager), this);
        getServer().getPluginManager().registerEvents(stormAbilityOne, this);
        getServer().getPluginManager().registerEvents(new FrostMoveListener(frostAbilityOne), this);
        getServer().getPluginManager().registerEvents(new PlayerConsumeListener(fortuneAbilityOne, this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

