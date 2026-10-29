package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.abilityone.*;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbilityOneCommand implements CommandExecutor {

    // Variables for the classes with the methods needed
    private final BladeManager bladeManager;
    private final PulseAbilityOne pulseAbilityOne;
    private final BloomAbilityOne bloomAbilityOne;
    private final FlameAbilityOne flameAbilityOne;
    private final FrostAbilityOne frostAbilityOne;
    private final FortuneAbilityOne fortuneAbilityOne;
    private final StormAbilityOne stormAbilityOne;
    private final BladeSMP plugin;


    public AbilityOneCommand(BladeManager bladeManager, PulseAbilityOne pulseAbilityOne, BloomAbilityOne bloomAbilityOne, FlameAbilityOne flameAbilityOne, FrostAbilityOne frostAbilityOne, FortuneAbilityOne fortuneAbilityOne, StormAbilityOne stormAbilityOne, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.pulseAbilityOne = pulseAbilityOne;
        this.bloomAbilityOne = bloomAbilityOne;
        this.flameAbilityOne = flameAbilityOne;
        this.frostAbilityOne = frostAbilityOne;
        this.fortuneAbilityOne = fortuneAbilityOne;
        this.stormAbilityOne = stormAbilityOne;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // If this was not sent by a player, return
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only");
            return true;
        }

        // Get the item in the players hand, and loop through all the blades and activate their abilities
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (bladeManager.isPulseBlade(hand)) {
            if (isEnabled("pulse")) {
                pulseAbilityOne.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isBloomBlade(hand)) {
            if (isEnabled("bloom")) {
                bloomAbilityOne.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isFlameBlade(hand)) {
            if (isEnabled("flame")) {
                flameAbilityOne.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isFrostBlade(hand)) {
            if (isEnabled("frost")) {
                frostAbilityOne.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isFortuneBlade(hand)) {
            if (isEnabled("fortune")) {
                fortuneAbilityOne.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isStormBlade(hand)) {
            if (isEnabled("storm")) {
                stormAbilityOne.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        // If they are not holding a blade, tell them
        if (!bladeManager.isBloomBlade(hand) && !bladeManager.isFlameBlade(hand) && !bladeManager.isFortuneBlade(hand) && !bladeManager.isPulseBlade(hand) && !bladeManager.isFrostBlade(hand) && !bladeManager.isStormBlade(hand)) {
            player.sendMessage("§cYou must be holding a blade to use an ability.");
        }

        return true;
    }

    private boolean isEnabled(String ability) {
        return plugin.getConfig().getBoolean(ability + ".ability-one.enabled", true);
    }
}