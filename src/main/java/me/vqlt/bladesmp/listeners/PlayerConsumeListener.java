package me.vqlt.bladesmp.listeners;

import me.vqlt.bladesmp.BladeSMP;
import me.vqlt.bladesmp.abilityone.FortuneAbilityOne;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerConsumeListener implements Listener {
    private final FortuneAbilityOne fortuneAbilityOne;
    private final BladeSMP plugin;

    // Stores the types of food the ability works on
    private final Set<Material> items = new HashSet<>();

    public PlayerConsumeListener(FortuneAbilityOne fortuneAbilityOne, BladeSMP plugin) {
        this.fortuneAbilityOne = fortuneAbilityOne;
        this.plugin = plugin;

        // When the class is created load the config values for the foods
        loadItems();
    }

    private void loadItems() {
        // Clears the set
        items.clear();

        // Gets a list of the names of all the foods that work
        List<String> itemNames = plugin.getConfig().getStringList("fortune.ability-one.items");

        // If the config value isn't there, use the default
        if (itemNames.isEmpty()) {
            itemNames = List.of("APPLE", "BAKED_POTATO", "BEEF", "BEETROOT", "BEETROOT_SOUP", "BREAD", "CARROT", "CHICKEN", "CHORUS_FRUIT", "COD", "COOKED_BEEF", "COOKED_CHICKEN", "COOKED_COD", "COOKED_MUTTON", "COOKED_PORKCHOP", "COOKED_RABBIT", "COOKED_SALMON", "COOKIE", "DRIED_KELP", "GLOW_BERRIES", "GOLDEN_APPLE", "GOLDEN_CARROT", "HONEY_BOTTLE", "MELON_SLICE", "MUSHROOM_STEW", "MUTTON", "POISONOUS_POTATO", "PORKCHOP", "POTATO", "PUFFERFISH", "PUMPKIN_PIE", "RABBIT", "RABBIT_STEW", "ROTTEN_FLESH", "SALMON", "SPIDER_EYE", "SUSPICIOUS_STEW", "SWEET_BERRIES", "TROPICAL_FISH");
        }

        // For every food in the config list
        for (String name : itemNames) {
            // Matches the material the user inputted to its Material value
            Material material = Material.matchMaterial(name);

            // If that material doesn't exist, send a warning in the config and skip it
            if (material == null) {
                plugin.getLogger().warning("Invalid Conservation Item: " + name);
                continue;
            }

            // Add the food to the list of foods the conservation works on
            items.add(material);
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        UUID id = player.getUniqueId();

        // If the player is in the list of players that have fortune ability one active
        if (fortuneAbilityOne.getConservationPlayers().contains(id)) {
            // Get the item they consumed, and if it isn't in the list of conservation food items return
            ItemStack item = event.getItem().asOne();
            if (!items.contains(item.getType())) {
                return;
            }

            // After one tick
            Bukkit.getScheduler().runTask(plugin, () -> {
                // If the hand they used to consume the food is their offhand
                if (event.getHand() == EquipmentSlot.OFF_HAND) {
                    ItemStack offhand = player.getInventory().getItemInOffHand();

                    // If the item in the offhand is the same as the food they consumed
                    if (offhand.isSimilar(item)) {
                        // Increase the existing stack by one
                        offhand.setAmount(offhand.getAmount() + 1);
                    } else {
                        // If they aren't the same item, just add it to their inventory
                        player.getInventory().addItem(item);
                    }
                } else {
                    // If the player consumed the food in their main hand, add the food item to their inventory
                    player.getInventory().addItem(item);
                }
            });
        }
    }
}
