package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import org.bukkit.entity.Player;

public class PulseAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double upwardVelocity;
    private final double downardVelocity;
    private final double damage;
    private final double radius;
    private final double knockback;

    public PulseAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("pulse.ability-two.cooldown", 60) * 1000L;
        this.upwardVelocity = plugin.getConfig().getDouble("pulse.ability-two.upwardVelocity", 1.5);
        this.downardVelocity = plugin.getConfig().getDouble("pulse.ability-two.downwardVelocity", 1.5);
        this.damage = plugin.getConfig().getDouble("pulse.ability-two.damage", 6);
        this.radius = plugin.getConfig().getDouble("pulse.ability-two.radius", 3);
        this.knockback = plugin.getConfig().getDouble("pulse.ability-two.knockback", 1.2);
    }

    public void activate(Player player) {
        // hi
    }
}
