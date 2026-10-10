package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class FortuneAbilityTwo implements Listener {
    // Variables for the classes with the methods needed for the blades abilities
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final double maxDamageMultiplier;
    private final long chargeDuration;

    // Variables for the config values
    private static final TextColor FORTUNE_COLOR = TextColor.fromHexString("#FFD700");

    // Set to store armed players
    private final Set<UUID> armedPlayers = new HashSet<>();

    public FortuneAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        // Fetches the config values and stores them in variables
        this.cooldown = plugin.getConfig().getLong("fortune.ability-two.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("fortune.ability-two.duration", 15) * 1000L;
        this.maxDamageMultiplier = plugin.getConfig().getDouble("fortune.ability-two.max-damage", 1.3);
        this.chargeDuration = plugin.getConfig().getLong("fortune.ability-two.charge-duration", 60) * 1000L;
    }

    public void activate(Player player) {
        // Fetches the players UUID, and check if their ability is on cooldown or already active
        UUID id = player.getUniqueId();

        // Get the item the player is holding and return if it is not the fortune blade
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        if (armedPlayers.contains(id)) {
            player.sendMessage(Component.text("♣ Lucky Strike is already charged").color(FORTUNE_COLOR));
            return;
        }

        if (cooldownManager.isOnCooldown(id, "fortunetwo")) {
            int seconds = (int) Math.ceil(cooldownManager.getRemainingMillis(id, "fortunetwo") / 1000.0);
            player.sendMessage(Component.text("♣ Lucky Strike is on cooldown for " + seconds + "s").color(FORTUNE_COLOR));
            return;
        }

        if (durationManager.isActive(id, "fortunetwo")) {
            player.sendMessage(Component.text("♣ Lucky Strike is already active").color(FORTUNE_COLOR));
            return;
        }

        // Adds the players to the armed players set and sends the player a message saying their ability is charged. Play an activation sound at the players location
        player.sendMessage(Component.text("♣ Lucky Strike charged").color(FORTUNE_COLOR));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.5F, 1.5F);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.25F, 2);
        armedPlayers.add(id);

        // Removes the players from the armed players set after the charge duration
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (armedPlayers.remove(id)) {
                player.sendMessage(Component.text("♣ Lucky Strike charge expired").color(FORTUNE_COLOR));
                player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.5F, 0.8F);
            }
        }, chargeDuration / 50);
    }

    // Listens for an entity damage by entity event
    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {

        // If the entity that did the damage is not a player, return
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        // If the entity that was damaged is not a player, return
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        // Get the item the player is holding and return if it is not the fortune blade
        UUID id = attacker.getUniqueId();

        ItemStack hand = attacker.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        // Start the abilities duration and tell the player the ability has been activated. Play an activation sound at the players location
        if (armedPlayers.contains(id)) {
            attacker.sendMessage(Component.text("♣ Lucky Strike activated").color(FORTUNE_COLOR));
            durationManager.startDuration(id, "fortunetwo", duration);
            durationManager.runAfter(duration / 1000, () -> startCooldown(id, attacker));
            armedPlayers.remove(id);
        }

        // Get the damage that the player did
        double damage = event.getDamage();

        // Generate a random number from 0 to 1
        Random random = new Random();

        // Calculate the number of levels so that there is a level for each 0.1 (eg if the max was 1.3, there would be 3 levels, if it was 2.5 there would be 15)
        int numberOfLevels = Math.max(1, (int) Math.round((maxDamageMultiplier - 1.1) * 10) + 1);

        // If the ability is still active
        if (durationManager.isActive(id, "fortunetwo")) {
            // calculate the random multiplier
            double multiplier = 1.1 + random.nextInt(numberOfLevels) * 0.1;
            // set the damage to the damage * multiplier
            event.setDamage(event.getDamage() * multiplier);
        }
    }

    public void startCooldown(UUID id, Player player) {
        // Start the ability cooldown, play a sound and send the player a message
        cooldownManager.startCooldown(id, "fortunetwo", cooldown);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        player.sendMessage(Component.text("♣ Lucky Strike on cooldown").color(FORTUNE_COLOR));
    }
}
