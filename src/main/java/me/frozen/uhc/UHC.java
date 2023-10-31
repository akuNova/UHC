package me.frozen.uhc;

import me.frozen.uhc.Commands.MoveBorder;
import me.frozen.uhc.Commands.UHCCommand;
import me.frozen.uhc.Controllers.GameController;
import me.frozen.uhc.Events.*;
import me.frozen.uhc.GUIs.TeamsGUI;
import me.frozen.uhc.Models.GameBorder;
import me.frozen.uhc.Models.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import static org.bukkit.block.Biome.*;

public final class UHC extends JavaPlugin {

    public GameController gameController;
    public final int INITIAL_BORDER_SIZE = 1000;
    public final Biome[] BIOMES_DONT_WANT = {
            COLD_OCEAN,
            DEEP_COLD_OCEAN,
            DEEP_FROZEN_OCEAN,
            DEEP_LUKEWARM_OCEAN,
            DEEP_OCEAN,
            FROZEN_OCEAN,
            LUKEWARM_OCEAN,
            OCEAN,
            WARM_OCEAN,
    };
    private TeamsGUI teamsGUI;
    private static Scoreboard scoreboard;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("moveborder").setExecutor(new MoveBorder(this));
        getCommand("uhc").setExecutor(new UHCCommand(this));

        getServer().getPluginManager().registerEvents(new EntityRegainHealth(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerRightClick(), this);
        getServer().getPluginManager().registerEvents(new BeforeGameStart(this), this);
        teamsGUI = new TeamsGUI(this);
        getServer().getPluginManager().registerEvents(teamsGUI, this);
        getServer().getPluginManager().registerEvents(new Teams(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);

        gameController = new GameController(this);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();

        loadTeams();

        for (Player player : Bukkit.getOnlinePlayers()) {
            String fullName = UHC.getSpecialPlayerFull(player);

            player.setDisplayName(fullName);
            player.setPlayerListName(fullName);
            player.getInventory().remove(getTeamsGUI().getOpenTeamMenuItem());
            player.setScoreboard(scoreboard);

        }
    }

    private void loadTeams() {
        new GameTeam("Red", ChatColor.RED);
        new GameTeam("Orange", ChatColor.GOLD);
        new GameTeam("Yellow", ChatColor.YELLOW);
        new GameTeam("Lime", ChatColor.GREEN);
        new GameTeam("Green", ChatColor.DARK_GREEN);
        new GameTeam("Cyan", ChatColor.DARK_AQUA);
        new GameTeam("Aqua", ChatColor.AQUA);
        new GameTeam("Blue", ChatColor.BLUE);
        new GameTeam("Purple", ChatColor.DARK_PURPLE);
        new GameTeam("Pink", ChatColor.LIGHT_PURPLE);
        scoreboard.registerNewTeam("Red");
        scoreboard.registerNewTeam("Orange");
        scoreboard.registerNewTeam("Yellow");
        scoreboard.registerNewTeam("Lime");
        scoreboard.registerNewTeam("Green");
        scoreboard.registerNewTeam("Cyan");
        scoreboard.registerNewTeam("Aqua");
        scoreboard.registerNewTeam("Blue");
        scoreboard.registerNewTeam("Purple");
        scoreboard.registerNewTeam("Pink");
        scoreboard.getTeam("Red").setColor(ChatColor.RED);
        scoreboard.getTeam("Orange").setColor(ChatColor.GOLD);
        scoreboard.getTeam("Yellow").setColor(ChatColor.YELLOW);
        scoreboard.getTeam("Lime").setColor(ChatColor.GREEN);
        scoreboard.getTeam("Green").setColor(ChatColor.DARK_GREEN);
        scoreboard.getTeam("Cyan").setColor(ChatColor.DARK_AQUA);
        scoreboard.getTeam("Aqua").setColor(ChatColor.AQUA);
        scoreboard.getTeam("Blue").setColor(ChatColor.BLUE);
        scoreboard.getTeam("Purple").setColor(ChatColor.DARK_PURPLE);
        scoreboard.getTeam("Pink").setColor(ChatColor.LIGHT_PURPLE);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public TeamsGUI getTeamsGUI() {
        return teamsGUI;
    }

    public void setTeamsGUI(TeamsGUI teamsGUI) {
        this.teamsGUI = teamsGUI;
    }

    public GameController getGameController() {
        return gameController;
    }

    public GameBorder getGameBorder() {
        return gameController.getBorderController().getGameBorder();
    }

    public static String getSpecialPlayer(Player player) {
        if (player.getUniqueId() == Bukkit.getPlayerUniqueId("akuNova")) {
            return "Nova";
        } else {
            return null;
        }
    }

    public static String getSpecialPlayerFull(Player player) {
        String newName = UHC.getSpecialPlayer(player);
        if (newName == null) return null;
        String prefix = "", suffix = "";
        switch (newName) {
            case "Nova" -> {
                prefix = ChatColor.DARK_AQUA + "" + ChatColor.BOLD;
                suffix = "";
            }
        }
        return prefix + newName + suffix;
    }
}
