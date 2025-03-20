package com.manydev;

import com.manydev.loot.Chanced;
import com.manydev.loot.Loot;
import com.manydev.loot.LootBox;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Getter
public class LootManager {
    private final Map<Location, LootBox> lootBoxes = new ConcurrentHashMap<>();
    private final ManyCastle plugin;
    private boolean regenerationEnabled = true;
    private BukkitTask task;

    public LootManager(ManyCastle plugin) {
        this.plugin = plugin;
        runTask();
    }

    public void register(LootBox lootBox, Location location) {
        lootBoxes.put(location, lootBox);
        location.getBlock().setType(lootBox.getMaterial());

        if (plugin.getConfiguration().isExplosionEnabled()) {
            location.createExplosion((float) plugin.getConfiguration().getExplosionDamage(), false, false);
        }
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
        unregister(location);
        int amountItems = ThreadLocalRandom.current().nextInt(lootBox.getMinLoot(), lootBox.getMaxLoot() + 1);


        for (int i = 0; i  < amountItems; i++) {

            if (lootBox.getLoots().isEmpty()) {
                break;
            }

            Loot loot = (Loot) getRandom(lootBox.getLoots());
            location.getWorld().dropItem(location, loot.getRandomItemStack());
        }
    }

    public void spawnLootBoxes() {
        List<Location> locationsCopy = new ArrayList<>(plugin.getConfiguration().getLocations());
        locationsCopy.removeAll(lootBoxes.keySet());

        List<LootBox> lootBoxesCopy = plugin.getConfiguration().getLootBoxes().stream().map(LootBox::new).collect(Collectors.toList());
        int max = plugin.getConfiguration().getShulkersMax();
        int min = plugin.getConfiguration().getShulkersMin();

        int amountShulkers = ThreadLocalRandom.current().nextInt(min, max + 1);

        for (int i = 0; i < amountShulkers; i++) {
            if (locationsCopy.isEmpty()) {
                break;
            }
            LootBox lootBox = ((LootBox) getRandom(lootBoxesCopy)).copy();
            Location randomLocation = locationsCopy.remove(ThreadLocalRandom.current().nextInt(locationsCopy.size()));
            register(lootBox, randomLocation);
        }
    }

    private Chanced getRandom(List<? extends Chanced> items) {
        int rand = ThreadLocalRandom.current().nextInt(101);
        double percentTotal = 0;

        for (Chanced item : items) {
            percentTotal += item.getChance();

            if (rand < percentTotal) {
                return item;
            }
        }

        return items.get(0);
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
