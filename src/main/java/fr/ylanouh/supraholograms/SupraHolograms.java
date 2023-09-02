package fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.hologram.HologramBox;
import fr.ylanouh.supraholograms.hologram.HologramItem;
import fr.ylanouh.supraholograms.hologram.HologramText;
import fr.ylanouh.supraholograms.hologram.packets.HologramTextPacket;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import fr.ylanouh.supraholograms.listener.HologramListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SupraHolograms {

    private static SupraHolograms instance;

    private Plugin plugin;

    private final Map<String, HologramBox> hologramsBoxes;

    public SupraHolograms() {
        this.hologramsBoxes = new HashMap<>();
    }

    public void add(HologramBox hologramBox) {
        hologramsBoxes.put(hologramBox.getBoxId(), hologramBox);
    }

    public void remove(String id) {
        hologramsBoxes.get(id).removeAll();
        hologramsBoxes.remove(id);
    }

    public void removeAll() {
        deSpawnAll();
        hologramsBoxes.clear();
    }

    public Map<String, HologramBox> getHologramsBoxes() {
        return hologramsBoxes;
    }

    public HologramBox getHologramsBox(String id) {
        return hologramsBoxes.get(id);
    }

    public void spawnAll() {
        for (HologramBox hologramBox : hologramsBoxes.values()) {
            hologramBox.spawnAll();
        }
    }

    public void deSpawnAll() {
        for (HologramBox hologramBox : hologramsBoxes.values()) {
            hologramBox.removeAll();
        }
    }

    public static SupraHolograms getInstance(){
        if(instance == null) instance = new SupraHolograms();
        return instance;
    }

    public void save(File file, FileConfiguration config) {
        if (config == null) return;

        for (HologramBox hologramBox : getHologramsBoxes().values()) {
            final String key = "hologramsBox." + hologramBox.getBoxId() + ".";
            config.set(key + "location", parseLocToString(hologramBox.getLocation()));
            for (Hologram hologram : hologramBox.getHolograms().values()) {
                final String keyHolo = key + "holograms." + hologram.getId() + ".";
                config.set(keyHolo + "location", parseLocToString(hologram.getLocation()));
                config.set(keyHolo + "type", hologram.getType());
                config.set(keyHolo + "line", (hologram.getType().equalsIgnoreCase("item") ? ((Item) hologram.getLine()).getItemStack().getType().name()
                        : hologram.getLine()));
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(Plugin plugin, PluginManager pm) {
        pm.registerEvents(new HologramListener(), plugin);
    }

    public void load(FileConfiguration config) {
        if (config == null) return;
        final ConfigurationSection configurationSection = config.getConfigurationSection("hologramsBox");
        if (configurationSection == null) return;

        for (String id : configurationSection.getKeys(false)) {
            if (id == null) return;

            final String key = "hologramsBox." + id + ".";

            final HologramBox hologramBox = new HologramBox(id, parseStringToLoc(config.getString(key + "location")));
            for (String holoId : config.getConfigurationSection(key + "holograms").getKeys(false)) {
                final String keyHolo = key + "holograms." + holoId + ".";
                System.out.println("Type: " + config.getString(keyHolo + "type"));

                switch (config.getString(keyHolo + "type")) {
                    case "packet":
                        hologramBox.add(new HologramTextPacket(holoId, config.getString(keyHolo + "line"), parseStringToLoc(config.getString(keyHolo + "location"))));
                        break;
                    case "item":
                        hologramBox.add(new HologramItem(holoId,  Utils.spawnItem(new ItemStack(Material.
                                valueOf(config.getString(keyHolo + "line"))), parseStringToLoc(config.getString(keyHolo + "location"))),
                                parseStringToLoc(config.getString(keyHolo + "location")), null));
                        break;
                    default:
                        hologramBox.add(new HologramText(holoId, config.getString(keyHolo + "line"), parseStringToLoc(config.getString(keyHolo + "location"))));
                        break;
                }
            }
            add(hologramBox);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    private String parseLocToString(Location location) {
        return location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getWorld().getName();
    }

    private Location parseStringToLoc(String loc) {
        final String[] parser = loc.split(",");
        return new Location(Bukkit.getWorld(parser[3]), Double.parseDouble(parser[0]), Double.parseDouble(parser[1]),
                Double.parseDouble(parser[2]));
    }
 }