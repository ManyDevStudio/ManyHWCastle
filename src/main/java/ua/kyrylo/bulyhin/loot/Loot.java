package ua.kyrylo.bulyhin.loot;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Loot {
    private final double chance;
    private final ItemStack itemStack;
    private final int amountMin;
    private final int amountMax;

    public Loot(double chance, ItemStack itemStack, int amountMin, int amountMax) {
        this.chance = chance;
        this.itemStack = itemStack;
        this.amountMin = amountMin;
        this.amountMax = amountMax;
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getAmountMin() {
        return amountMin;
    }

    public int getAmountMax() {
        return amountMax;
    }

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
