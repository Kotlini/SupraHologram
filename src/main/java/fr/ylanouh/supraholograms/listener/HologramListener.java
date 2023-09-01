package fr.ylanouh.supraholograms.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class HologramListener implements Listener {

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        if (event.getEntity().getName().equals("SUPRAHOLOGRAMS")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (!event.getItem().getName().equals("SUPRAHOLOGRAMS")) return;

        event.setCancelled(true);
    }
}