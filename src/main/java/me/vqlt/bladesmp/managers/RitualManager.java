package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.rituals.Ritual;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RitualManager {

    private final BladeSMP plugin;

    private final Map<String, Ritual> activeRituals = new HashMap<>();

    public RitualManager(BladeSMP plugin) {
        this.plugin = plugin;
    }

    public boolean startRitual(Location location, ItemStack blade,
                               Runnable onComplete) {

        if (location == null || location.getWorld() == null) {
            return false;
        }

        String key = getLocationKey(location);

        // Prevent multiple rituals at the same location
        if (activeRituals.containsKey(key)) {
            return false;
        }

        World world = location.getWorld();

        // Get configuration
        int duration = plugin.getConfig().getInt("rituals.duration", 60);

        // Spawn invisible Ender Crystal
        Location beamLocation = location.clone();

        EnderCrystal beam = world.spawn(
                beamLocation,
                EnderCrystal.class
        );

        beam.setInvisible(true);
        beam.setInvulnerable(true);
        beam.setShowingBottom(false);

        // Set beam target
        int height = plugin.getConfig().getInt("rituals.beam.height", 15);

        beam.setBeamTarget(
                location.clone().add(0, height, 0)
        );

        // Create boss bar
        String title = plugin.getConfig()
                .getString("rituals.boss-bar.title", "&5Blade Ritual");

        String colorName = plugin.getConfig()
                .getString("rituals.boss-bar.color", "PURPLE");

        BarColor color;

        try {
            color = BarColor.valueOf(colorName.toUpperCase());
        } catch (IllegalArgumentException exception) {
            color = BarColor.PURPLE;
        }

        BossBar bossBar = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', title),
                color,
                BarStyle.SOLID
        );

        // Add nearby players to boss bar
        double radius = 32;

        for (Player player : world.getPlayers()) {
            if (player.getLocation().distanceSquared(location) <= radius * radius) {
                bossBar.addPlayer(player);
            }
        }

        // Create ritual object
        Ritual ritual = new Ritual(
                location,
                blade,
                beam,
                bossBar,
                duration,
                onComplete
        );

        // Track active ritual
        activeRituals.put(key, ritual);

        return true;
    }

    private String getLocationKey(Location location) {

        return location.getWorld().getUID() + ":"
                + location.getBlockX() + ":"
                + location.getBlockY() + ":"
                + location.getBlockZ();
    }
}
