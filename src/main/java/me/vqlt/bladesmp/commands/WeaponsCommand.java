package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.other.WeaponsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WeaponsCommand implements CommandExecutor {

    private final BladeManager bladeManager;

    public WeaponsCommand(BladeManager bladeManager) {
        this.bladeManager = bladeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        WeaponsGUI gui = new WeaponsGUI(bladeManager);
        player.openInventory(gui.getInventory());

        return true;
    }
}
