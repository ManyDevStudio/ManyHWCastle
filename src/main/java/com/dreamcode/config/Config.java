package com.dreamcode.config;

import com.dreamcode.loot.Loot;
import com.dreamcode.loot.LootBox;
import com.dreamcode.utils.HexColor;
import com.dreamcode.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Config {
    private final Map<String, Object> configMap = new HashMap<>();
    private final File file;
    private final FileConfiguration yaml;

    private String messageStartRegeneration;

    private String messageStopRegeneration;

    private String messageStartReload;

    private String messageBreak;

    private String messageNoPermission;

    private String messageNoCommand;

    private boolean explosionEnabled;

    private double explosionDamage;

    private double explosionRadiusDamage;

    private int timeRegeneration;

    private int shulkersMin;

    private int shulkersMax;

    private List<Location> locations;

    private List<LootBox> lootBoxes;

    public Config (JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        ConfigurationSection sectionMessages = yaml.getConfigurationSection("messages");

        messageStartRegeneration = HexColor.color(sectionMessages.getString("start-regeneration"));
        messageStopRegeneration = HexColor.color(sectionMessages.getString("stop-regeneration"));
        messageStartReload = HexColor.color(sectionMessages.getString("reload"));
        messageBreak = HexColor.color(sectionMessages.getString("break"));
        messageNoPermission = HexColor.color(sectionMessages.getString("no-permission"));
        messageNoCommand = HexColor.color(sectionMessages.getString("no-command"));

        ConfigurationSection sectionShulkers = yaml.getConfigurationSection("shulkers");

        shulkersMin = sectionShulkers.getInt("min");
        shulkersMax = sectionShulkers.getInt("max");
        timeRegeneration = sectionShulkers.getInt("time-regeneration");
        locations = readLocations(sectionShulkers.getConfigurationSection("locations"));
        lootBoxes = readLootBoxes(sectionShulkers.getConfigurationSection("rarities"));

        ConfigurationSection sectionExplosion = sectionShulkers.getConfigurationSection("explosion");

        explosionEnabled = sectionExplosion.getBoolean("enabled");
        explosionDamage = sectionExplosion.getDouble("damage");
        explosionRadiusDamage = sectionExplosion.getDouble("radius-damage");
    }

    public List<Location> readLocations(ConfigurationSection section) {
        List<Location> locations = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            locations.add(readLocation(section.getCurrentPath() + "." + key));
        }

        return locations;
    }

    public Location readLocation(String key) {
        String worldName = yaml.getString(key + ".world");
        double x = yaml.getDouble(key + ".x");
        double y = yaml.getDouble(key + ".y");
        double z = yaml.getDouble(key + ".z");

        World world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z);
    }

    public List<LootBox> readLootBoxes(ConfigurationSection section) {
        List<LootBox> lootBoxes = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            lootBoxes.add(readLootBox(section.getConfigurationSection(key).getCurrentPath()));
        }

        return lootBoxes;
    }

    public LootBox readLootBox(String key) {
        double chance = yaml.getDouble(key + ".chance");
        Material material = Material.valueOf(yaml.getString(key + ".material"));
        int amountBreaks = yaml.getInt(key + ".amount-breaks");
        int lootMin = yaml.getInt(key + ".loot.min");
        int lootMax = yaml.getInt(key + ".loot.max");
        List<Loot> loots = readLoots(yaml.getConfigurationSection(key + ".loot.items"));

        return new LootBox(material, loots, amountBreaks, chance, lootMin, lootMax);
    }

    public List<Loot> readLoots(ConfigurationSection section) {
        List<Loot> loots = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            loots.add(readLoot(section.getConfigurationSection(key).getCurrentPath()));
        }

        return loots;
    }

    public Loot readLoot(String key) {
        double chance = yaml.getDouble(key + ".chance");
        ConfigurationSection sectionAmount = yaml.getConfigurationSection(key + ".amount");
        int amountMin;
        int amountMax;

        if (sectionAmount != null) {
             amountMin = sectionAmount.getInt("min");
             amountMax = sectionAmount.getInt("max");
        } else {
            amountMin = 1;
            amountMax = 1;
        }

        String materialName = yaml.getString(key + ".material");
        Material material = Material.valueOf(materialName);
        ItemStack itemStack = ItemBuilder.build(new ItemStack(material))
                .setEnchantmentsFromConfig(yaml, key  + ".enchantments")
                .setLoreFromConfig(yaml, key + ".lore")
                .setPersistentFromConfig(yaml, key + ".pdt")
                .setItemFlagsFromConfig(yaml, key + ".itemflags")
                .setUnbreakableFromConfig(yaml, key + "unbreakable")
                .setNameFromConfig(yaml, key + ".name")
                .save();
        return new Loot(chance, itemStack, amountMin, amountMax);
    }
}
