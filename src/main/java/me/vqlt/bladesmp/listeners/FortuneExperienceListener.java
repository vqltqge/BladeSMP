package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.PassiveManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneExperienceListener implements Listener {
    private final BladeSMP plugin;
    private final BladeManager bladeManager;
    private final PassiveManager passiveManager;

    private final double xpMultiplier;

    public FortuneExperienceListener(BladeSMP plugin, BladeManager bladeManager, PassiveManager passiveManager) {
        this.plugin = plugin;
        this.bladeManager = bladeManager;
        this.passiveManager = passiveManager;

        this.xpMultiplier = plugin.getConfig().getDouble("fortune.passive.xp-multiplier", 2);
    }

    @EventHandler
    public void onExperienceGain(PlayerExpChangeEvent event) {
        if (!plugin.getConfig().getBoolean("storm.passive.enabled", true)) {
            return;
        }

        // Get the player that gained the experience
        Player player = event.getPlayer();

        // If the player is not holding the fortune blade, return
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        // Get the amount of xp that the player gained
        int num = event.getAmount();

        // Multiply the amount of xp they will gain by the xp multiplier, and round it to the nearest integer
        event.setAmount((int) Math.round(num * xpMultiplier));
    }
}
