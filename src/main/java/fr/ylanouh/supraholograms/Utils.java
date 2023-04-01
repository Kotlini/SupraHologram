package fr.ylanouh.supraholograms;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static Item spawnItem(ItemStack itemStack, Location location) {
        Item item = location.getWorld().dropItem(location, itemStack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setCustomName(" ");
        item.setCustomNameVisible(false);
        return item;
    }

    public static boolean isString(Object ob) {
        boolean string = true;

        try {
            final String ob1 = (String) ob;
        } catch(ClassCastException e) {
            string = false;
        }

        return string;
    }

    public static boolean isItem(Object ob) {
        boolean item = true;

        try {
            final ItemStack ob1 = (ItemStack) ob;
        } catch(ClassCastException e) {
            item = false;
        }

        return item;
    }
}
