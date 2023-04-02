package fr.ylanouh.supraholograms.commands;

import fr.ylanouh.supraholograms.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import test.fr.ylanouh.supraholograms.SupraHologramsTests;

import java.util.Arrays;

public class TestCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Location location;
        if(sender instanceof Player) location = ((Player) sender).getLocation();
        else location = new Location(Bukkit.getWorld("world"), 0, 150, 0);

        try {
            sender.sendMessage("Starting test at your position");
            SupraHologramsTests.prepare(location);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), ()->{
                new SupraHologramsTests().testAll();
                sender.sendMessage("Tests success");
            });
        } catch (Exception e) {
            sender.sendMessage("Tests failed with errors: " + e);
            sender.sendMessage(Arrays.toString(e.getStackTrace()));
        }

        return false;
    }
}