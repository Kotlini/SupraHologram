package fr.ylanouh.supraholograms.listeners;

import fr.ylanouh.supraholograms.SupraHolograms;
import fr.ylanouh.supraholograms.hologram.HologramBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinPlayer implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final HologramBox hologramBox = SupraHolograms.getInstance().getHologramsBox("personal");
        hologramBox.addViewer(player);
        hologramBox.replaceLinePacket("%kills%", "2", player, 2);
        hologramBox.replaceLinePacket("%deaths%", "3", player, 3);
        hologramBox.replaceLinePacket("%kd%", "4", player, 4);
        hologramBox.replaceLinePacket("%wbt%","5", player, 5);
        hologramBox.replaceLinePacket("%tp%", "6", player, 6);
        hologramBox.replaceLinePacket("%wins%", "7", player, 7);
        hologramBox.replaceLinePacket("%looses%", "8", player, 8);
        hologramBox.replaceLinePacket("%lw%", "9", player, 9);
        hologramBox.replaceLinePacket("%ws%", "10", player, 10);
    }
}
