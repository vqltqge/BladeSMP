package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.abilityone.*;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbilityOneCommand implements CommandExecutor {

    private final BladeManager bladeManager;
    private final PulseAbilityOne pulseAbilityOne;
    private final BloomAbilityOne bloomAbilityOne;
    private final FlameAbilityOne flameAbilityOne;
    private final FrostAbilityOne frostAbilityOne;
    private final FortuneAbilityOne fortuneAbilityOne;


    public AbilityOneCommand(BladeManager bladeManager, PulseAbilityOne pulseAbilityOne, BloomAbilityOne bloomAbilityOne, FlameAbilityOne flameAbilityOne, FrostAbilityOne frostAbilityOne, FortuneAbilityOne fortuneAbilityOne) {
        this.bladeManager = bladeManager;
        this.pulseAbilityOne = pulseAbilityOne;
        this.bloomAbilityOne = bloomAbilityOne;
        this.flameAbilityOne = flameAbilityOne;
        this.frostAbilityOne = frostAbilityOne;
        this.fortuneAbilityOne = fortuneAbilityOne;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (bladeManager.isPulseBlade(hand)) {
            pulseAbilityOne.activate(player);
        }

        if (bladeManager.isBloomBlade(hand)) {
            bloomAbilityOne.activate(player);
        }

        if (bladeManager.isFlameBlade(hand)) {
            flameAbilityOne.activate(player);
        }

        if (bladeManager.isFrostBlade(hand)) {
            frostAbilityOne.activate(player);
        }

        if (bladeManager.isFortuneBlade(hand)) {
            fortuneAbilityOne.activate(player);
        }

        if (!bladeManager.isTidalBlade(hand) && !bladeManager.isBloomBlade(hand) && !bladeManager.isFlameBlade(hand) && !bladeManager.isStormBlade(hand) && !bladeManager.isFortuneBlade(hand) && !bladeManager.isPulseBlade(hand) && !bladeManager.isFrostBlade(hand)) {
            player.sendMessage("§aYou must be holding a blade to use an ability.");
        }

        return true;
    }
}