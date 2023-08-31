package fr.ylanouh.supraholograms.interfaces;

import fr.ylanouh.supraholograms.config.CHologram;
import fr.ylanouh.supraholograms.config.CLocation;
import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.enums.UpdateType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Hologram extends Separable{
    String getId();

    @SuppressWarnings("unused")
    void setId(String id);

    void setLine(Object ob);

    Object getLine();

    void setLocation(Location location);

    Location getLocation();

    void setArmorStand(ArmorStand armorStand);

    ArmorStand getArmorStand();

    void spawn(SpawnType spawnType);

    void remove(RemoveType removeType);

    void update(UpdateType updateType);

    boolean isItem();

    boolean isText();

    boolean isPacket();

    String getType();

    default CHologram toConfig() {
        final CHologram cHologram = new CHologram();
        cHologram.setId(getId());
        cHologram.setLocation(CLocation.of(getLocation()));
        cHologram.setType(getType());
        cHologram.setLine(getType().equalsIgnoreCase("item") ? ((ItemStack) getLine()).getType().name()  : (String) getLine());
        return cHologram;
    }
}