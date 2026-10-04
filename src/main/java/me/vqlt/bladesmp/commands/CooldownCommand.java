package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.managers.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CooldownCommand implements CommandExecutor {
    private final CooldownManager cooldownManager;

    public CooldownCommand(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        Player target;

        if (args.length > 1) {
            sender.sendMessage("§cUsage: /cooldown [player]");

            return true;
        } else if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("§cThat player is not online.");

                return true;
            }
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cConsole usage: /cooldown <player>");
                return true;
            }

            target = player;
        }

        cooldownManager.clearCooldowns(target);

        sender.sendMessage("§aCleared all cooldowns for §f" + target.getName() + "§a.");

        if (!sender.equals(target)) {
            target.sendMessage("§aYour ability cooldowns were cleared.");
        }

        return true;

    }

}
