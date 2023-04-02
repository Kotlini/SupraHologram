package fr.ylanouh.supraholograms.hologram;

import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.enums.UpdateType;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;

public class HologramItem implements Hologram {

    private String id;

    private Item line;

    private Location location;

    private Hologram parent;

    private ArmorStand armorStand;

    private double space;

    public HologramItem(String id, Item line, Location location, Hologram parent) {
        this.id = id;
        this.line = line;
        this.parent = parent;
        this.armorStand = null;
        this.space = 0;
        this.location = location;
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
    public void setLine(Object ob) {
        this.line = (Item) ob;
    }

    @Override
    public Object getLine() {
        return this.line;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void spawn(SpawnType spawnType) {
        switch (spawnType) {
            case ALL:
                if (getParent() != null) {
                    ArmorStand armor = getParent().getArmorStand();
                    armor.teleport(getLocation().add(0, space, 0));
                    armor.setPassenger(line);
                    return;
                }

                addArmorStand();
                break;
            case PARENT:
                setArmorStand(null);
                getParent().getArmorStand().setPassenger(line);
                break;
            case ARMORSTAND:
                setParent(null);
                addArmorStand();
                break;
            default:
                break;
        }
    }

    @Override
    public void remove(RemoveType removeType) {
        if (removeType.equals(RemoveType.ALL)) {
            line.remove();

            if (getArmorStand() != null) {
                getArmorStand().remove();
            }
        }
    }

    public void setParent(Hologram parent) {
        this.parent = parent;
    }

    public Hologram getParent() {
        return parent;
    }

    @Override
    public void update(UpdateType updateType) {
        if (updateType.equals(UpdateType.LOCATION)) {
            if (parent != null) return;

            setLocation(armorStand.getLocation());
        }
    }

    @Override
    public boolean isItem() {
        return true;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public void addSpace(double space) {
        this.space = space;
    }

    @Override
    public void removeSpace() {
        this.space = 0;
    }

    @Override
    public double getSpace() {
        return this.space;
    }

    @Override
    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        if (armorStand == null) {
            spawn(SpawnType.ARMORSTAND);
            return;
        }

        this.armorStand.teleport(location);
    }

    private void addArmorStand() {
        setArmorStand(getLocation().getWorld()
                .spawn(getLocation().clone().add(0, space, 0), ArmorStand.class));
        getArmorStand().setGravity(false);
        getArmorStand().setCustomNameVisible(false);
        getArmorStand().setVisible(false);
        getArmorStand().setBasePlate(false);
        getArmorStand().setSmall(true);
        getArmorStand().setCanPickupItems(false);

        getArmorStand().setPassenger(line);
    }
}
