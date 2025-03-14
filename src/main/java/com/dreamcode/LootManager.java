package com.dreamcode;

import com.dreamcode.loot.Loot;
import com.dreamcode.loot.LootBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LootManager {
    private final Map<Location, LootBox> lootBoxes = new ConcurrentHashMap<>();
    private final ManyShulkers plugin;
    private boolean regenerationEnabled = true;
    private BukkitTask task;

    public LootManager(ManyShulkers plugin) {
        this.plugin = plugin;
        runTask();
    }

    public void register(LootBox lootBox, Location location) {
        lootBoxes.put(location, lootBox);
        location.getBlock().setType(lootBox.getMaterial());
    }

    public void unregister(Location location) {
        lootBoxes.remove(location);
        location.getBlock().setType(Material.AIR);
    }

    public void unregisterAll() {
        for (Location location : lootBoxes.keySet()) {
            unregister(location);
        }
    }

    public void dropLoot(LootBox lootBox, Location location) {
        int amountItems = ThreadLocalRandom.current().nextInt((lootBox.getMaxLoot() - lootBox.getMinLoot()) + 1) + lootBox.getMaxLoot();

        for (int i = 0; i  < amountItems; i++) {

            if (lootBox.getLoots().isEmpty()) {
                break;
            }

            Loot loot = getRandom(lootBox.getLoots());
            location.getWorld().dropItem(location, loot.getItemStack());
        }
        unregister(location);
    }

    public void spawnLootBoxes() {
        List<Location> locationsCopy = new ArrayList<>(plugin.getConfiguration().getLocations());

        for (Location location : lootBoxes.keySet()) {
            locationsCopy.remove(location);
        }

        List<LootBox> lootBoxesCopy = plugin.getConfiguration().getLootBoxes().stream().map(LootBox::new).collect(Collectors.toList());
        int max = plugin.getConfiguration().getShulkersMax();
        int min = plugin.getConfiguration().getShulkersMin();
        int amountShulkers = ThreadLocalRandom.current().nextInt((max - min) + 1) + min;


        for (int i = 0; i  < amountShulkers; i++) {

            if (locationsCopy.isEmpty()) {
                break;
            }

            LootBox lootBox = getRandom(lootBoxesCopy);
            Location randomLocation = locationsCopy.get(ThreadLocalRandom.current().nextInt(locationsCopy.size()));
            register(lootBox, randomLocation);
        }
    }

    private <T> T getRandom(List<T> items) {
        int rand = ThreadLocalRandom.current().nextInt(101);
        double percentTotal = 0;

        for (T item : items) {
            percentTotal += getChanceFromItem(item);

            if (rand < percentTotal) {
                return item;
            }
        }

        return items.get(0);
    }

    private double getChanceFromItem(Object item) {
        if (item instanceof LootBox) {
            return ((LootBox) item).getChance();
        } else if (item instanceof Loot) {
            return ((Loot) item).getChance();
        }
        throw new IllegalArgumentException("Unsupported item type");
    }

    public LootBox getLootBoxOrNull(Block block) {
        return getLootBoxOrNull(block.getLocation());
    }

    public LootBox getLootBoxOrNull(Location location) {
        if (lootBoxes.containsKey(location)) {
            return lootBoxes.get(location);
        }
        return null;
    }

    private void runTask() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            spawnLootBoxes();
        }, 0L, plugin.getConfiguration().getTimeRegeneration() * 20L);
    }

    public void setRegenerationEnabled(boolean regenerationEnabled) {

        if (!task.isCancelled()) {
            task.cancel();
        }

        if (regenerationEnabled) {
            runTask();
        }

        this.regenerationEnabled = regenerationEnabled;
    }
}
