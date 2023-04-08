package fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.builder.HologramBuilder;
import fr.ylanouh.supraholograms.commands.SHCmd;
import fr.ylanouh.supraholograms.commands.TestCmd;
import fr.ylanouh.supraholograms.listeners.JoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("test").setExecutor(new TestCmd());
        getCommand("sh").setExecutor(new SHCmd());

        SupraHolograms.getInstance().setPlugin(this);

        Location location = new Location(Bukkit.getWorld("world"), 47.5, 64, 151.5);

        // Create simple holograms
        new HologramBuilder("test-1", location).appendLines("&eWorld", "&6Hello").build();

        // Create holograms line and item, add space 0.6 for "&6Hello"
        new HologramBuilder("test-2", location.add(-3, 0, 0))
                .appendLine("&eWorld").appendLine(new ItemStack(Material.EMERALD_BLOCK))
                .appendLine("&6Hello")
                .addSpace(2, 0.6).build();

        // Create holograms line and item
        new HologramBuilder("test-3", location.add(-3, 0, 0))
                .appendLines("&eWorld", "&6Hello").appendLine(new ItemStack(Material.EMERALD_BLOCK)).build();

        // Create holograms client
        new HologramBuilder("test-4", location.add(-6, 0, 0))
                .appendLinesPacket("&bUUID:&c %uuid%", "&bIP:&c %ip%", "&9&lInfo player&b %player%")
                .appendLine(new ItemStack(Material.PAPER)).build();

        // Create holograms client
        new HologramBuilder("test-5", location.add(-6, 0, 0))
                .appendLinesPacket("&a&lonly one to see me", "&2&lYou are the").build();

        getServer().getPluginManager().registerEvents(new JoinPlayer(), this);

        System.out.println("SupraHolograms was enabled");
        System.out.println("Unit test starting...");

        try {
            System.out.println("Starting test at 0, 150, 0");
            SupraHologramsTests.prepare(new Location(Bukkit.getWorld("world"), 0, 150, 0));
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ()->{
                new SupraHologramsTests().testAll();
                System.out.println("Tests success");
            });
        } catch (Exception e) {
            System.out.println("Tests failed with errors");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void onDisable() {
        SupraHolograms.getInstance().removeAll();
    }

    public static Main getInstance(){
        return instance;
    }
}
