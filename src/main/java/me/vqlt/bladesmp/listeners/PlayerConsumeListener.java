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

    private final Set<Material> items = new HashSet<>();

    public PlayerConsumeListener(FortuneAbilityOne fortuneAbilityOne, BladeSMP plugin) {
        this.fortuneAbilityOne = fortuneAbilityOne;
        this.plugin = plugin;

        loadItems();
    }

    private void loadItems() {
        items.clear();

        List<String> itemNames = plugin.getConfig().getStringList("fortune.ability-one.items");

        if (itemNames.isEmpty()) {
            itemNames = List.of("APPLE", "BAKED_POTATO", "BEEF", "BEETROOT", "BEETROOT_SOUP", "BREAD", "CARROT", "CHICKEN", "CHORUS_FRUIT", "COD", "COOKED_BEEF", "COOKED_CHICKEN", "COOKED_COD", "COOKED_MUTTON", "COOKED_PORKCHOP", "COOKED_RABBIT", "COOKED_SALMON", "COOKIE", "DRIED_KELP", "GLOW_BERRIES", "GOLDEN_APPLE", "GOLDEN_CARROT", "HONEY_BOTTLE", "MELON_SLICE", "MUSHROOM_STEW", "MUTTON", "POISONOUS_POTATO", "PORKCHOP", "POTATO", "PUFFERFISH", "PUMPKIN_PIE", "RABBIT", "RABBIT_STEW", "ROTTEN_FLESH", "SALMON", "SPIDER_EYE", "SUSPICIOUS_STEW", "SWEET_BERRIES", "TROPICAL_FISH");
        }

        for (String name : itemNames) {
            Material material = Material.matchMaterial(name);

            if (material == null) {
                plugin.getLogger().warning("Invalid Conservation Item: " + name);
                continue;
            }

            items.add(material);
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        UUID id = player.getUniqueId();

        if (fortuneAbilityOne.getConservationPlayers().contains(id)) {
            ItemStack item = event.getItem().asOne();
            if (!items.contains(item.getType())) {
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (event.getHand() == EquipmentSlot.OFF_HAND) {
                    ItemStack offhand = player.getInventory().getItemInOffHand();

                    if (offhand.isSimilar(item)) {
                        offhand.setAmount(offhand.getAmount() + 1);
                    } else {
                        player.getInventory().setItemInOffHand(item);
                    }
                } else {
                    player.getInventory().addItem(item);
                }
            });
        }
    }
}
