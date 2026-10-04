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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final double maxDamageMultiplier;
    private final long chargeDuration;

    private static final TextColor FORTUNE_COLOR = TextColor.fromHexString("#FFD700");

    private final Set<UUID> armedPlayers = new HashSet<>();

    public FortuneAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("fortune.ability-two.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("fortune.ability-two.duration", 15) * 1000L;
        this.maxDamageMultiplier = plugin.getConfig().getDouble("fortune.ability-two.max-damage", 1.5);
        this.chargeDuration = plugin.getConfig().getLong("fortune.ability-two.charge-duration", 60) * 1000L;
    }

    public void activate(Player player) {
        UUID id = player.getUniqueId();

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

        player.sendMessage(Component.text("♣ Lucky Strike charged").color(FORTUNE_COLOR));
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.5F, 1.5F);
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.25F, 2);
        armedPlayers.add(id);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (armedPlayers.remove(id)) {
                player.sendMessage(Component.text("♣ Lucky Strike charge expired").color(FORTUNE_COLOR));
                player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.5F, 0.8F);
            }
        }, chargeDuration / 50);
    }

    @EventHandler
    public void onMeleeHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        UUID id = attacker.getUniqueId();

        ItemStack hand = attacker.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        if (armedPlayers.contains(id)) {
            attacker.sendMessage(Component.text("♣ Lucky Strike activated").color(FORTUNE_COLOR));
            durationManager.startDuration(id, "fortunetwo", duration);
            durationManager.runAfter(duration / 1000, () -> startCooldown(id, attacker));
            armedPlayers.remove(id);
        }

        double damage = event.getDamage();

        Random random = new Random();

        int numberOfLevels = Math.max(1, (int) Math.round((maxDamageMultiplier - 1.1) * 10) + 1);

        if (durationManager.isActive(id, "fortunetwo")) {
            double multiplier = 1.1 + random.nextInt(numberOfLevels) * 0.1;
            event.setDamage(event.getDamage() * multiplier);
        }
    }

    public void startCooldown(UUID id, Player player) {
        cooldownManager.startCooldown(id, "fortunetwo", cooldown);
        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
        player.sendMessage(Component.text("♣ Lucky Strike on cooldown").color(FORTUNE_COLOR));
    }
}
