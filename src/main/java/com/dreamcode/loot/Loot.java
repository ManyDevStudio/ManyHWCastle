package com.dreamcode.loot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class Loot {
    private final double chance;
    private final ItemStack itemStack;
    private final int amountMin;
    private final int amountMax;

    @Override
    public String toString() {
        return "Loot{" +
                "chance=" + chance +
                ", itemStack=" + itemStack +
                ", amountMin=" + amountMin +
                ", amountMax=" + amountMax +
                '}';
    }
}
