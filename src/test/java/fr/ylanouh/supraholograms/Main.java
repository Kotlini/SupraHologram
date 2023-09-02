package fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.commands.SHCmd;
import fr.ylanouh.supraholograms.commands.TestCmd;
import fr.ylanouh.supraholograms.listeners.JoinPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("test").setExecutor(new TestCmd());
        getCommand("sh").setExecutor(new SHCmd());

        SupraHolograms.register(this, getServer().getPluginManager());
        SupraHolograms.getInstance().setPlugin(this);

        getServer().getPluginManager().registerEvents(new JoinPlayer(), this);

        System.out.println("SupraHolograms was enabled");
        System.out.println("Unit test starting...");

        System.out.println("Tests success");


        System.out.println("load config...");
        final File fileHolo = new File(getDataFolder() + File.separator + "holograms.yml");
        SupraHolograms.getInstance().load(YamlConfiguration.loadConfiguration(fileHolo));
        SupraHolograms.getInstance().spawnAll();
        System.out.println(SupraHolograms.getInstance().getHologramsBoxes().size() + " holograms loaded");
        /*try {
            System.out.println("Starting test at 0, 150, 0");
            SupraHologramsTests.prepare(new Location(Bukkit.getWorld("world"), 0, 150, 0));
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ()->{
                new SupraHologramsTests().testAll();            });
        } catch (Exception e) {
            System.out.println("Tests failed with errors");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }*/
    }

    @Override
    public void onDisable() {
        final File fileHolo = new File(getDataFolder() + File.separator + "holograms.yml");
        SupraHolograms.getInstance().save(fileHolo, YamlConfiguration.loadConfiguration(fileHolo));
        SupraHolograms.getInstance().removeAll();
    }

    public static Main getInstance(){
        return instance;
    }
}
