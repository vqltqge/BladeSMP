package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ActionBarManager {

    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeManager bladeManager;
    private final BladeSMP plugin;

    private static final TextColor FLAME_COLOR = TextColor.fromHexString("#FF7A2F");
    private static final TextColor FROST_COLOR = TextColor.fromHexString("#6EE7FF");
    private static final TextColor BLOOM_COLOR = TextColor.fromHexString("#F50CAB");
    private static final TextColor STORM_COLOR = TextColor.fromHexString("#FFE44D");
    private static final TextColor TIDAL_COLOR = TextColor.fromHexString("#20BFFF");
    private static final TextColor PULSE_COLOR = TextColor.fromHexString("#A855F7");
    private static final TextColor FORTUNE_COLOR = TextColor.fromHexString("#FFD700");

    private static final TextColor READY_COLOR = TextColor.fromHexString("#00ff00");
    private static final TextColor ACTIVE_COLOR = TextColor.fromHexString("#ff0000");
    private static final TextColor COOLDOWN_COLOR = TextColor.color(NamedTextColor.GRAY);

    public ActionBarManager(
            CooldownManager cooldownManager,
            DurationManager durationManager,
            BladeManager bladeManager,
            BladeSMP plugin
    ) {
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.bladeManager = bladeManager;
        this.plugin = plugin;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    ItemStack hand = player.getInventory().getItemInMainHand();

                    if (bladeManager.isFlameBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "🔥 Flame Sweep", "flameone",
                                "🔥 Inferno", "flametwo",
                                FLAME_COLOR
                        );

                    } else if (bladeManager.isFrostBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "❄ Frozen Dash", "frostone",
                                "❄ Frozen Domain", "frosttwo",
                                FROST_COLOR
                        );

                    } else if (bladeManager.isTidalBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "≋ Drowning Field", "tidalone",
                                "≋ Tsunami", "tidaltwo",
                                TIDAL_COLOR
                        );

                    } else if (bladeManager.isBloomBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "❤ Vital Surge", "bloomone",
                                "❤ Lifebind", "bloomtwo",
                                BLOOM_COLOR
                        );

                    } else if (bladeManager.isPulseBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "➜ Velocity", "pulseone",
                                "✦ Shockwave", "pulsetwo",
                                PULSE_COLOR
                        );

                    } else if (bladeManager.isStormBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "⚡ Thunderstorm", "stormone",
                                "⚡ Thunderfield", "stormtwo",
                                STORM_COLOR
                        );

                    } else if (bladeManager.isFortuneBlade(hand)) {
                        sendAbilityBar(
                                player,
                                "★ Conservation", "fortuneone",
                                "★ Lucky Strike", "fortunetwo",
                                FORTUNE_COLOR
                        );
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    private void sendAbilityBar(
            Player player,
            String abilityOneName,
            String abilityOneKey,
            String abilityTwoName,
            String abilityTwoKey,
            TextColor bladeColor
    ) {

        UUID id = player.getUniqueId();

        Component abilityOne = Component.text(abilityOneName + " ")
                .color(bladeColor)
                .append(getAbilityState(id, abilityOneKey));

        Component separator = Component.text("  |  ")
                .color(NamedTextColor.DARK_GRAY);

        Component abilityTwo = Component.text(abilityTwoName + " ")
                .color(bladeColor)
                .append(getAbilityState(id, abilityTwoKey));

        player.sendActionBar(
                abilityOne
                        .append(separator)
                        .append(abilityTwo)
        );
    }

    private Component getAbilityState(UUID id, String abilityKey) {

        if (durationManager.isActive(id, abilityKey)) {
            return Component.text("Active")
                    .color(ACTIVE_COLOR)
                    .decorate(TextDecoration.BOLD);
        }

        if (cooldownManager.isOnCooldown(id, abilityKey)) {
            return Component.text(
                            String.format(
                                    "%.1fs",
                                    cooldownManager.getRemainingMillis(id, abilityKey) / 1000.0
                            )
                    )
                    .color(COOLDOWN_COLOR)
                    .decorate(TextDecoration.BOLD);
        }

        return Component.text("Ready")
                .color(READY_COLOR)
                .decorate(TextDecoration.BOLD);
    }
}