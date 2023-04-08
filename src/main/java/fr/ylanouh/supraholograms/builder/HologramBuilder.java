package fr.ylanouh.supraholograms.builder;

import fr.ylanouh.supraholograms.SupraHolograms;
import fr.ylanouh.supraholograms.Utils;
import fr.ylanouh.supraholograms.hologram.HologramBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class HologramBuilder {

    private final HologramBox hologramBox;

    private double quirky;

    public HologramBuilder(String boxId, Location location) {
        this.hologramBox = new HologramBox(boxId, location);
        this.quirky = 0.3;
    }

    public HologramBuilder setQuirky(double d) {
        this.quirky = d;
        return this;
    }

    public HologramBuilder addViewer(Player player) {
        hologramBox.addViewer(player);
        return this;
    }

    public HologramBuilder addViewer(Player player, int index) {
        hologramBox.addViewer(player, index);
        return this;
    }

    public HologramBuilder appendLinePacket(String line) {
        double q = quirky != 0 ? quirky : 0.3;
        hologramBox.appendTextPacket(line, q, false);
        return this;
    }

    public HologramBuilder appendLinesPacket(String... lines) {
        for (String line : lines) appendLinePacket(line);
        return this;
    }

    public HologramBuilder appendLines(String... lines) {
        for (String line : lines) appendLine(line);
        return this;
    }

    public HologramBuilder appendLines(ItemStack... items) {
        for (ItemStack item : items) appendLine(item);
        return this;
    }

    public HologramBuilder appendLine(ItemStack item) {
        double q = quirky != 0 ? quirky : 0.3;
        hologramBox.appendItem(Utils.spawnItem(item, hologramBox.getLocation()), q, false);
        return this;
    }

    public HologramBuilder appendLine(String line) {
        double q = quirky != 0 ? quirky : 0.3;
        hologramBox.appendText(line, q, false);
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

    public HologramBox build() {
        SupraHolograms.getInstance().add(hologramBox);
        hologramBox.spawnAll();
        return this.hologramBox;
    }
}
