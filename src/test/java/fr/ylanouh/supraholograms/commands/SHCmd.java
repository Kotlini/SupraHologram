package fr.ylanouh.supraholograms.commands;

import fr.ylanouh.supraholograms.SupraHolograms;
import fr.ylanouh.supraholograms.Utils;
import fr.ylanouh.supraholograms.builder.HologramBuilder;
import fr.ylanouh.supraholograms.hologram.HologramBox;
import fr.ylanouh.supraholograms.messages.Prefix;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SHCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(Prefix.PREFIX_ERROR.getMessage() + "\n" +
                    "/sh add [boxId] text [text]\n" +
                    "/sh add [boxId] item [itemID/itemName]\n" +
                    "/sh insert [boxId] text [index] [text]\n" +
                    "/sh insert [boxId] item [index] [itemID/itemName]\n" +
                    "/sh remove [boxId] [index]\n" +
                    "/sh remove [boxId]\n" +
                    "/sh create [boxId] [text]");
            return false;
        }
        HologramBox hologramBox = boxIdExist(player, args[1]);

        if ((!args[0].equals("create")) && hologramBox == null) return false;

        switch (args[0]) {
            case "add":
                if (args.length != 4) {
                    player.sendMessage(Prefix.PREFIX_ERROR.getMessage() + "\n" +
                            "/sh add [boxId] text [text]\n" +
                            "/sh add [boxId] item [itemID/itemName]");
                }

                if (args[2].equals("item")) {
                    hologramBox.appendItem(Utils.spawnItem(new ItemStack(Material.matchMaterial(args[3])), hologramBox.getLocation()), true);
                    return true;
                }

                if (args[2].equals("text")) {
                    hologramBox.appendText(args[3], true);
                    return true;
                }

                return false;
            case "insert":
                if (args.length != 5 && !args[2].equals("text") || !args[2].equals("item")) {
                    player.sendMessage(Prefix.PREFIX_ERROR.getMessage() + "\n" +
                            "/sh insert [boxId] text [index] [string]\n" +
                            "/sh insert [boxId] item [index] [itemID/itemName]");
                    return false;
                }

                int index = Integer.parseInt(args[3]);

                if (args[2].equals("text")) {
                    hologramBox.insertText(index, getStringAt(3, args));
                } else {
                    if (hologramBox.getHologram(index).isText()) return false;
                    hologramBox.insertItem(index, Utils.spawnItem(new ItemStack(Material.matchMaterial(args[4])), hologramBox.getLocation()));
                }
                break;
            case "remove":
                if (args.length == 2) {
                    hologramBox.removeAll();
                }

                if (args.length < 3) {
                    player.sendMessage(Prefix.PREFIX_ERROR.getMessage() + "\n" +
                            "/sh remove [boxId] [index]");
                    return false;
                }

                hologramBox.removeLine(Integer.parseInt(args[2]));
                break;
            case "create":
                if (args.length < 3) {
                    player.sendMessage(Prefix.PREFIX_ERROR.getMessage() + "\n" +
                            "/sh create [boxId] [text]");
                    return false;
                }

                new HologramBuilder(args[1], player.getLocation()).appendLines(getStringArrayAt(2, args)).build();
                break;
            default:
                break;
        }
        return false;
    }


    private HologramBox boxIdExist(Player player, String id) {
        HologramBox hologramBox = SupraHolograms.getInstance().getHologramsBox(id);
        if (hologramBox == null) {
            player.sendMessage(Prefix.PREFIX_ERROR.getMessage() + "§fid: " + id + " no exist.");
            return null;
        }

        return hologramBox;
    }

    private String getStringAt(int index, String[] args) {
        StringBuilder str = new StringBuilder();
        for (int x = index; x < args.length; x++) {
            str.append(args[x]).append(" ");
        }
        return str.toString();
    }

    private String[] getStringArrayAt(int index, String[] args) {
        String[] array = new String[args.length - index];

        if (args.length - index >= 0) System.arraycopy(args, index, array, 0, args.length - index);
        return array;
    }
}
