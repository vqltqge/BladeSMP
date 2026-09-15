package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneExperienceListener implements Listener {
    private final BladeManager bladeManager;
    private final PassiveManager passiveManager;

    public FortuneExperienceListener(BladeManager bladeManager, PassiveManager passiveManager) {
        this.bladeManager = bladeManager;
        this.passiveManager = passiveManager;
    }

    @EventHandler
    public void onExperienceGain(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        int num = event.getAmount();

        event.setAmount(num * 2);
    }
}
