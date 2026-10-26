package me.vqlt.bladesmp.commands;

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
    private final FlameAbilityTwo flameAbilityTwo;
    private final FrostAbilityTwo frostAbilityTwo;
    private final BloomAbilityTwo bloomAbilityTwo;
    private final FortuneAbilityTwo fortuneAbilityTwo;
    private final PulseAbilityTwo pulseAbilityTwo;
    private final StormAbilityTwo stormAbilityTwo;

    public AbilityTwoCommand(BladeManager bladeManager, FlameAbilityTwo flameAbilityTwo, FrostAbilityTwo frostAbilityTwo, BloomAbilityTwo bloomAbilityTwo, FortuneAbilityTwo fortuneAbilityTwo, PulseAbilityTwo pulseAbilityTwo, StormAbilityTwo stormAbilityTwo) {
        this.bladeManager = bladeManager;
        this.flameAbilityTwo = flameAbilityTwo;
        this.frostAbilityTwo = frostAbilityTwo;
        this.bloomAbilityTwo = bloomAbilityTwo;
        this.fortuneAbilityTwo = fortuneAbilityTwo;
        this.pulseAbilityTwo = pulseAbilityTwo;
        this.stormAbilityTwo = stormAbilityTwo;
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
            pulseAbilityTwo.activate(player);
        }

        if (bladeManager.isBloomBlade(hand)) {
            bloomAbilityTwo.activate(player);
        }

        if (bladeManager.isFlameBlade(hand)) {
            flameAbilityTwo.activate(player);
        }

        if (bladeManager.isFrostBlade(hand)) {
            frostAbilityTwo.activate(player);
        }

        if (bladeManager.isFortuneBlade(hand)) {
            fortuneAbilityTwo.activate(player);
        }

        if (bladeManager.isStormBlade(hand)) {
            stormAbilityTwo.activate(player);
        }

        // If they are not holding a blade, tell them
        if (!bladeManager.isBloomBlade(hand) && !bladeManager.isFlameBlade(hand) && !bladeManager.isFortuneBlade(hand) && !bladeManager.isPulseBlade(hand) && !bladeManager.isFrostBlade(hand) && !bladeManager.isStormBlade(hand)) {
            player.sendMessage("§aYou must be holding a blade to use an ability.");
        }

        return true;
    }
}
