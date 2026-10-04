package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.abilityone.FrostAbilityOne;
import me.vqlt.bladesmp.abilitytwo.FrostAbilityTwo;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FrostMoveListener implements Listener {
    private final FrostAbilityOne frostAbilityOne;
    private final FrostAbilityTwo frostAbilityTwo;
    private final PassiveManager passiveManager;

    public FrostMoveListener(FrostAbilityOne frostAbilityOne, FrostAbilityTwo frostAbilityTwo, PassiveManager passiveManager) {
        this.frostAbilityOne = frostAbilityOne;
        this.frostAbilityTwo = frostAbilityTwo;
        this.passiveManager = passiveManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (passiveManager.getPassiveFreezePlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }

        if (frostAbilityOne.getFreezePlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }

        if (frostAbilityTwo.getFreezePlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
