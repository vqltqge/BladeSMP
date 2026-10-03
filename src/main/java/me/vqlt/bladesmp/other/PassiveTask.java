package me.vqlt.bladesmp.other;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PassiveTask {

    private final BladeSMP plugin;
    private final PassiveManager passiveManager;
    private final BladeManager bladeManager;

    public PassiveTask(BladeSMP plugin, PassiveManager passiveManager, BladeManager bladeManager) {
        this.plugin = plugin;
        this.passiveManager = passiveManager;
        this.bladeManager = bladeManager;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack hand = player.getInventory().getItemInMainHand();

                if (!(bladeManager.isFlameBlade(hand))) {
                    continue;
                }

                if (bladeManager.isFlameBlade(hand)) {
                    passiveManager.flamePassive(player);
                }
            }
        }, 0L, 20L);
    }
}
