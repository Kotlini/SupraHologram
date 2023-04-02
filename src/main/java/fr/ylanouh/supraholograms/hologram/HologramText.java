package fr.ylanouh.supraholograms.hologram;

import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.enums.UpdateType;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class HologramText implements Hologram {

    private String id, line;

    private Location location;

    private ArmorStand armorStand;

    private double space;

    private HologramItem hologramItem;

    public HologramText(String id, String line, Location location) {
        this.id = id;
        this.line = line;
        this.location = location;
        this.armorStand = null;
        this.space = 0;
        this.hologramItem = null;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object getLine() {
        return this.line;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public ArmorStand getArmorStand() {
        return this.armorStand;
    }

    @Override
    public void spawn(SpawnType spawnType) {
        if (armorStand == null || spawnType.equals(SpawnType.ALL)) {
            setArmorStand(getLocation().getWorld().spawn(getLocation().clone().add(0, space, 0), ArmorStand.class));
            getArmorStand().setGravity(false);
            getArmorStand().setCustomNameVisible(!getLine().equals(""));
            getArmorStand().setCustomName(ChatColor.translateAlternateColorCodes('&', line));
            getArmorStand().setVisible(false);
            getArmorStand().setBasePlate(false);
            getArmorStand().setSmall(true);
            getArmorStand().setCanPickupItems(false);
            return;
        }

        getArmorStand().setCustomNameVisible(true);
    }

    @Override
    public void remove(RemoveType removeType) {
        if (armorStand == null) return;

        if (removeType.equals(RemoveType.NAME)) {
            armorStand.setCustomNameVisible(false);
            return;
        }

        armorStand.remove();
    }

    @Override
    public void update(UpdateType updateType) {
        if (armorStand == null) {
            spawn(SpawnType.ALL);
            return;
        }

        switch (updateType) {
            case NAME:
                armorStand.setCustomName(line);
                armorStand.setCustomNameVisible(true);
                break;
            case LOCATION:
                armorStand.teleport(location);
                break;
            case ALL:
                armorStand.teleport(location);
                armorStand.setCustomName(line);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isItem() {
        return false;
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    public void setLine(Object line) {
        this.line = (String) line;

        update(UpdateType.NAME);
    }

    @Override
    public void addSpace(double space) {
        this.space = space;
    }

    @Override
    public void removeSpace() {
        if (armorStand != null) update(UpdateType.LOCATION);
        this.space = 0;
    }

    @Override
    public double getSpace() {
        return space;
    }

    public HologramItem getHologramItem() {
        return hologramItem;
    }

    public void setHologramItem(HologramItem hologramItem) {
        this.hologramItem = hologramItem;
    }

    public boolean asHologramItem() {
        return this.hologramItem != null;
    }
}
