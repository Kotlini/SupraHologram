package fr.ylanouh.supraholograms.config;

import fr.ylanouh.supraholograms.hologram.HologramBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CHologramBox {

    private String id;

    private CLocation location;

    private LinkedHashMap<String, CHologram> hologramMap;

    public LinkedHashMap<String, CHologram> getHologramMap() {
        return hologramMap;
    }

    public void addHologram(CHologram hologram) {
        this.hologramMap.put(hologram.getId(), hologram);
    }

    public CLocation getLocation() {
        return location;
    }

    public void setLocation(CLocation location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HologramBox toBox() {
        final HologramBox hologramBox = new HologramBox(getId(), getLocation().toLocation());
        hologramBox.setHolograms(hologramMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().toHologram(), (existing, replacement) -> existing, LinkedHashMap::new)));
        return hologramBox;
    }
}