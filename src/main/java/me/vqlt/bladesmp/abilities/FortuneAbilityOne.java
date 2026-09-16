package me.vqlt.bladesmp.abilities;

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
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final Set<UUID> conservationPlayers = new HashSet<>();

    public Set<UUID> getConservationPlayers() {
        return conservationPlayers;
    }

    private final long cooldown;
    private final long duration;

    private static final TextColor FORTUNE_COLOR = TextColor.fromHexString("#FFD700");

    public FortuneAbilityOne(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("fortune.ability-one.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("fortune.ability-one.duration", 15) * 1000L;

    }

    public void activate(Player player) {
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

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!(bladeManager.isFortuneBlade(hand))) {
            return;
        }

        conservationPlayers.add(id);

        durationManager.startDuration(id, "fortuneone", duration);

        player.sendMessage(Component.text("♣ Conservation activated").color(FORTUNE_COLOR));
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.6f, 1.5f);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.25f);

        durationManager.runAfter((duration / 1000), () -> {
            conservationPlayers.remove(id);
            player.sendMessage(Component.text("♣ Conservation on cooldown").color(FORTUNE_COLOR));
            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
            cooldownManager.startCooldown(id, "fortuneone", cooldown);
        });
    }
}
