package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

public class PlayerDeathListener implements Listener {
    private final BladeSMP plugin;
    private final BladeManager bladeManager;

    public PlayerDeathListener(BladeSMP plugin, BladeManager bladeManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();

        Iterator<ItemStack> iterator = drops.iterator();

        while (iterator.hasNext()) {
            ItemStack item = iterator.next();

            if (bladeManager.isBladeChooser(item)) {
                if (plugin.getConfig().getBoolean("keep-chooser-on-death", false)) {
                    iterator.remove();
                }

            } else if (bladeManager.isRandomiser(item)) {
                if (plugin.getConfig().getBoolean("keep-randomiser-on-death", false)) {
                    iterator.remove();
                }

            } else if (bladeManager.isAnyBlade(item)) {
                if (plugin.getConfig().getBoolean("keep-blade-on-death", false)) {
                    iterator.remove();
                }
            }
        }
    }
}
