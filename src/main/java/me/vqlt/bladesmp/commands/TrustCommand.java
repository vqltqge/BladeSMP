package me.vqlt.bladesmp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrustCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only");
            return true;
        }

        Player target;

        // Usages:
        // trust add <player>
        // trust remove <player>
        // trust list

        if (args.length > 2) {
            sender.sendMessage("§cUsage: /trust <add/remove/list> <player>");
            return true;
        } else if (args.length < 1) {
            sender.sendMessage("§cUsage: /trust <add/remove/list> <player>");
            return true;
        } else if (args.length == 1) {
            if (args[0].equals("list")) {
                // List trusted players
            } else {
                sender.sendMessage("§cUsage: /trust <add/remove/list> <player>");
                return true;
            }
        } else {
            if (args[0].equals("add")) {
                target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage("§cThat player is not online.");
                    return true;
                }

                // Add that player to trust list
            } else if (args[0].equals("remove")) {
                target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage("§cThat player is not online.");
                    return true;
                }

                // Remove that player from trust list
            } else {
                sender.sendMessage("§cUsage: /trust <add/remove/list> <player>");
                return true;
            }
        }

        return true;
    }
}
