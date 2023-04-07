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
        Player player = event.getPlayer();
        HologramBox hologramBox = SupraHolograms.getInstance().getHologramsBox("test-4");
        hologramBox.addViewer(player);
        hologramBox.replaceLinePacket("%player%", player.getName(), player, 2);
        hologramBox.replaceLinePacket("%ip%",
                 player.getAddress().getAddress().getHostAddress(), player, 1);
        hologramBox.replaceLinePacket("%uuid%",
                player.getUniqueId().toString(), player, 0);

        if (player.getName().equals("Ylanouh")) {
            SupraHolograms.getInstance().getHologramsBox("test-5").addViewer(player);
        }
    }
}
