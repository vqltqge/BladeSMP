package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.abilities.FrostAbilityOne;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FrostMoveListener implements Listener {
    private final FrostAbilityOne frostAbilityOne;

    public FrostMoveListener(FrostAbilityOne frostAbilityOne) {
        this.frostAbilityOne = frostAbilityOne;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (frostAbilityOne.getFreezePlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }


    }
}
