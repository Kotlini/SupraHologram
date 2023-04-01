package fr.ylanouh.supraholograms.hologram;

import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;

import java.util.*;

public class HologramBox {
    private String boxId;

    private Location location;

    private LinkedHashMap<String, Hologram> holograms;

    public HologramBox(String boxId, Location location) {
        this.boxId = boxId;
        this.location = location;
        this.holograms = new LinkedHashMap<>();
    }

    public void appendText(String name, double quirky) {
        String id = generateID();
        holograms.put(id, new HologramText(generateID(), name, getNewLoc(quirky)));
    }

    public void insertText(int index, String line) {
        Iterator<Map.Entry<String, Hologram>> iterator = holograms.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        iterator.next().getValue().setLine(line);
        iterator.remove();
    }

    public void removeText(int index) {
        Iterator<Map.Entry<String, Hologram>> iterator = holograms.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }

        if (iterator.next().getValue().isText()) {
            HologramText hologramItem = (HologramText) iterator.next();
            hologramItem.remove(RemoveType.ALL);
            iterator.remove();
        }
    }

    public void appendItem(Item item, double quirky) {
        String id = generateID();
        holograms.put(id, new HologramItem(generateID(), item, getNewLoc(quirky), null));
    }

    public void insertItem(int index, Item item) {
        String id = generateID();
        Hologram hologram = getHologram(index);

        holograms.put(id, new HologramItem(generateID(), item, hologram.getLocation(), hologram));
    }

    public void removeItem(int index) {
        Iterator<Map.Entry<String, Hologram>> iterator = holograms.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }

        if (iterator.next().getValue().isItem()) {
            HologramItem hologramItem = (HologramItem) iterator.next();
            hologramItem.remove(RemoveType.ALL);
            iterator.remove();
        }
    }

    public void removeHologramText(HologramText hologramText) {
        hologramText.remove(RemoveType.ALL);
    }

    public void removeHologramItem(HologramItem hologramItem) {
        hologramItem.remove(RemoveType.ALL);
    }

    public Hologram getHologram(int index) {
        List<Map.Entry<String, Hologram>> entryList = new ArrayList<>(holograms.entrySet());
        Map.Entry<String, Hologram> entry = entryList.get(index);
        return entry.getValue();
    }

    public Hologram getHologram(ArmorStand armorStand) {
        for (Hologram hologram : holograms.values()) {
            if (hologram.getArmorStand().equals(armorStand)) return hologram;
        }
        return null;
    }

    public Location getNewLoc(double quirky) {
        return location.clone().add(0, (holograms.size() == 0 ? 0 : -quirky * holograms.size()), 0);
    }

    public String getBoxId() {
        return boxId;
    }

    public void spawnAll() {
        for (Hologram hologram : holograms.values()) {
            hologram.spawn(SpawnType.ALL);
        }
    }

    public void removeAll() {
        for (Hologram hologram : holograms.values()) {
            hologram.remove(RemoveType.ALL);
        }
        holograms.clear();
    }

    public LinkedHashMap<String, Hologram> getHolograms() {
        return holograms;
    }

    public String generateID() {
        return boxId + "-" + UUID.randomUUID();
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setHolograms(LinkedHashMap<String, Hologram> holograms) {
        this.holograms = holograms;
    }
}
