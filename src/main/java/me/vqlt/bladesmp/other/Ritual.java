package me.vqlt.bladesmp.other;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.inventory.ItemStack;

public class Ritual {

    private final Location location;
    private final ItemStack blade;
    private final BossBar bossBar;
    private final EnderCrystal crystal;
    private final Runnable onComplete;

    private int timeRemaining;

    public Ritual(Location location, ItemStack blade,
                  EnderCrystal beam, BossBar bossBar,
                  int duration, Runnable onComplete) {

        this.location = location;
        this.blade = blade;
        this.timeRemaining = duration;
        this.bossBar = bossBar;
        this.crystal = crystal;
        this.onComplete = onComplete;
    }

    public Location getLocation() {
        return location;
    }

    public ItemStack getBlade() {
        return blade;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public EnderCrystal getCrystal() {
        return crystal;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Runnable getOnComplete() {
        return onComplete;
    }
}