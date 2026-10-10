package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.RandomBladeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RandomCommand implements CommandExecutor {
    // Variables for the classes with the methods needed
    private final RandomBladeManager randomBladeManager;
    private final BladeSMP plugin;

    // No player is currently rolling a new blade, so set it to false
    private boolean rolling = false;

    public RandomCommand(RandomBladeManager randomBladeManager, BladeSMP plugin) {
        this.randomBladeManager = randomBladeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // If the sender is not an operator, return
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        // If a player is already rolling, return
        if (rolling) {
            sender.sendMessage("§cA random blade roll is already in progress.");
            return true;
        }

        // Define the target variable with no value
        Player target;


        if (args.length == 1) {
            // If there is one argument, set that argument as the player that will be rerolled
            target = Bukkit.getPlayer(args[0]);

            // If that player is not online, return
            if (target == null) {
                sender.sendMessage("§cThat player is not online.");
                return true;
            }

            // Start rolling for that player
            rolling = true;

            sender.sendMessage("§aRolling a random blade for " + target.getName());
            randomBladeManager.rollBlade(target);
        } else if (args.length > 1) {
            // If there is two or more arguments, return
            sender.sendMessage("§cUsage: /random [player]");
            return true;
        } else {
            // If there are zero arguments, roll a random blade for everyone
            rolling = true;
            sender.sendMessage("§aRolling a random blade for everyone");
            for (Player player : Bukkit.getOnlinePlayers()) {
                randomBladeManager.rollBlade(player);
            }
        }

        // After 105 ticks, set rolling to false to allow another reroll to happen
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            rolling = false;
        }, 105L);

        return true;
    }
}
