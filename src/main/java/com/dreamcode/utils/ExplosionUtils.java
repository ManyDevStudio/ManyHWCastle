package com.dreamcode.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ExplosionUtils {

    public static void explosion(Location loc, double radiusDamage, double damage) {
        World world = loc.getWorld();
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
        Collection<Player> players = loc.getNearbyPlayers(radiusDamage);
        for (Player player : players) {
            player.damage(damage);
        }
    }
}
