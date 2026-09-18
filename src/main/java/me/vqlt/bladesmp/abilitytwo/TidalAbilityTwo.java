package me.vqlt.bladesmp.abilitytwo;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.managers.BladeManager;
import me.vqlt.bladesmp.managers.CooldownManager;
import me.vqlt.bladesmp.managers.DurationManager;
import org.bukkit.entity.Player;

public class TidalAbilityTwo {
    private final BladeManager bladeManager;
    private final CooldownManager cooldownManager;
    private final DurationManager durationManager;
    private final BladeSMP plugin;

    private final long cooldown;
    private final double radius;
    private final double pullVelocity;
    private final double knockback;
    private final double damage;

    public TidalAbilityTwo(BladeManager bladeManager, CooldownManager cooldownManager, DurationManager durationManager, BladeSMP plugin) {
        this.bladeManager = bladeManager;
        this.cooldownManager = cooldownManager;
        this.durationManager = durationManager;
        this.plugin = plugin;

        this.cooldown = plugin.getConfig().getLong("tidal.ability-two.cooldown", 60) * 1000L;
        this.radius = plugin.getConfig().getDouble("tidal.ability-two.radius", 3);
        this.knockback = plugin.getConfig().getDouble("tidal.ability-two.knockback", 2);
        this.damage = plugin.getConfig().getDouble("tidal.ability-two.knockback", 8);
        this.pullVelocity = plugin.getConfig().getDouble("tidal.ability-two.pull-velocity", 1);
    }

    public void activate(Player player) {

    }
}
