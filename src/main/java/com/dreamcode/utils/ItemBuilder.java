package com.dreamcode.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {

    public static ItemBuild build(ItemStack item) {
        return new ItemBuilder.ItemBuild(item);
    }

    public static ItemBuilder.Checker check(ItemStack item) {
        return new ItemBuilder.Checker(item);
    }

    public static class Checker {
        private final ItemStack item;
        private final ItemMeta meta;

        public Checker(ItemStack item) {
            this.item = item;
            this.meta = item.getItemMeta();
        }

        public String getPersistent(String key) {
            if (!meta.getPersistentDataContainer().isEmpty()) {
                PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
                if (persistentDataContainer.has(NamespacedKey.fromString(key), PersistentDataType.STRING)) {
                    return persistentDataContainer.get(NamespacedKey.fromString(key), PersistentDataType.STRING);
                }
            }
            return "none";
        }
    }

    public static class ItemBuild {
        private final ItemStack item;
        private final ItemMeta meta;

        public ItemBuild(ItemStack item) {
            this.item = item;
            this.meta = item.getItemMeta();
        }

        public String getName() {
            return meta.getDisplayName();
        }

        public List<String> getLore() {
            return meta.getLore();
        }

        public PersistentDataContainer getPdt() {
            return meta.getPersistentDataContainer();
        }

        public boolean equalsPdt(ItemBuilder.ItemBuild itemBuild) {

            if (item.getItemMeta() != null && itemBuild.meta != null && getPdt().getKeys().containsAll(itemBuild.getPdt().getKeys())) {

                Map<String, String> checkMap = new HashMap<>();
                Map<String, String> targetMap = new HashMap<>();

                getPdt().getKeys().forEach(checkKey -> checkMap.put(checkKey.getKey(), getPdt().get(checkKey, PersistentDataType.STRING)));
                itemBuild.getPdt().getKeys().forEach(targetKey -> targetMap.put(targetKey.getKey(), itemBuild.getPdt().get(targetKey, PersistentDataType.STRING)));

                return (checkMap.entrySet().containsAll(targetMap.entrySet()));
            }

            return false;
        }

        public ItemBuild setNameFromConfig(Player player, FileConfiguration config, String path) {
            String name = config.getString(path);
            if (name != null) setName(PlaceholderAPI.setPlaceholders(player, name));
            return this;
        }

        public ItemBuild setNameFromSection(Player player, ConfigurationSection section, String path) {
            String name = section.getString(path);
            if (name != null) setName(PlaceholderAPI.setPlaceholders(player, name));
            return this;
        }

        public ItemBuild setColorOfLeather(Color color) {
            if (!item.getType().name().contains("LEATHER_")) return this;
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(color);
            item.setItemMeta(leatherArmorMeta);
            return this;
        }

        public ItemBuild setEffects(List<PotionEffect> potionEffects) {
            if (!item.getType().name().contains("POTION")) return this;

            PotionMeta potionMeta = (PotionMeta) meta;
            potionEffects.forEach(potionEffect -> potionMeta.addCustomEffect(potionEffect, false));
            item.setItemMeta(meta);
            return this;
        }

        public ItemBuild setEffectsFromConfig(FileConfiguration config, String path) {

            List<String> effects = config.getStringList(path);
            List<PotionEffect> effectList = new ArrayList<>();

            effects.forEach(effect -> effectList.add(new PotionEffect(
                    PotionEffectType.getByName(effect.split(":")[0]),
                    Integer.parseInt(effect.split(":")[1]),
                    Integer.parseInt(effect.split(":")[2]))));
            setEffects(effectList);

            return this;
        }

        public ItemBuild setEffectsFromSection(ConfigurationSection section, String path) {

            List<String> effects = section.getStringList(path);
            List<PotionEffect> effectList = new ArrayList<>();

            effects.forEach(effect -> effectList.add(new PotionEffect(
                    PotionEffectType.getByName(effect.split(":")[0]),
                    Integer.parseInt(effect.split(":")[1]),
                    Integer.parseInt(effect.split(":")[2]))));
            setEffects(effectList);

            return this;
        }

        public ItemBuild setAmount(int amount) {
            item.setAmount(amount);
            return this;
        }
        public ItemBuild setAmountFromConfig(FileConfiguration config, String path) {
            if (config.getInt(path) != 0) item.setAmount(config.getInt(path));
            return this;
        }

        public ItemBuild setAmountFromSection(ConfigurationSection section, String path) {
            if (section.getInt(path) != 0) item.setAmount(section.getInt(path));
            return this;
        }

        public ItemBuild setNameFromConfig(FileConfiguration config, String path) {
            if (config.getString(path) != null) {
                setName(config.getString(path)); }
            return this;
        }

        public ItemBuild setNameFromSection(ConfigurationSection section, String path) {
            if (section.getString(path) != null) {
                setName(section.getString(path)); }
            return this;
        }

        public ItemBuild setName(String name) {
            meta.setDisplayName(HexColor.color(name));
            return this;
        }

        public ItemBuild setLoreFromConfig(FileConfiguration config, String path) {
            List<String> lore = config.getStringList(path);
            if (!lore.isEmpty()) setLore(lore);
            return this;
        }

        public ItemBuild setLoreFromSection(ConfigurationSection section, String path) {
            List<String> lore = section.getStringList(path);
            if (!lore.isEmpty()) setLore(lore);
            return this;
        }

        public ItemBuild setLoreFromConfig(Player player, FileConfiguration config, String path) {
            List<String> lore = config.getStringList(path);
            if (!lore.isEmpty()) {
                lore.forEach(s -> PlaceholderAPI.setPlaceholders(player, s));
                setLore(lore);
            }

            return this;
        }

        public ItemBuild setLoreFromSection(Player player, ConfigurationSection section, String path) {
            List<String> lore = section.getStringList(path);
            if (!lore.isEmpty()) {
                lore.forEach(s -> PlaceholderAPI.setPlaceholders(player, s));
                setLore(lore);
            }

            return this;
        }

        public ItemBuild setLore(String... lore) {
            List<String> l = new ArrayList<>();
            for (String s : lore) {
                l.add(HexColor.color(s));
            }
            meta.setLore(l);
            return this;
        }

        public ItemBuild setLore(List<String> lore) {
            List<String> l = new ArrayList<>();
            for (String s : lore) {
                l.add(HexColor.color(s));
            }
            meta.setLore(l);
            return this;
        }

        public ItemBuild setItemFlagsFromConfig(FileConfiguration config, String path) {
            List<String> itemflags = config.getStringList(path);

            if (!itemflags.isEmpty()) setItemFlags(itemflags
                    .stream()
                    .map(s1 -> ItemFlag.valueOf(s1))
                    .collect(Collectors.toSet())
            );
            return this;
        }

        public ItemBuild setItemFlagsFromSection(ConfigurationSection section, String path) {
            List<String> itemflags = section.getStringList(path);

            if (!itemflags.isEmpty()) setItemFlags(itemflags
                    .stream()
                    .map(s1 -> ItemFlag.valueOf(s1))
                    .collect(Collectors.toSet())
            );
            return this;
        }

        public ItemBuild setItemFlags(Set<ItemFlag> itemsFlags) {
            if (itemsFlags != null && !itemsFlags.isEmpty()) {
                itemsFlags.forEach(meta::addItemFlags);
                itemsFlags.forEach(item::addItemFlags);
            }
            return this;
        }

        public ItemBuild setPersistent(PersistentDataContainer dataContainer) {
            if (dataContainer != null && !dataContainer.isEmpty()) {
                dataContainer.getKeys().forEach(namespacedKey -> getPdt().set(namespacedKey, PersistentDataType.STRING, dataContainer.get(namespacedKey, PersistentDataType.STRING)));
            }
            return this;
        }

        public ItemBuild setPersistent(Map<String, String> map) {
            if (map != null && !map.isEmpty()) {
                map.forEach((key, value) ->
                        meta.getPersistentDataContainer().set(new NamespacedKey(key, key), PersistentDataType.STRING, value));
            }
            return this;
        }

        public ItemBuild setPersistentFromConfig(FileConfiguration config, String path) {
            List<String> persistent = config.getStringList(path);

            if (!persistent.isEmpty()) setPersistent(persistent
                    .stream()
                    .map(s1 -> s1.split(":"))
                    .collect(Collectors.toMap(
                            key -> key[0],
                            value -> value[1]
                    ))
            );
            return this;
        }

        public ItemBuild setPersistentFromSection(ConfigurationSection section, String path) {
            List<String> persistent = section.getStringList(path);

            if (!persistent.isEmpty()) setPersistent(persistent
                    .stream()
                    .map(s1 -> s1.split(":"))
                    .collect(Collectors.toMap(
                            key -> key[0],
                            value -> value[1]
                    ))
            );
            return this;
        }

        public ItemBuild setUnbreakable(boolean b) {
            meta.setUnbreakable(b);
            return this;
        }

        public ItemBuild setUnbreakableFromConfig(FileConfiguration config, String path) {

            if (config.getString(path) != null) {
                meta.setUnbreakable(config.getBoolean(path));
            }
            return this;
        }

        public ItemBuild setUnbreakableFromSection(ConfigurationSection section, String path) {

            if (section.getString(path) != null) {
                meta.setUnbreakable(section.getBoolean(path));
            }
            return this;
        }

        public ItemBuild setEnchantments(Map<Enchantment, Integer> enchantments) {
            if (!enchantments.isEmpty()) {
                if (item.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                    enchantments.keySet().forEach(enchantment -> {
                        if (enchantment != null) enchantmentStorageMeta.addStoredEnchant(enchantment, enchantments.get(enchantment), false);
                        item.setItemMeta(enchantmentStorageMeta);
                    });
                } else {
                    enchantments.forEach((enchantment, lvl) -> {
                        meta.addEnchant(enchantment, lvl ,true);
                        item.setItemMeta(meta);
                    });
                }
            }
            return this;
        }

        public ItemBuild setEnchantmentsFromConfig(FileConfiguration config, String path) {
            List<String> enchantments = config.getStringList(path);

            if (!enchantments.isEmpty()) {
                setEnchantments(enchantments
                        .stream()
                        .map(s1 -> s1.split(":"))
                        .collect(Collectors.toMap(
                                enchantment -> Enchantment.getByName(enchantment[0].trim().toUpperCase()),
                                enchantment -> Integer.parseInt(enchantment[1].trim())
                        ))
                );
            }
            return this;
        }

        public ItemBuild setEnchantmentsFromSection(ConfigurationSection section, String path) {
            List<String> enchantments = section.getStringList(path);

            if (!enchantments.isEmpty()) {
                setEnchantments(enchantments
                        .stream()
                        .map(s1 -> s1.split(":"))
                        .collect(Collectors.toMap(
                                enchantment -> Enchantment.getByName(enchantment[0].trim().toUpperCase()),
                                enchantment -> Integer.parseInt(enchantment[1].trim())
                        ))
                );
            }
            return this;
        }

        public ItemStack save() {
            item.setItemMeta(meta);
            return item;
        }
    }
}
