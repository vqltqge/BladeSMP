package me.vqlt.bladesmp.managers;

import me.vqlt.bladesmp.BladeSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BladeManager {

    private final BladeSMP plugin;
    private final NamespacedKey bladeKey;

    public BladeManager(BladeSMP plugin) {
        this.plugin = plugin;
        this.bladeKey = new NamespacedKey(plugin, "custom_blade");
    }

    // =========================
    // FLAME BLADE
    // =========================

    public ItemStack createFlameBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "flameBlade"
        );

        meta.displayName(
                Component.text("✦ FLAME BLADE ✦")
                        .color(TextColor.fromHexString("#FF4A1C"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("🔥 PASSIVE • Fireborn")
                        .color(TextColor.fromHexString("#FF7A2F"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Gain Fire Resistance while holding the blade.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Flame Sweep")
                        .color(TextColor.fromHexString("#FF9A3C"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Sweep flames through a 120° arc.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Deals bonus damage and knocks enemies back.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Inferno")
                        .color(TextColor.fromHexString("#FFD166"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Create a fiery explosion around yourself.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Deals massive damage and launches enemies away.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 90s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // FROST BLADE
    // =========================

    public ItemStack createFrostBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "frostBlade"
        );

        meta.displayName(
                Component.text("❄ FROST BLADE ❄")
                        .color(TextColor.fromHexString("#6EE7FF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("❄ PASSIVE • Frostbite")
                        .color(TextColor.fromHexString("#9CEBFF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Every 10th hit freezes the enemy.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Frozen Dash")
                        .color(TextColor.fromHexString("#64DFFF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Dash forward and freeze enemies you pass through.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Freeze Duration: 3s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Absolute Zero")
                        .color(TextColor.fromHexString("#C7F5FF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Freeze all players within a 3 block radius.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Frozen players take 2x damage.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 5s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 90s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // TIDAL BLADE
    // =========================

    public ItemStack createTidalBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "tidalBlade"
        );

        meta.displayName(
                Component.text("≈ TIDAL BLADE ≈")
                        .color(TextColor.fromHexString("#20BFFF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("◈ PASSIVE • Waterborn")
                        .color(TextColor.fromHexString("#58D7FF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Gain Dolphin's Grace and Water Breathing.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Drowning Field")
                        .color(TextColor.fromHexString("#36C8FF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Create an area where enemies begin drowning.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("You gain Resistance II while active.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 10s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Tidal Surge")
                        .color(TextColor.fromHexString("#7CEBFF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Send out a wave in a 3 block radius.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Launches enemies up and deals 3 hearts.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 90s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // BLOOM BLADE
    // =========================

    public ItemStack createBloomBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "bloomBlade"
        );

        meta.displayName(
                Component.text("❀ BLOOM BLADE ❀")
                        .color(TextColor.fromHexString("#F50CAB"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("❤ PASSIVE • Lifesteal")
                        .color(TextColor.fromHexString("#FF65C8"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Every 10th hit heals you for the damage dealt.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Vital Surge")
                        .color(TextColor.fromHexString("#FF7DD1"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Raise yourself to 15 hearts for 15 seconds.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 60s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Life Seal")
                        .color(TextColor.fromHexString("#F50CAB"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Cap your next victim's maximum health temporarily.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Minimum: 4 hearts")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 20s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 100s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // PULSE BLADE
    // =========================

    public ItemStack createPulseBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "pulseBlade"
        );

        meta.displayName(
                Component.text("✧ PULSE BLADE ✧")
                        .color(TextColor.fromHexString("#A855F7"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("➤ PASSIVE • Featherfall")
                        .color(TextColor.fromHexString("#C084FC"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Become completely immune to fall damage.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Velocity")
                        .color(TextColor.fromHexString("#B968FF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Dash in the direction you are looking.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 15s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Groundbreaker")
                        .color(TextColor.fromHexString("#D8B4FE"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Launch 5 blocks upward and smash into the ground.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Deals 5 hearts within a 3 block radius.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 60s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // STORM BLADE
    // =========================

    public ItemStack createStormBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "stormBlade"
        );

        meta.displayName(
                Component.text("⚡ STORM BLADE ⚡")
                        .color(TextColor.fromHexString("#FFE44D"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("⚡ PASSIVE • Static")
                        .color(TextColor.fromHexString("#FFF176"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Every 5th hit strikes lightning.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Overcharge")
                        .color(TextColor.fromHexString("#69DCFF"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Every hit strikes lightning while active.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 10s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 45s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Thunderstorm")
                        .color(TextColor.fromHexString("#FFE76A"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Create a storm in a 10 block radius.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("You gain Regeneration II.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Enemies gain Weakness and Slowness.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 20s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 120s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // FORTUNE BLADE
    // =========================

    public ItemStack createFortuneBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();

        meta.getPersistentDataContainer().set(
                bladeKey,
                PersistentDataType.STRING,
                "fortuneBlade"
        );

        meta.displayName(
                Component.text("✦ FORTUNE BLADE ✦")
                        .color(TextColor.fromHexString("#FFD700"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(List.of(
                Component.text("✧ PASSIVE • Experience")
                        .color(TextColor.fromHexString("#FFE66D"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Gain 2x XP while holding the blade.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("✦ ABILITY I • Golden Guard")
                        .color(TextColor.fromHexString("#F6C85F"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Summon 5 Iron Golems around you.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("They attack every nearby player except you.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 15s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 60s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.empty(),

                Component.text("◆ ABILITY II • Loaded Dice")
                        .color(TextColor.fromHexString("#A7E76B"))
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Your hits gain random damage multipliers.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Damage ranges from 1.1x to 2x.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("Duration: 7s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),

                Component.text("⌛ Cooldown: 60s")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        blade.setItemMeta(meta);
        return blade;
    }

    // =========================
    // BLADE CHECKS
    // =========================

    public boolean isPulseBlade(ItemStack item) {
        return isBlade(item, "pulseBlade");
    }

    public boolean isFlameBlade(ItemStack item) {
        return isBlade(item, "flameBlade");
    }

    public boolean isFrostBlade(ItemStack item) {
        return isBlade(item, "frostBlade");
    }

    public boolean isTidalBlade(ItemStack item) {
        return isBlade(item, "tidalBlade");
    }

    public boolean isBloomBlade(ItemStack item) {
        return isBlade(item, "bloomBlade");
    }

    public boolean isStormBlade(ItemStack item) {
        return isBlade(item, "stormBlade");
    }

    public boolean isFortuneBlade(ItemStack item) {
        return isBlade(item, "fortuneBlade");
    }

    private boolean isBlade(ItemStack item, String type) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String bladeType = item.getItemMeta()
                .getPersistentDataContainer()
                .get(bladeKey, PersistentDataType.STRING);

        return type.equals(bladeType);
    }
}