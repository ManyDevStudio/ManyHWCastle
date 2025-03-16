package com.dreamcode.loot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor
public class Loot implements Chanced {
    private final double chance;
    private final ItemStack itemStack;
    private final int amountMin;
    private final int amountMax;

    public ItemStack getRandomItemStack() {
        int amount = ThreadLocalRandom.current().nextInt(amountMin, amountMax + 1);
        ItemStack itemStack = new ItemStack(getItemStack());
        itemStack.setAmount(amount);
        return itemStack;
    }
}
