package fr.ylanouh.supraholograms.hologram;

import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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

    public void appendText(String name) {
        appendText(name, 0.3);
    }
    public void appendText(String name, double quirky) {
        String id = generateID();
        holograms.put(id, new HologramText(generateID(), name, getNewLoc(quirky)));
    }

    public void insertText(int index, String line) {
        Hologram hologram = getHologram(index);
        if (hologram.isItem()) return;

        hologram.setLine(line);
    }

    public void removeText(int index) {
        Iterator<Map.Entry<String, Hologram>> iterator = holograms.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }

        Hologram hologram = iterator.next().getValue();

        if (hologram.isText()) {
            HologramText hologramItem = (HologramText) hologram;
            hologramItem.remove(RemoveType.ALL);
            holograms.remove(hologram.getId());
            iterator.remove();
        }
    }

    public void appendItem(Item item) {
        appendItem(item, 0.3);
    }

    public void appendItem(Item item, double quirky) {
        String id = generateID();
        holograms.put(id, new HologramItem(generateID(), item, getNewLoc(quirky), null));
    }

    public void insertItem(int index, Item item) {
        Hologram hologram = getHologram(index);
        if (hologram.isText()) return;

        Entity entity = hologram.getArmorStand().getPassenger();

        if (entity != null) entity.remove();

        hologram.getArmorStand().remove();
        hologram.setLine(item);

        hologram.spawn(SpawnType.ALL);
    }

    public void removeItem(int index) {
        Iterator<Map.Entry<String, Hologram>> iterator = holograms.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }

        Hologram hologram = iterator.next().getValue();

        if (hologram.isItem()) {
            HologramItem hologramItem = (HologramItem) hologram;
            hologramItem.remove(RemoveType.ALL);
            holograms.remove(hologram.getId());
            iterator.remove();
        }
    }

    public Hologram getHologram(int index) {
        List<Map.Entry<String, Hologram>> entryList = new ArrayList<>(holograms.entrySet());
        Map.Entry<String, Hologram> entry = entryList.get(index);
        return entry.getValue();
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    @SuppressWarnings("unused")
    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @SuppressWarnings("unused")
    public void setHolograms(LinkedHashMap<String, Hologram> holograms) {
        this.holograms = holograms;
    }
}
