package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import org.bukkit.entity.Player;

public class StormAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final long duration;
    private final double radius;
    private final int strikeInterval;
    private final int regenAmplifier;
    private final int slownessAmplifier;
    private final int weaknessAmplifier;

    public StormAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("storm.ability-two.cooldown", 60) * 1000L;
        this.duration = plugin.getConfig().getLong("storm.ability-two.duration", 10) * 1000L;
        this.radius = plugin.getConfig().getDouble("storm.ability-two.radius", 10);
        this.strikeInterval = plugin.getConfig().getInt("storm.ability-two.strike-interval", 5);
        this.regenAmplifier = plugin.getConfig().getInt("storm.ability-two.regen-amplifier", 1);
        this.slownessAmplifier = plugin.getConfig().getInt("storm.ability-two.slowness-amplifier", 0);
        this.weaknessAmplifier = plugin.getConfig().getInt("storm.ability-two.weakness-amplifier", 0);
    }

    public void activate(Player player) {
        // hi
    }
}
