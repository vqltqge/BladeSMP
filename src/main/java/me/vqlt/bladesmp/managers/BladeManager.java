package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BladeManager {

    private final BladeSMP plugin;
    private final NamespacedKey bladeKey;

    public BladeManager(BladeSMP plugin) {
        this.plugin = plugin;
        this.bladeKey = new NamespacedKey(plugin, "custom_blade");
    }

    // Create the Flame Blade
    public ItemStack createFlameBlade() {
        ItemStack flameBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta flameBladeMeta = flameBlade.getItemMeta();
        PersistentDataContainer container = flameBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "flameBlade");
        flameBladeMeta.displayName(Component.text("Flame Blade").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        flameBladeMeta.lore(List.of(Component.text("Aura flame quote").color(NamedTextColor.WHITE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("◆ PASSIVE").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
                Component.text("- Fire Resistance").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("✦ ABILITY").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
                Component.text("- Flame Sweep").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                Component.text("- Inferno").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
        ));

        flameBlade.setItemMeta(flameBladeMeta);
        return flameBlade;
    }


    public ItemStack createFrostBlade() {
        ItemStack frostBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta frostBladeMeta = frostBlade.getItemMeta();
        PersistentDataContainer container = frostBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "frostBlade");
        frostBladeMeta.displayName(Component.text("Frost Blade"));
        frostBlade.setItemMeta(frostBladeMeta);
        return frostBlade;
    }

    public ItemStack createTidalBlade() {
        ItemStack tidalBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta tidalBladeMeta = tidalBlade.getItemMeta();
        PersistentDataContainer container = tidalBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "tidalBlade");
        tidalBladeMeta.displayName(Component.text("Tidal Blade"));
        tidalBlade.setItemMeta(tidalBladeMeta);
        return tidalBlade;
    }

    public ItemStack createBloomBlade() {
        ItemStack bloomBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta bloomBladeMeta = bloomBlade.getItemMeta();
        PersistentDataContainer container = bloomBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "bloomBlade");
        bloomBladeMeta.displayName(Component.text("Bloom Blade"));
        bloomBlade.setItemMeta(bloomBladeMeta);
        return bloomBlade;
    }

    public ItemStack createPulseBlade() {
        ItemStack pulseBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta pulseBladeMeta = pulseBlade.getItemMeta();
        PersistentDataContainer container = pulseBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "pulseBlade");
        pulseBladeMeta.displayName(Component.text("Pulse Blade"));
        pulseBlade.setItemMeta(pulseBladeMeta);
        return pulseBlade;
    }

    public ItemStack createStormBlade() {
        ItemStack stormBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta stormBladeMeta = stormBlade.getItemMeta();
        PersistentDataContainer container = stormBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "stormBlade");
        stormBladeMeta.displayName(Component.text("Storm Blade"));
        stormBlade.setItemMeta(stormBladeMeta);
        return stormBlade;
    }

    public ItemStack createFortuneBlade() {
        ItemStack fortuneBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta fortuneBladeMeta = fortuneBlade.getItemMeta();
        PersistentDataContainer container = fortuneBladeMeta.getPersistentDataContainer();
        container.set(bladeKey, PersistentDataType.STRING, "fortuneBlade");
        fortuneBladeMeta.displayName(Component.text("Fortune Blade"));
        fortuneBlade.setItemMeta(fortuneBladeMeta);
        return fortuneBlade;
    }

    public boolean isPulseBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return "pulseBlade".equals(bladeType);
    }

    public boolean isFlameBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return "flameBlade".equals(bladeType);
    }

    public boolean isFrostBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return "frostBlade".equals(bladeType);
    }

    public boolean isTidalBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return "tidalBlade".equals(bladeType);
    }

    public boolean isBloomBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return "bloomBlade".equals(bladeType);
    }

    public boolean isStormBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return "stormBlade".equals(bladeType);
    }

    public boolean isFortuneBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return ("fortuneBlade").equals(bladeType);
    }
}


