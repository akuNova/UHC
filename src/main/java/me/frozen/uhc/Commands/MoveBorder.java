package me.frozen.uhc.Commands;

import me.frozen.uhc.Models.GameBorder;
import me.frozen.uhc.UHC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.frozen.uhc.Tasks.MoveBorder.moveBorder;

public class MoveBorder implements CommandExecutor {

    UHC plugin;

    public MoveBorder(UHC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // /moveborder <size> <time> <centerX> <centerZ>
        if (command.getName().equalsIgnoreCase("moveborder")) {
            if (sender instanceof Player player) {
                GameBorder gameBorder = plugin.getGameBorder();
                if (gameBorder == null) {
                    player.sendMessage("No border. Create one with /createborder");
                    return false;
                }
                double size, centerX = gameBorder.getCenter().getBlockX(), centerZ = gameBorder.getCenter().getBlockZ();
                double time = 0.05;
                if (args.length == 3) {
                    player.sendMessage("Invalid arguments. Usage: /moveborder <size> [time] [centerX] [centerZ]");
                    return false;
                }
                if (args.length == 4) {
                    try {
                        centerX = Double.parseDouble(args[2]);
                        centerZ = Double.parseDouble(args[3]);
                    } catch (NumberFormatException e) {
                        player.sendMessage("Invalid arguments. Usage: /moveborder <size> [time] [centerX] [centerZ]");
                        return false;
                    }
                }
                if (args.length >= 2) {
                    try {
                        time = Long.parseLong(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage("Invalid arguments. Usage: /moveborder <size> [time] [centerX] [centerZ]");
                        return false;
                    }
                }
                if (args.length >= 1) {
                    size = Double.parseDouble(args[0]);
                } else {
                    player.sendMessage("Invalid arguments. Usage: /moveborder <size> [time] [centerX] [centerZ]");
                    return false;
                }
                moveBorder(plugin, size, time, centerX, centerZ, plugin.getGameBorder().getBorderFloor(), plugin.getGameBorder().getBorderRoof());
                return true;
            }
        }
        return false;
    }
}
