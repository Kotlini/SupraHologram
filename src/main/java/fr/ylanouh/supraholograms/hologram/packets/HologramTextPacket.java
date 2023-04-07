package fr.ylanouh.supraholograms.hologram.packets;

import fr.ylanouh.supraholograms.SupraHolograms;
import fr.ylanouh.supraholograms.Utils;
import fr.ylanouh.supraholograms.enums.RemoveType;
import fr.ylanouh.supraholograms.enums.SpawnType;
import fr.ylanouh.supraholograms.enums.UpdateType;
import fr.ylanouh.supraholograms.interfaces.Hologram;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class HologramTextPacket implements Hologram {

    private String id, line;

    private Location location;

    private EntityArmorStand entityArmorStand;

    private double space;

    private final Set<Player> viewers;

    public HologramTextPacket(String id, String line, Location location) {
        this.id = id;
        this.line = line;
        this.location = location;
        this.entityArmorStand = null;
        viewers = new HashSet<>();
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
        this.line = (String) ob;
    }

    @Override
    public Object getLine() {
        return this.line;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        if (entityArmorStand == null) {
            spawn(SpawnType.ALL);
            return;
        }

        update(UpdateType.LOCATION);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setArmorStand(ArmorStand armorStand) {}

    @Override
    public ArmorStand getArmorStand() {
        return null;
    }

    @Override
    public void spawn(SpawnType spawnType) {
        if (spawnType.equals(SpawnType.CLIENT)) {
            if (entityArmorStand == null) return;

            viewers.forEach(this::show);
            return;
        }

        if (spawnType.equals(SpawnType.NAME)) {
            if (entityArmorStand == null) return;

            entityArmorStand.setCustomNameVisible(true);
            return;
        }

        if (spawnType.equals(SpawnType.ARMORSTAND)) {
            if (entityArmorStand != null) {
                remove(RemoveType.ALL);
            }

            EntityArmorStand armorStand;
            try {
                armorStand = new EntityArmorStand(Utils.toNMSWorld(location.getWorld()),
                        getLocation().getX(), getLocation().getY(), getLocation().getZ());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            armorStand.setCustomName(line);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            armorStand.setBasePlate(false);
            armorStand.setInvisible(true);
            armorStand.setSmall(true);
            armorStand.setPosition(location.getX(), location.getY(), location.getZ());
            entityArmorStand = armorStand;
        }

        if (spawnType.equals(SpawnType.ALL)) {
            spawn(SpawnType.ARMORSTAND);
            spawn(SpawnType.NAME);
            spawn(SpawnType.CLIENT);
        }
    }

    @Override
    public void remove(RemoveType removeType) {
        if (entityArmorStand == null) return;

        if (removeType.equals(RemoveType.CLIENT)) {
            for (Player player : viewers) {
                hide(player, false);
            }
            viewers.clear();
            return;
        }

        if (removeType.equals(RemoveType.NAME)) {
            entityArmorStand.setCustomNameVisible(false);
            return;
        }

        if (removeType.equals(RemoveType.ALL)) {
            remove(RemoveType.CLIENT);
            entityArmorStand.getWorld().removeEntity(entityArmorStand);
        }
    }

    @Override
    public void update(UpdateType updateType) {
        if (entityArmorStand == null) {
            spawn(SpawnType.CLIENT);
            return;
        }

        switch (updateType) {
            case NAME:
                entityArmorStand.setCustomName(line);
                entityArmorStand.setCustomNameVisible(true);
                break;
            case LOCATION:
                teleport();
                break;
            case ALL:
                update(UpdateType.LOCATION);
                update(UpdateType.NAME);
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
    public boolean isPacket() {
        return true;
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

    public void show(Player player) {
        if (entityArmorStand == null) return;
        if (viewers.contains(player)) return;

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
        viewers.add(player);
    }

    public void hide(Player player, boolean removedInCache) {
        if (entityArmorStand == null) return;
        if (!viewers.contains(player)) return;

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityArmorStand.getId()));
        if (removedInCache) {
            viewers.remove(player);
        }
    }

    public void changeLinePlayer(Player player, String line) {
        if (entityArmorStand == null) return;
        if (!viewers.contains(player)) return;

        EntityArmorStand entityArmorStandTemp = entityArmorStand;
        entityArmorStandTemp.setCustomName(line);
        entityArmorStand.setCustomNameVisible(true);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStandTemp));
    }

    public void changeLine(String line) {
        if (entityArmorStand == null) return;

        for (Player player : viewers) {
            changeLinePlayer(player, line);
        }
    }

    public void teleport() {
        if (entityArmorStand == null) return;

        double posX = MathHelper.floor(location.getX() * 32.0D) / 32.0D;
        double posY = MathHelper.floor(location.getY() * 32.0D) / 32.0D;
        double posZ = MathHelper.floor(location.getZ() * 32.0D) / 32.0D;
        Bukkit.getScheduler().runTask(SupraHolograms.getInstance().getPlugin(), () ->
                entityArmorStand.setPosition(posX, posY, posZ));
    }
}
