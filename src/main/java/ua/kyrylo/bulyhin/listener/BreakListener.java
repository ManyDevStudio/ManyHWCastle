package ua.kyrylo.bulyhin.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ua.kyrylo.bulyhin.ManyShulkers;
import ua.kyrylo.bulyhin.loot.LootBox;

public class BreakListener implements Listener {
    private final ManyShulkers plugin;

    public BreakListener(ManyShulkers plugin) {
        this.plugin = plugin;
    }

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
}
