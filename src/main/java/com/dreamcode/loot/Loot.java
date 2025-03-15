package com.dreamcode.loot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class Loot {
    private final double chance;
    private final ItemStack itemStack;
}
