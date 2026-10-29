package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.abilitytwo.*;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbilityTwoCommand implements CommandExecutor {

    // Variables for the classes with the methods needed
    private final BladeManager bladeManager;
    private final PulseAbilityTwo pulseAbilityTwo;
    private final BloomAbilityTwo bloomAbilityTwo;
    private final FlameAbilityTwo flameAbilityTwo;
    private final FrostAbilityTwo frostAbilityTwo;
    private final FortuneAbilityTwo fortuneAbilityTwo;
    private final StormAbilityTwo stormAbilityTwo;
    private final BladeSMP plugin;


    public AbilityTwoCommand(BladeManager bladeManager, PulseAbilityTwo pulseAbilityTwo, BloomAbilityTwo bloomAbilityTwo, FlameAbilityTwo flameAbilityTwo, FrostAbilityTwo frostAbilityTwo, FortuneAbilityTwo fortuneAbilityTwo, StormAbilityTwo stormAbilityTwo, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.pulseAbilityTwo = pulseAbilityTwo;
        this.bloomAbilityTwo = bloomAbilityTwo;
        this.flameAbilityTwo = flameAbilityTwo;
        this.frostAbilityTwo = frostAbilityTwo;
        this.fortuneAbilityTwo = fortuneAbilityTwo;
        this.stormAbilityTwo = stormAbilityTwo;
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
                pulseAbilityTwo.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isBloomBlade(hand)) {
            if (isEnabled("bloom")) {
                bloomAbilityTwo.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isFlameBlade(hand)) {
            if (isEnabled("flame")) {
                flameAbilityTwo.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isFrostBlade(hand)) {
            if (isEnabled("frost")) {
                frostAbilityTwo.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isFortuneBlade(hand)) {
            if (isEnabled("fortune")) {
                fortuneAbilityTwo.activate(player);
            } else {
                player.sendMessage("§cThat ability is disabled.");
            }
        }

        if (bladeManager.isStormBlade(hand)) {
            if (isEnabled("storm")) {
                stormAbilityTwo.activate(player);
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
        return plugin.getConfig().getBoolean(ability + ".ability-two.enabled", true);
    }
}