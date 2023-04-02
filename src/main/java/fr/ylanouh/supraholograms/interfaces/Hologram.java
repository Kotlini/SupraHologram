package fr.ylanouh.supraholograms.interfaces;

import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.enums.UpdateType;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

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
}
