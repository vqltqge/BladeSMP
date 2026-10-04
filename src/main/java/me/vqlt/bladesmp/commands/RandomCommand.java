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
    private final RandomBladeManager randomBladeManager;
    private final BladeSMP plugin;
    private boolean rolling = false;

    public RandomCommand(RandomBladeManager randomBladeManager, BladeSMP plugin) {
        this.randomBladeManager = randomBladeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (rolling) {
            sender.sendMessage("§cA random blade roll is already in progress.");
            return true;
        }

        Player target;


        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("§cThat player is not online.");
                return true;
            }

            rolling = true;

            sender.sendMessage("§aRolling a random blade for " + target.getName());
            randomBladeManager.rollBlade(target);
        } else if (args.length > 1) {
            sender.sendMessage("§cUsage: /random [player]");
            return true;
        } else {
            rolling = true;
            sender.sendMessage("§aRolling a random blade for everyone");
            for (Player player : Bukkit.getOnlinePlayers()) {
                randomBladeManager.rollBlade(player);
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            rolling = false;
        }, 105L);

        return true;
    }
}
