package fr.ylanouh.supraholograms.builder;

import fr.ylanouh.supraholograms.SupraHolograms;
import fr.ylanouh.supraholograms.Utils;
import fr.ylanouh.supraholograms.hologram.HologramBox;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public final class HologramBuilder {

    private final HologramBox hologramBox;

    private double quirky;

    public HologramBuilder(String boxId, Location location) {
        this.hologramBox = new HologramBox(boxId, location);
        this.quirky = 0;
    }

    public HologramBuilder setQuirky(double d) {
        this.quirky = d;
        return this;
    }

    public HologramBuilder appendLines(Object... lines) {
        for (Object line : lines) {
            appendLine(line);
        }
        return this;
    }

    public HologramBuilder appendLine(Object line) {
        double q = quirky != 0 ? quirky : 0.3;
        if (Utils.isItem(line)) {
            hologramBox.appendItem(Utils.spawnItem((ItemStack) line, hologramBox.getLocation()), q);
        } else {
            hologramBox.appendText((String) line, q);
        }
        return this;
    }

    public HologramBuilder addSpace(int index, double quirky) {
        this.hologramBox.getHologram(index).addSpace(quirky);
        return this;
    }

    public HologramBuilder insertItem(ItemStack item, int index) {
        hologramBox.insertItem(index, Utils.spawnItem(item, hologramBox.getLocation()));
        return this;
    }

    public HologramBuilder register(SupraHolograms supraHolograms) {
        supraHolograms.add(hologramBox);
        return this;
    }

    public HologramBox build() {
        hologramBox.spawnAll();
        return this.hologramBox;
    }
}
