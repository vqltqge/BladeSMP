package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// This class is seemingly unneeded - can be deleted
public class AbilityManager {
    private final BladeSMP plugin;
    private final BladeManager bladeManager;

    public AbilityManager(BladeSMP plugin, BladeManager bladeManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
    }

    public void activateAbilityOne(Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isTidalBlade(hand)) && !(bladeManager.isBloomBlade(hand)) && !(bladeManager.isFlameBlade(hand)) && !(bladeManager.isStormBlade(hand)) && !(bladeManager.isFrostBlade(hand)) && !(bladeManager.isFortuneBlade(hand)) && bladeManager.isPulseBlade(hand)) {
            return;
        }

        if (bladeManager.isStormBlade(hand)) {

        }
    }
}
