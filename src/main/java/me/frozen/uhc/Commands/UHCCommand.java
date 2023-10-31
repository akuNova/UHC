package me.frozen.uhc.Commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.frozen.uhc.Controllers.GameController;
import me.frozen.uhc.Models.GameTeam;
import me.frozen.uhc.Tasks.DisplayTimer;
import me.frozen.uhc.UHC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UHCCommand implements CommandExecutor {

    UHC plugin;

    public UHCCommand(UHC plugin) {
        this.plugin = plugin;
    }

    /* /uhc
            toggleteams - toggles teams
            checkmap <x> <z> - scans for ocean blocks
            start - Starts the game
            border
               create <size> [centerX] [centerZ]
            stage <stageIndex>
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("uhc")) return false;
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;
        switch (args[0].toLowerCase()) {
            case "toggleteams" -> {
                if (plugin.getGameController().getStatus() == GameController.Status.INACTIVE) {
                    plugin.getGameController().toggleTeams();
                    if (plugin.getGameController().isTeamsEnabled())
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.getInventory().setItem(0, plugin.getTeamsGUI().getOpenTeamMenuItem());
                            p.sendMessage("Teams have been enabled");

                            String newName = ((GameTeam.getTeam(player) != null) ? Objects.requireNonNull(GameTeam.getTeam(player)).getChatColor() : ChatColor.WHITE) + UHC.getSpecialPlayer(player);
                            PlayerProfile profile = player.getPlayerProfile();
                            profile.setName(newName);
                            player.setPlayerProfile(profile);
                            player.setDisplayName(newName);
                            player.setPlayerListName(newName);
                        }
                    else
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.getInventory().remove(plugin.getTeamsGUI().getOpenTeamMenuItem());
                            p.sendMessage("Teams have been disabled");
                            String newName = UHC.getSpecialPlayerFull(player);
                            PlayerProfile profile = player.getPlayerProfile();
                            profile.setName(newName);
                            player.setPlayerProfile(profile);
                            player.setDisplayName(newName);
                            player.setPlayerListName(newName);
                        }
                } else
                    player.sendMessage("Cannot toggle teams while game is live");
            }
            case "checkmap" -> {
                switch (args.length) {
                    case 3 -> {
                        player.sendMessage("This map contains " + plugin.getGameController().countBlocksOfOcean(player.getWorld(), new Location(player.getWorld(), Double.parseDouble(args[1]), 0, Double.parseDouble(args[2]))) + " blocks of ocean.");
                    }
                    default -> {
                        player.sendMessage("Usage: /uhc checkmap <x> <z>");
                    }
                }
            }
            case "start" -> {
                plugin.getGameController().startGame(player.getWorld());
            }
            case "border" -> {
                if (args.length == 1) return false;
                switch (args[1].toLowerCase()) {
                    case "create" -> {
                        if (args.length == 2) return false;
                        if (plugin.getGameBorder() != null) {
                            player.sendMessage("Game border already exists.");
                            return true;
                        }
                        if (args.length == 5) {
                            try {
                                int size = Integer.parseInt(args[2]);
                                double centerX = Double.parseDouble(args[3]);
                                double centerZ = Double.parseDouble(args[4]);

                                plugin.getGameController().getBorderController().createBorder(player.getWorld(), size, new Location(player.getWorld(), centerX, 0, centerZ));

                                player.sendMessage("Game border created with size " + size);
                                return true;
                            } catch (NumberFormatException e) {
                                player.sendMessage("Invalid arguments. Please provide numbers.");
                                return false;
                            }
                        }
                        if (args.length == 3) {
                            try {
                                int size = Integer.parseInt(args[2]);

                                plugin.getGameController().getBorderController().createBorder(player.getWorld(), size);

                                player.sendMessage("Game border created with size " + size);
                                return true;
                            } catch (NumberFormatException e) {
                                player.sendMessage("Invalid size argument. Please provide a number.");
                                return false;
                            }
                        } else {
                            player.sendMessage("Invalid command usage. Correct usage: /uhc border create <size> [center]");
                            return false;
                        }
                    }
                    case "move" -> {
                        // TODO: /uhc border move
                        player.sendMessage("TODO: /uhc border move");
                    }
                    default -> {
                        return false;
                    }
                }
            }
            case "stage" -> {
                if (args.length == 1) return false;
                int stage = Integer.parseInt(args[1]);
                if (stage > plugin.gameController.getStages().size() - 1) {
                    player.sendMessage("Invalid stage number");
                    return true;
                }
                plugin.gameController.setStageIndex(stage);
                plugin.gameController.getBorderController().loadStage(player.getWorld(), plugin.gameController.getStages().get(stage));
                DisplayTimer.loadTimer(plugin, plugin.gameController.getCurrentStage());
            }
            default -> {
                return false;
            }
        }

        return true;
    }
}
