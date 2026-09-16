package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ConfigCommand implements CommandExecutor {

    private final BladeSMP plugin;

    public ConfigCommand(BladeSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        sender.sendMessage("§6----- BladeSMP Config -----");

        for (String key : plugin.getConfig().getKeys(true)) {

            // Don't show section headings
            if (plugin.getConfig().isConfigurationSection(key)) {
                continue;
            }

            Object value = plugin.getConfig().get(key);

            sender.sendMessage("§e" + key + "§7: §f" + value);
        }

        sender.sendMessage("§6-------------------------");

        return true;
    }
}
