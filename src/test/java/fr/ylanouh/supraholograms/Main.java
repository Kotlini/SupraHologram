package fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.commands.TestCmd;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    @Override
    public void onEnable() {
        instance = this;

        getCommand("test").setExecutor(new TestCmd());
    }

    @Override
    public void onDisable() {
        SupraHolograms.getInstance().removeAll();
    }

    public static Main getInstance(){
        return instance;
    }
}
