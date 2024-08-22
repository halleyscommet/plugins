package us.dingl.threatLevel;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemStackSplitter {

    public static List<ItemStack> splitItemStack(ItemStack item, int quantity) {
        List<ItemStack> itemStacks = new ArrayList<>();

        int fullStacks = quantity / 64;
        int remainingItems = quantity % 64;

        for (int i = 0; i < fullStacks; i++) {
            ItemStack newItem = new ItemStack(item.getType(), 64);
            newItem.setItemMeta(item.getItemMeta());
            itemStacks.add(newItem);
        }

        if (remainingItems > 0) {
            ItemStack newItem = new ItemStack(item.getType(), remainingItems);
            newItem.setItemMeta(item.getItemMeta());
            itemStacks.add(newItem);
        }

        return itemStacks;
    }
}