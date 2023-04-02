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
}
