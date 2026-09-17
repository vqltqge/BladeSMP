package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.abilitytwo.FlameAbilityTwo;
import me.vqlt.bladesmp.abilitytwo.FrostAbilityTwo;
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

    public AbilityTwoCommand(BladeManager bladeManager, FlameAbilityTwo flameAbilityTwo, FrostAbilityTwo frostAbilityTwo) {
        this.bladeManager = bladeManager;
        this.flameAbilityTwo = flameAbilityTwo;
        this.frostAbilityTwo = frostAbilityTwo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (bladeManager.isStormBlade(hand)) {
            // stormAbilityOne.activate(player);
            player.sendMessage("Activated storm2");
        }

        if (bladeManager.isPulseBlade(hand)) {
//            pulseAbilityOne.activate(player);
            player.sendMessage("Activated pulse2");
        }

        if (bladeManager.isBloomBlade(hand)) {
//            bloomAbilityOne.activate(player);
            player.sendMessage("Activated bloom2");
        }

        if (bladeManager.isFlameBlade(hand)) {
            flameAbilityTwo.activate(player);
        }

        if (bladeManager.isFrostBlade(hand)) {
            frostAbilityTwo.activate(player);
        }

        if (bladeManager.isFortuneBlade(hand)) {
//            fortuneAbilityOne.activate(player);
            player.sendMessage("Activated fortune2");
        }

        if (bladeManager.isTidalBlade(hand)) {
//            tidalAbilityOne.activate(player);
            player.sendMessage("Activated tidal2");
        }


        if (!bladeManager.isTidalBlade(hand) && !bladeManager.isBloomBlade(hand) && !bladeManager.isFlameBlade(hand) && !bladeManager.isStormBlade(hand) && !bladeManager.isFortuneBlade(hand) && !bladeManager.isPulseBlade(hand) && !bladeManager.isFrostBlade(hand)) {
            player.sendMessage("§aYou must be holding a blade to use an ability.");
        }

        return true;
    }
}
