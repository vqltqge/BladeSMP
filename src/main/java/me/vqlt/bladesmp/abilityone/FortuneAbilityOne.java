package me.vqlt.bladesmp.abilityone;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FortuneAbilityOne {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    // A set that stores which players have the ability active
    private final Set<UUID> conservationPlayers = new HashSet<>();

    // A getter for the set
    public Set<UUID> getConservationPlayers() {
        return conservationPlayers;
    }

    // Variables for the config values
    private final long cooldown;
    private final long duration;

    // Variable for the colour for the messages
    private static final TextColor FORTUNE_COLOR = TextColor.fromHexString("#FFD700");

    public FortuneAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("fortune.ability-one.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("fortune.ability-one.duration", 15) * 1000L;

    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        if (cooldownManager.isOnCooldown(id, "fortuneone")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "fortuneone") / 1000.0);
            player.sendMessage(Component.text("♣ Conservation is on cooldown for " + seconds + "s").color(FORTUNE_COLOR));
            return;
        }

        if (durationManager.isActive(id, "fortuneone")) {
            player.sendMessage(Component.text("♣ Conservation is already active").color(FORTUNE_COLOR));
            return;
        }

        // Get the item the player is holding and return if it is not the fortune blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        // Adds the player to the set of players with the ability active
        conservationPlayers.add(id);

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        durationManager.startDuration(id, "fortuneone", duration);
        player.sendMessage(Component.text("♣ Conservation activated").color(FORTUNE_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.6f, 1.5f);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.25f);

        // Schedule the following: remove the player from the conservation players set, and plays the deactivation sound, tell them their ability is on cooldown and start their cooldown
        durationManager.runAfter((duration / 1000), () -> {
            conservationPlayers.remove(id);
            player.sendMessage(Component.text("♣ Conservation on cooldown").color(FORTUNE_COLOR));
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
            cooldownManager.startCooldown(id, "fortuneone", cooldown);
        });
    }
}
