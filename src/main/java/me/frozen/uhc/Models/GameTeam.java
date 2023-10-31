package me.frozen.uhc.Models;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.frozen.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameTeam {
    private static List<GameTeam> allTeams = new ArrayList<GameTeam>();
    private static HashMap<UUID, GameTeam> playerTeams = new HashMap<>();
    private String teamName;
    private ChatColor color;

    public GameTeam(String teamName, ChatColor color) {
        this.teamName = teamName;
        this.color = color;
        allTeams.add(this);
    }

    public static ArrayList<Player> getPlayersOnTeam(GameTeam team) {
        ArrayList<Player> players = new ArrayList<>();
        for (UUID uuid : playerTeams.keySet())
            if (playerTeams.get(uuid).equals(team))
                players.add(Bukkit.getPlayer(uuid));
        return players;
    }

    public ArrayList<Player> getPlayersOnTeam() {
        return getPlayersOnTeam(this);
    }

    public boolean isEmpty() {
        return playerTeams.containsValue(this);
    }

    public static ArrayList<GameTeam> getActiveTeams() {
        ArrayList<GameTeam> activeTeams = new ArrayList<>();
        for (GameTeam team : GameTeam.getAllTeams())
            if (!team.isEmpty()) activeTeams.add(team);
        return activeTeams;
    }

    public void joinTeam(Player player) {
        if (GameTeam.hasTeam(player)) this.remove(player);
        Objects.requireNonNull(UHC.getScoreboard().getTeam(teamName)).addEntity(player);

        String newName = color + UHC.getSpecialPlayer(player);
        System.out.println(newName);
        PlayerProfile profile = player.getPlayerProfile();
        profile.setName(newName);
        player.setPlayerProfile(profile);
        player.setDisplayName(newName);
        player.setPlayerListName(newName);
        this.add(player);
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public String getTeamName() {
        return teamName;
    }

    public static List<GameTeam> getAllTeams() {
        return allTeams;
    }

    public static void setAllTeams(List<GameTeam> allTeams) {
        GameTeam.allTeams = allTeams;
    }

    public static HashMap<UUID, GameTeam> getPlayerTeams() {
        return playerTeams;
    }

    public static void setPlayerTeams(HashMap<UUID, GameTeam> playerTeams) {
        GameTeam.playerTeams = playerTeams;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    private void add(Player player) {
        playerTeams.put(player.getUniqueId(), this);
    }

    public boolean remove(Player player) {
        if (!hasTeam(player)) return false;
        Objects.requireNonNull(UHC.getScoreboard().getTeam(teamName)).removeEntity(player);
        playerTeams.remove(player.getUniqueId());
        String newName = ChatColor.WHITE + UHC.getSpecialPlayer(player);
        PlayerProfile profile = player.getPlayerProfile();
        profile.setName(newName);
        player.setPlayerProfile(profile);
        player.setDisplayName(newName);
        player.setPlayerListName(newName);
        return true;
    }

    public static boolean hasTeam(Player player) {
        return playerTeams.containsKey(player.getUniqueId());
    }

    public static GameTeam getTeam(Player player) {
        if (!hasTeam(player)) return null;
        return playerTeams.get(player.getUniqueId());
    }
    public static GameTeam getTeam(String name) {
        for (GameTeam t : allTeams)
            if (t.teamName.equalsIgnoreCase(name))
                return t;
        return null;
    }

    public ChatColor getChatColor() {
        return color;
    }
    public GlowAPI.Color getGlowAPIColor() {
        switch (getChatColor()) {
            case RED -> {
                return GlowAPI.Color.RED;
            }
            case GOLD -> {
                return GlowAPI.Color.GOLD;
            }
            case YELLOW -> {
                return GlowAPI.Color.YELLOW;
            }
            case GREEN -> {
                return GlowAPI.Color.GREEN;
            }
            case DARK_GREEN -> {
                return GlowAPI.Color.DARK_GREEN;
            }
            case DARK_AQUA -> {
                return GlowAPI.Color.DARK_AQUA;
            }
            case AQUA -> {
                return GlowAPI.Color.AQUA;
            }
            case BLUE -> {
                return GlowAPI.Color.BLUE;
            }
            case DARK_PURPLE -> {
                return GlowAPI.Color.DARK_PURPLE;
            }
            case LIGHT_PURPLE -> {
                return GlowAPI.Color.PURPLE;
            }
        }
        return null;
    }
}
