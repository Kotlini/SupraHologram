package fr.ylanouh.supraholograms.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CLocation {

    public static CLocation of(Location location) {
        final CLocation cLocation = new CLocation();
        cLocation.setX(location.getX());
        cLocation.setY(location.getY());
        cLocation.setZ(location.getZ());
        cLocation.setWorld(location.getWorld().getName());
        return cLocation;
    }

    private String world;
    private double x;
    private double y;
    private double z;

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ());
    }
}