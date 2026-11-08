package me.vqlt.bladesmp.commands;

import me.vqlt.bladesmp.managers.TrustManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TrustCommand implements CommandExecutor, TabCompleter {

    private final TrustManager trustManager;

    public TrustCommand(TrustManager trustManager) {
        this.trustManager = trustManager;
    }

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
                Set<UUID> trustedPlayers = trustManager.getTrustedPlayers(player.getUniqueId());

                if (trustedPlayers.isEmpty()) {
                    player.sendMessage("§cYou haven't trusted anyone.");
                    return true;
                }

                player.sendMessage("§6§lTrusted Players:");

                for (UUID uuid : trustedPlayers) {
                    OfflinePlayer trustedPlayer = Bukkit.getOfflinePlayer(uuid);

                    player.sendMessage("§7- §f" + trustedPlayer.getName());
                }

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
                if (!trustManager.trustPlayer(player.getUniqueId(), target.getUniqueId())) {
                    if (player.getUniqueId().equals(target.getUniqueId())) {
                        player.sendMessage("§cYou cannot trust yourself.");
                    } else {
                        player.sendMessage("§cThat player is already trusted..");
                    }

                } else {
                    player.sendMessage("§aTrusted " + target.getName());
                }

            } else if (args[0].equals("remove")) {
                target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage("§cThat player is not online.");
                    return true;
                }

                // Remove that player from trust list
                if (!trustManager.untrustPlayer(player.getUniqueId(), target.getUniqueId())) {
                    if (player.getUniqueId().equals(target.getUniqueId())) {
                        player.sendMessage("§cYou cannot untrust yourself.");
                    } else {
                        player.sendMessage("§cThat player is not trusted.");
                    }

                } else {
                    player.sendMessage("§aUntrusted " + target.getName());
                }

            } else {
                sender.sendMessage("§cUsage: /trust <add/remove/list> <player>");
                return true;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("list");

        } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        String input = args[args.length - 1].toLowerCase();

        return completions.stream().filter(s -> s.toLowerCase().startsWith(input)).collect(Collectors.toList());
    }
}