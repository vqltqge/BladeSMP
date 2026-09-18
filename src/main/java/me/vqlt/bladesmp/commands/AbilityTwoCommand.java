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
    private final BladeManager bladeManager;
    private final FlameAbilityTwo flameAbilityTwo;
    private final FrostAbilityTwo frostAbilityTwo;
    private final BloomAbilityTwo bloomAbilityTwo;
    private final FortuneAbilityTwo fortuneAbilityTwo;
    private final PulseAbilityTwo pulseAbilityTwo;
    private final TidalAbilityTwo tidalAbilityTwo;
    private final StormAbilityTwo stormAbilityTwo;

    public AbilityTwoCommand(BladeManager bladeManager, FlameAbilityTwo flameAbilityTwo, FrostAbilityTwo frostAbilityTwo, BloomAbilityTwo bloomAbilityTwo, FortuneAbilityTwo fortuneAbilityTwo, PulseAbilityTwo pulseAbilityTwo, TidalAbilityTwo tidalAbilityTwo, StormAbilityTwo stormAbilityTwo) {
        this.bladeManager = bladeManager;
        this.flameAbilityTwo = flameAbilityTwo;
        this.frostAbilityTwo = frostAbilityTwo;
        this.bloomAbilityTwo = bloomAbilityTwo;
        this.fortuneAbilityTwo = fortuneAbilityTwo;
        this.pulseAbilityTwo = pulseAbilityTwo;
        this.tidalAbilityTwo = tidalAbilityTwo;
        this.stormAbilityTwo = stormAbilityTwo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (bladeManager.isStormBlade(hand)) {
            stormAbilityTwo.activate(player);
        }

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

        if (bladeManager.isTidalBlade(hand)) {
            tidalAbilityTwo.activate(player);
        }


        if (!bladeManager.isTidalBlade(hand) && !bladeManager.isBloomBlade(hand) && !bladeManager.isFlameBlade(hand) && !bladeManager.isStormBlade(hand) && !bladeManager.isFortuneBlade(hand) && !bladeManager.isPulseBlade(hand) && !bladeManager.isFrostBlade(hand)) {
            player.sendMessage("§aYou must be holding a blade to use an ability.");
        }

        return true;
    }
}
