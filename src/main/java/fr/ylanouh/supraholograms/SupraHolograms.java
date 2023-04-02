package fr.ylanouh.supraholograms;

import fr.ylanouh.supraholograms.hologram.HologramBox;

import java.util.HashMap;
import java.util.Map;

public class SupraHolograms {
    private static SupraHolograms instance;

    private final Map<String, HologramBox> hologramsBoxes;

    public SupraHolograms() {
        this.hologramsBoxes = new HashMap<>();
    }

    public void add(HologramBox hologramBox) {
        hologramsBoxes.put(hologramBox.getBoxId(), hologramBox);
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public HologramBox getHologramsBox(String id) {
        return hologramsBoxes.get(id);
    }

    @SuppressWarnings("unused")
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

}