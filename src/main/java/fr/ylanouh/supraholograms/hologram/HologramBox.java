package fr.ylanouh.supraholograms.hologram;

import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.enums.UpdateType;
import fr.ylanouh.supraholograms.hologram.packets.HologramTextPacket;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

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

    public void add(Hologram hologram) {
        this.holograms.put(hologram.getId(), hologram);
    }

    public void appendText(String name, boolean spawn) {
        appendText(name, 0.3, spawn);
    }

    public void appendText(String name, double quirky, boolean spawn) {
        String id = generateID();
        Hologram hologram = new HologramText(generateID(), name, getNewLoc(quirky));
        holograms.put(id, hologram);

        if (spawn) {
            hologram.spawn(SpawnType.ALL);
        }
    }

    public void insertText(int index, String line) {
        Hologram hologram = getHologram(index);
        if (hologram.isItem()) return;

        hologram.setLine(line);
        hologram.update(UpdateType.NAME);
    }

    public void removeText(int index) {
        Hologram hologram = getHologram(index);

        if (hologram == null) return;

        if (hologram.isText()) {
            hologram.remove(RemoveType.ALL);
            holograms.remove(hologram.getId());
            calculatePosition(index);
        }
    }

    public void appendItem(Item item, boolean spawn) {
        appendItem(item, 0.2, spawn);
    }

    public void appendItem(Item item, double quirky, boolean spawn) {
        final String id = generateID();
        final HologramItem hologramItem = new HologramItem(id, item, getNewLoc(quirky), null);
        holograms.put(id, hologramItem);

        if (spawn) {
            hologramItem.spawn(SpawnType.ALL);
        }
    }

    public void insertItem(int index, Item item) {
        Hologram hologram = getHologram(index);
        if (hologram.isText()) return;

        Entity entity = hologram.getArmorStand().getPassenger();

        if (entity != null) entity.remove();

        hologram.getArmorStand().remove();
        hologram.setLine(item);

        hologram.spawn(SpawnType.ALL);
        calculatePosition(index);
    }

    public void removeLine(int index) {
        Hologram hologram = getHologram(index);
        if (hologram == null) return;

        if (hologram.isText()) {
            removeText(index);
        } else {
            removeItem(index);
        }
        calculatePosition(index);
    }

    public void removeItem(int index) {
        Hologram hologram = getHologram(index);

        if (hologram == null) return;

        if (hologram.isItem()) {
            hologram.remove(RemoveType.ALL);
            holograms.remove(hologram.getId());
            calculatePosition(index);
        }
    }

    public void appendHologram(Hologram hologram, boolean spawn) {
        holograms.put(hologram.getId(), hologram);

        if (spawn) {
            hologram.spawn(SpawnType.ALL);
        }
    }

    public void appendTextPacket(String name, double quirky, boolean spawn) {
        final String id = generateID();
        final Hologram hologram = new HologramTextPacket(id, ChatColor.translateAlternateColorCodes('&', name),
                getNewLoc(quirky));
        holograms.put(id, hologram);

        if (spawn) {
            hologram.spawn(SpawnType.ALL);
        }
    }

    public void appendTextPacket(String name, boolean spawn) {
        appendTextPacket(name, 0.3, spawn);
    }

    public void insertTextPacket(String name, int index) {
        Hologram hologram = getHologram(index);
        if (!hologram.isPacket()) return;

        if (hologram.isPacket()) {
            hologram.setLine(ChatColor.translateAlternateColorCodes('&', name));
            hologram.update(UpdateType.NAME);
        }
    }

    public void removeTextPacket(int index) {
        Hologram hologram = getHologram(index);

        if (hologram == null) return;

        if (hologram.isPacket()) {
            hologram.remove(RemoveType.ALL);
            holograms.remove(hologram.getId());
            calculatePosition(index);
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
        int size = holograms.size() - 1;
        double space = getAllSpace(size);
        double positionMultiply = quirky * holograms.size();
        double itemSpace = getAllItems(size);

        return getLocation().clone().add(0, space + positionMultiply + itemSpace, 0);
    }

    public String getBoxId() {
        return boxId;
    }

    public void spawnAll() {
        System.out.println("holograms count in " + getBoxId() + " " + holograms.size());
        for (int i = 0; i < holograms.size(); i++) {
            System.out.println("Spawn holo n°" + i + " is null:" + getHologram(i));
            getHologram(i).spawn(SpawnType.ALL);
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

    public void calculatePosition(int index) {
        for (int h = index; h < holograms.size(); h++) {
            Hologram hologram = getHologram(h);
            double space = getAllSpace(h);
            double positionMultiply = (hologram.isText() ? 0.3 : 0.2) * h;
            double itemSpace = getAllItems(h);

            hologram.setLocation(getLocation().clone().add(0, space + positionMultiply + itemSpace, 0));
        }
    }

    private double getAllSpace(int index) {
        double space = 0;
        for (int h = index; h > 0; h--) {
            Hologram hologram = getHologram(h);
            if (hologram == null) continue;
            space = space + hologram.getSpace();
        }
        return space;
    }

    private double getAllItems(int index) {
        double items = 0;
        for (int h = index; h > 0; h--) {
            Hologram hologram = getHologram(h);
            if (hologram == null) continue;
            if (hologram.getArmorStand() == null) continue;
            if (hologram.getArmorStand().getPassenger() == null) continue;

            items = items + 0.2;
        }
        return items;
    }


    public void addViewer(Player player) {
        for (Hologram hologram : holograms.values()) {
            if (!hologram.isPacket()) continue;
            ((HologramTextPacket) hologram).show(player);
        }
    }

    public void removeViewer(Player player) {
        for (Hologram hologram : holograms.values()) {
            if (hologram.isPacket()) {
                ((HologramTextPacket) hologram).hide(player, true);
            }
        }
    }

    public void addViewer(Player player, int index) {
        Hologram hologram = getHologram(index);
        if (hologram == null || !hologram.isPacket()) return;

        ((HologramTextPacket) hologram).show(player);
    }

    public void removeViewer(Player player,  int index) {
        Hologram hologram = getHologram(index);
        if (hologram == null || !hologram.isPacket()) return;

        ((HologramTextPacket) hologram).hide(player, true);
    }

    public void changeLinePacket(Player player, String line, int index) {
        Hologram hologram = getHologram(index);
        if (hologram == null) return;

        if (hologram.isPacket()) {
            ((HologramTextPacket) hologram).changeLinePlayer(player, line);
        }
    }

    public void replaceLinePacket(String placeholder, String replace, Player player, int index) {
        Hologram hologram = getHologram(index);
        if (hologram == null) return;

        if (hologram.isPacket()) {
            ((HologramTextPacket) hologram).changeLinePlayer(player,
                    ((String) hologram.getLine()).replace(placeholder, replace));
        }
    }

    public String getLines(int index) {
        Hologram hologram = getHologram(index);
        if (hologram == null) return "holograms is null";

        if (!hologram.isItem()) return (String) hologram.getLine();
        return "";
    }
}