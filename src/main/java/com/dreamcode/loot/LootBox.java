package com.dreamcode.loot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@AllArgsConstructor
public class LootBox {
    private final Material material;
    private final List<Loot> loots;
    private final double chance;
    private final int minLoot;
    private final int maxLoot;
    private int breaksLeft;
    private boolean broken = false;

    public LootBox(Material material, List<Loot> loots, int breaksLeft, double chance, int minLoot, int maxLoot) {
        this.material = material;
        this.loots = loots;
        this.breaksLeft = breaksLeft;
        this.chance = chance;
        this.minLoot = minLoot;
        this.maxLoot = maxLoot;
    }

    public LootBox (LootBox other) {
        this.material = other.getMaterial();
        this.loots = other.getLoots();
        this.breaksLeft = other.getBreaksLeft();
        this.chance = other.getChance();
        this.minLoot = other.getMinLoot();
        this.maxLoot = other.getMaxLoot();
        this.broken = other.broken;
    }

    public void breakLootBox() {
        if (!isBroken()) {
            breaksLeft--;
        }
        if (breaksLeft <= 0) {
            broken = true;
        }
    }

    public LootBox copy() {
        return new LootBox(this);
    }

}
