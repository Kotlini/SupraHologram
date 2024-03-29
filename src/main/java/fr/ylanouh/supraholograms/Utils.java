package fr.ylanouh.supraholograms;

import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Utils {

    public static Item spawnItem(ItemStack itemStack, Location location) {
        final Item item = location.getWorld().dropItem(location, itemStack);
        item.setCustomName("SUPRAHOLOGRAMS");
        item.setVelocity(new Vector(0, 0, 0));
        item.setCustomNameVisible(false);
        return item;
    }

    public static void teleportArmorStand(ArmorStand armorStand, Location location) {
        Entity passenger = armorStand.getPassenger();

        armorStand.eject();
        passenger.teleport(location);
        armorStand.teleport(location);
        armorStand.setPassenger(passenger);
    }

    public static WorldServer toNMSWorld(World world) throws Exception {
        CraftWorld craftWorld = (CraftWorld) world;
        Method getHandleMethod = craftWorld.getClass().getMethod("getHandle");
        Object nmsWorld = getHandleMethod.invoke(craftWorld);
        return (WorldServer) nmsWorld;
    }
}
