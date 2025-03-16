package com.dreamcode.listener;

import com.dreamcode.ManyShulkers;
import com.dreamcode.loot.LootBox;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BreakListener implements Listener {
    private final ManyShulkers plugin;

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        LootBox lootBox = plugin.getLootManager().getLootBoxOrNull(loc);
        if (lootBox == null) {
            return;
        }

        event.setCancelled(true);
        lootBox.breakLootBox();

        if (lootBox.isBroken()) {
            plugin.getLootManager().dropLoot(lootBox, loc);
            return;
        }

        Player player = event.getPlayer();
        player.sendActionBar(plugin.getConfiguration().getMessageBreak()
                .replace("%amount%", String.valueOf(lootBox.getBreaksLeft())));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Location loc = block.getLocation();
        LootBox lootBox = plugin.getLootManager().getLootBoxOrNull(loc);

        if (lootBox == null) {
            return;
        }

        event.setCancelled(true);
    }
}
