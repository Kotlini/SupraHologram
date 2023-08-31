package fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.config.CHologramBox;
import fr.ylanouh.supraholograms.hologram.HologramBox;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void save(File file, ClassLoader classLoader) {
        final Yaml yaml = new Yaml(new CustomClassLoaderConstructor(classLoader));
        try (PrintWriter writerProfile = new PrintWriter(file)) {
            yaml.dumpAll(hologramsBoxes.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().toConfig(),
                    (e1, e2) -> e1,
                    HashMap::new
            )).entrySet().iterator(), writerProfile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(File file, ClassLoader classLoader) {
        final Yaml yaml = new Yaml(new CustomClassLoaderConstructor(classLoader));
        yaml.setBeanAccess(BeanAccess.FIELD);
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            final Iterable<Object> arenas = yaml.loadAll(inputStream);
            while (arenas.iterator().hasNext()) {
                final Object o = arenas.iterator().next();
                if (o instanceof CHologramBox) {
                    add(((CHologramBox) o).toBox());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}