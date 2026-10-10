package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.managers.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CooldownCommand implements CommandExecutor {
    // Variables for the classes with the methods needed
    private final CooldownManager cooldownManager;

    public CooldownCommand(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // If the command was not sent by an operator, return
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        // Define the target without setting a value to it
        Player target;

        if (args.length > 1) {
            // If there are two or more arguments, return
            sender.sendMessage("§cUsage: /cooldown [player]");

            return true;
        } else if (args.length == 1) {
            // Get the player in the first argument
            target = Bukkit.getPlayer(args[0]);

            // If it is not a player, return
            if (target == null) {
                sender.sendMessage("§cThat player is not online.");

                return true;
            }
        } else {
            // If the console sent the command with zero arguments, then return as they have not specified a target
            // If the player sender sent the command with zero arguments, assume the target as the sender
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cConsole usage: /cooldown <player>");
                return true;
            }

            target = player;
        }

        // Clear the cooldowns of that player and send a message to the sender
        cooldownManager.clearCooldowns(target);

        sender.sendMessage("§aCleared all cooldowns for §f" + target.getName() + "§a.");

        // If the sender is not the same player as the target, tell the target
        if (!sender.equals(target)) {
            target.sendMessage("§aYour ability cooldowns were cleared.");
        }

        return true;

    }

}
