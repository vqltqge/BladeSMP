package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerDeathListener implements Listener {
    private final BladeSMP plugin;
    private final BladeManager bladeManager;

    private final Map<UUID, List<ItemStack>> savedItems = new HashMap<>();

    public PlayerDeathListener(BladeSMP plugin, BladeManager bladeManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        List<ItemStack> saved = new ArrayList<>();
        Iterator<ItemStack> iterator = drops.iterator();

        while (iterator.hasNext()) {

            ItemStack item = iterator.next();

            if (bladeManager.isBladeChooser(item)) {
                if (plugin.getConfig().getBoolean("keep-chooser-on-death", false)) {
                    saved.add(item.clone());
                    iterator.remove();
                }

            } else if (bladeManager.isRandomiser(item)) {
                if (plugin.getConfig().getBoolean("keep-randomiser-on-death", false)) {
                    saved.add(item.clone());
                    iterator.remove();
                }

            } else if (bladeManager.isAnyBlade(item)) {
                if (plugin.getConfig().getBoolean("keep-blade-on-death", false)) {
                    saved.add(item.clone());
                    iterator.remove();
                }
            }
        }

        if (!saved.isEmpty()) {
            savedItems.put(event.getEntity().getUniqueId(), saved);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        List<ItemStack> items = savedItems.remove(uuid);

        if (items != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                Player player = event.getPlayer();

                for (ItemStack item : items) {
                    player.getInventory().addItem(item).values()
                            .forEach(leftover -> player.getWorld()
                                    .dropItemNaturally(player.getLocation(), leftover));
                }
            });
        }
    }

}
