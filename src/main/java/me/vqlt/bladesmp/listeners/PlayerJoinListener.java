package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final BladeManager bladeManager;
    private final BladeSMP plugin;

    public PlayerJoinListener(BladeManager bladeManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            if (plugin.getConfig().getBoolean("random-first-join-blade", false)) {
                player.getInventory().addItem(bladeManager.createBladeRandomiser());
            }

            if (plugin.getConfig().getBoolean("first-join-blade-selector", false)) {
                player.getInventory().addItem(bladeManager.createBladeChooser());
            }
        }
    }

}