package test.fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.Main;
import fr.ylanouh.supraholograms.SupraHolograms;
import fr.ylanouh.supraholograms.Utils;
import fr.ylanouh.supraholograms.builder.HologramBuilder;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.hologram.HologramBox;
import junit.framework.TestCase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SupraHologramsTests extends TestCase {
    public static Location testLocation;

    public void waitTicks(int ticks){
        CountDownLatch latch = new CountDownLatch(1);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), latch::countDown, ticks);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runTask(Runnable run){
        Bukkit.getScheduler().runTask(Main.getInstance(), run);
        waitTicks(2);
    }

    public static List<Entity> getArmorStandsInChunk(Location location){
        return Arrays.stream(location.getChunk().getEntities()).filter((e) -> e.getType() == EntityType.ARMOR_STAND).collect(Collectors.toList());
    }

    public static void prepare(Location location){
        //remove all remaining armor stand in chunk to test safely
        for (Entity armorStand : getArmorStandsInChunk(location)) {
            armorStand.remove();
        }
        SupraHolograms.getInstance().removeAll();
        testLocation = location;
    }

    public void testAll() {
        testEmptyBuilder();
        testBuilderRemoval();
        testSingleLineBuilder();
        testSingleItemBuilder();
        testMultipleLineBuilder();
        testMultipleItemBuilder();
        testPositionBuilder();
    }

    public void testEmptyBuilder(){
        runTask(new HologramBuilder("test", testLocation)::build);
        assertEquals(0, getArmorStandsInChunk(testLocation).size());
        runTask(()->SupraHolograms.getInstance().removeAll());
    }

    public void testBuilderRemoval(){
        runTask(new HologramBuilder("test", testLocation).appendLine("test")::build);
        assertEquals(1, SupraHolograms.getInstance().getHologramsBoxes().size());
        assertEquals(1, getArmorStandsInChunk(testLocation).size());
        runTask(SupraHolograms.getInstance()::removeAll);
        assertEquals(0, SupraHolograms.getInstance().getHologramsBoxes().size());
        assertEquals(0, getArmorStandsInChunk(testLocation).size());
    }

    public void testSingleLineBuilder(){
        AtomicReference<HologramBox> box = new AtomicReference<>();
        runTask(()-> box.set(new HologramBuilder("test", testLocation)
                .appendLine("value")
                .build()));

        assertEquals(1, box.get().getHolograms().size());
        List<Entity> armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(1, armorStands.size());
        assertEquals("value", armorStands.get(0).getName());

        runTask(()->box.get().removeText(0));
        assertEquals(0, box.get().getHolograms().size());
        armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(0, armorStands.size());

        runTask(()-> {
            box.get().appendText("value");
            box.get().getHologram(0).spawn(SpawnType.ALL);
        });
        assertEquals(1, box.get().getHolograms().size());
        armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(1, armorStands.size());
        assertEquals("value", armorStands.get(0).getName());

        runTask(()->box.get().insertText(0, "value2"));
        armorStands = getArmorStandsInChunk(testLocation);
        assertEquals("value2", armorStands.get(0).getName());

        runTask(()->SupraHolograms.getInstance().removeAll());
    }

    public void testSingleItemBuilder(){
        AtomicReference<HologramBox> box = new AtomicReference<>();
        runTask(()-> box.set(new HologramBuilder("test", testLocation)
                .appendLine(new ItemStack(Material.CHEST, 2))
                .build()));

        assertEquals(1, box.get().getHolograms().size());
        List<Entity> armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(1, armorStands.size());
        Entity itemEntity = armorStands.get(0).getPassenger();
        assertEquals(EntityType.DROPPED_ITEM, itemEntity.getType());
        assertEquals(Material.CHEST, ((Item) itemEntity).getItemStack().getType());
        assertEquals(2, ((Item) itemEntity).getItemStack().getAmount());

        runTask(()-> box.get().removeItem(0));
        assertEquals(0, box.get().getHolograms().size());
        armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(0, armorStands.size());

        runTask(()-> {
            box.get().appendItem(Utils.spawnItem(new ItemStack(Material.CHEST, 3), box.get().getLocation()));
            box.get().getHologram(0).spawn(SpawnType.ARMORSTAND);
        });
        assertEquals(1, box.get().getHolograms().size());
        armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(1, armorStands.size());
        itemEntity = armorStands.get(0).getPassenger();
        assertEquals(EntityType.DROPPED_ITEM, itemEntity.getType());
        assertEquals(Material.CHEST, ((Item) itemEntity).getItemStack().getType());
        assertEquals(3, ((Item) itemEntity).getItemStack().getAmount());

        runTask(()->box.get().insertItem(0, Utils.spawnItem(new ItemStack(Material.REDSTONE_BLOCK, 5), box.get().getLocation())));
        assertEquals(1, box.get().getHolograms().size());
        armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(1, armorStands.size());
        itemEntity = armorStands.get(0).getPassenger();
        assertEquals(EntityType.DROPPED_ITEM, itemEntity.getType());
        assertEquals(Material.REDSTONE_BLOCK, ((Item) itemEntity).getItemStack().getType());
        assertEquals(5, ((Item) itemEntity).getItemStack().getAmount());

        runTask(()->SupraHolograms.getInstance().removeAll());
    }

    public void testMultipleLineBuilder(){
        runTask(()->new HologramBuilder("test", testLocation)
                .appendLine("value")
                .appendLines("value1", "value2")
                .build());
        List<Entity> armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(3, armorStands.size());
        assertEquals("value", armorStands.get(0).getName());
        assertEquals("value1", armorStands.get(1).getName());
        assertEquals("value2", armorStands.get(2).getName());
        runTask(()->SupraHolograms.getInstance().removeAll());
    }

    public void testMultipleItemBuilder(){
        runTask(()->new HologramBuilder("test", testLocation)
                .appendLine(new ItemStack(Material.CHEST, 2))
                .appendLines(new ItemStack(Material.GOLD_AXE, 60), new ItemStack(Material.REDSTONE_BLOCK))
                .build());
        List<Entity> armorStands = getArmorStandsInChunk(testLocation);
        assertEquals(3, armorStands.size());
        Entity itemEntity = armorStands.get(0).getPassenger();
        assertEquals(EntityType.DROPPED_ITEM, itemEntity.getType());
        assertEquals(Material.CHEST, ((Item) itemEntity).getItemStack().getType());
        assertEquals(2, ((Item) itemEntity).getItemStack().getAmount());
        itemEntity = armorStands.get(1).getPassenger();
        assertEquals(EntityType.DROPPED_ITEM, itemEntity.getType());
        assertEquals(Material.GOLD_AXE, ((Item) itemEntity).getItemStack().getType());
        assertEquals(60, ((Item) itemEntity).getItemStack().getAmount());
        itemEntity = armorStands.get(2).getPassenger();
        assertEquals(EntityType.DROPPED_ITEM, itemEntity.getType());
        assertEquals(Material.REDSTONE_BLOCK, ((Item) itemEntity).getItemStack().getType());
        assertEquals(1, ((Item) itemEntity).getItemStack().getAmount());
        runTask(()->SupraHolograms.getInstance().removeAll());
    }

    public void testPositionBuilder(){
        runTask(()->new HologramBuilder("test", testLocation)
                .appendLine("value")
                .build());
        Entity armorStand = getArmorStandsInChunk(testLocation).get(0);
        assertEquals(testLocation, armorStand.getLocation());
        runTask(()->SupraHolograms.getInstance().removeAll());
    }
}
