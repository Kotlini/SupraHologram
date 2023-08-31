package fr.ylanouh.supraholograms.config;

import fr.ylanouh.supraholograms.Utils;
import fr.ylanouh.supraholograms.hologram.HologramItem;
import fr.ylanouh.supraholograms.hologram.HologramText;
import fr.ylanouh.supraholograms.hologram.packets.HologramTextPacket;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CHologram {

    private String id;

    private String line;

    private CLocation location;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public CLocation getLocation() {
        return location;
    }

    public void setLocation(CLocation location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Hologram toHologram() {
        switch (getType()) {
            case "packet":
                return new HologramTextPacket(getId(), getLine(), getLocation().toLocation());
            case "item":
                return new HologramItem(getId(), Utils.spawnItem(new ItemStack(Material.valueOf(getLine())),
                        getLocation().toLocation()), getLocation().toLocation(), null);
            default:
                return new HologramText(getId(), getLine(), getLocation().toLocation());
        }
    }
}
