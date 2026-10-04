package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RandomBladeManager {
    private final BladeSMP plugin;
    private final BladeManager bladeManager;
    private final Random random = new Random();
    private final List<ItemStack> blades;
    private final Set<UUID> rollingPlayers = new HashSet<>();

    public RandomBladeManager(BladeSMP plugin, BladeManager bladeManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;

        this.blades = List.of(bladeManager.createBloomBlade(), bladeManager.createFlameBlade(), bladeManager.createFortuneBlade(), bladeManager.createFrostBlade(), bladeManager.createPulseBlade());
    }

    public void rollBlade(Player player) {
        ItemStack winner = blades.get(random.nextInt(blades.size())).clone();

        rollingPlayers.add(player.getUniqueId());
        int slot = player.getInventory().firstEmpty();

        if (slot == -1) {
            slot = player.getInventory().getHeldItemSlot();

            ItemStack droppedItem =
                    player.getInventory().getItem(slot);

            if (droppedItem != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), droppedItem);

                player.getInventory().setItem(slot, null);
            }
        }

        roll(player, slot, winner, 0);
    }

    private void roll(Player player, int slot, ItemStack winner, int rolls) {
        if (rolls >= 10) {
            player.getInventory().setItem(slot, winner);
            rollingPlayers.remove(player.getUniqueId());

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

            return;
        }

        ItemStack displayBlade = blades.get(random.nextInt(blades.size()));

        player.getInventory().setItem(slot, displayBlade);

        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5f, 1.0f);

        Bukkit.getScheduler().runTaskLater(plugin, () -> roll(player, slot, winner, rolls + 1), 10L);
    }

    public boolean isRolling(Player player) {
        return rollingPlayers.contains(player.getUniqueId());
    }
}
