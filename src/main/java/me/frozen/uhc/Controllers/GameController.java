package me.frozen.uhc.Controllers;

import me.frozen.uhc.Models.Stage;
import me.frozen.uhc.Models.GameTeam;
import me.frozen.uhc.Tasks.DisplayTimer;
import me.frozen.uhc.UHC;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    UHC plugin;
    public enum Status {INACTIVE, ACTIVE, FINISH}
    private Status status;
    private int stageIndex;
    private BorderController borderController;

    private ArrayList<Stage> stages;
    private boolean teamsEnabled = false;
    /*
    STAGE LIST
    0 = 10 minutes 1000 blocks
    1 = 30 minutes shrink to 200 blocks
    2 = 10 minutes floor rise to y=0, roof lower to y=150, shrink to 100 blocks
    3 = 8 minutes floor rise to y=base, roof lower to y=base+10, shrink to 10 blocks
    4 = 2 minutes roof lower to y=base, shrink to -1 blocks
     */
    private ArrayList<Stage> loadStages(int base) {
        stages = new ArrayList<>();

        stages.add(new Stage(minutesToSeconds(10), plugin.INITIAL_BORDER_SIZE, -64, 320, false, ChatColor.GREEN + "" + ChatColor.BOLD + "Let the games begin")); // 0
        stages.add(new Stage(minutesToSeconds(30), 200, -64, 320, true, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "The walls are starting to shrink!")); // 1
        stages.add(new Stage(minutesToSeconds(10), 100, 0, 150, true, ChatColor.YELLOW + "" + ChatColor.BOLD + "The floor is rising. Stay above y=0!")); // 2
        stages.add(new Stage(minutesToSeconds(8), 10, base, base+10, true, ChatColor.RED + "" + ChatColor.BOLD + "Get above ground! The walls are shrinking!")); // 3
        stages.add(new Stage(minutesToSeconds(2), -1, base, base, true, ChatColor.DARK_RED + "" + ChatColor.BOLD + "SUDDEN DEATH")); // 4

//        stages.add(new Stage(minutesToSeconds(20), plugin.INITIAL_BORDER_SIZE, -64, 320, false)); // 0
//        stages.add(new Stage(10, 200, -64, 320, true)); // 1
//        stages.add(new Stage(10, 100, 0, 150, true)); // 2
//        stages.add(new Stage(8, 10, base, base+10, true)); // 3
//        stages.add(new Stage(20, 10, base, base+10, false));
//        stages.add(new Stage(minutesToSeconds(1), -1, base, base, true)); // 4

        return stages;
    }

    public GameController(UHC plugin) {
        this.plugin = plugin;
        status = Status.INACTIVE;
        stageIndex = 0;
        borderController = new BorderController(plugin);
    }

    public void startGame(World world) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            p.closeInventory();
            if (teamsEnabled) p.getInventory().remove(plugin.getTeamsGUI().getOpenTeamMenuItem());
        }

        world.setTime(0);

//        borderController.createBorder(world, plugin.INITIAL_BORDER_SIZE, findGoodCenter(world, new Location(world, 0, 0, 0)));
//        System.out.println(getBorderController().getGameBorder());
        borderController.createBorder(world, plugin.INITIAL_BORDER_SIZE);

        stages = loadStages(world.getHighestBlockAt(getBorderController().getGameBorder().getCenter()).getY());
        setStatus(Status.ACTIVE);
        setStageIndex(0);
        borderController.loadStage(world, stages.get(stageIndex));
        DisplayTimer.loadTimer(plugin, stages.get(stageIndex));

        if (isTeamsEnabled()) teleportTeams(world);
        else teleportPlayersInCircle(world, (plugin.INITIAL_BORDER_SIZE / 2) - (plugin.INITIAL_BORDER_SIZE / 20), (List<Player>) Bukkit.getOnlinePlayers());

        if (isTeamsEnabled())
            for (GameTeam team : GameTeam.getActiveTeams())
                for (Player player : team.getPlayersOnTeam())
                    for (Player playerOut : team.getPlayersOnTeam())
                        if (player != playerOut)
                            GlowAPI.setGlowing(playerOut, team.getGlowAPIColor(), player);
    }

    public void teleportTeams(World world) {
        Random rand = new Random();
        ArrayList<Player> soloPlayers = new ArrayList<>();
        ArrayList<GameTeam> activeTeams = GameTeam.getActiveTeams();
        for (Player player : Bukkit.getOnlinePlayers())
            if (!GameTeam.getPlayerTeams().containsKey(player.getUniqueId())) soloPlayers.add(player);
        int radius = (plugin.INITIAL_BORDER_SIZE / 2) - (plugin.INITIAL_BORDER_SIZE / 20);
        double arcDistanceDegrees = (360.0 / activeTeams.size()) + soloPlayers.size();
        double curDistanceAroundDegrees = 0;
        int nSoloPlayers = soloPlayers.size(), nActiveTeams = activeTeams.size();
        for (int i = 0; i < soloPlayers.size() + activeTeams.size(); i++) {
            double toX = Math.cos(curDistanceAroundDegrees)*radius;
            double toZ = Math.sin(curDistanceAroundDegrees)*radius;
            ArrayList<Player> players;
            if (nSoloPlayers != 0 && nActiveTeams != 0)
                if (rand.nextBoolean()) {
                    players = new ArrayList<>();
                    players.add(soloPlayers.get(nSoloPlayers-1));
                    nSoloPlayers--;
                } else {
                    players = activeTeams.get(nActiveTeams - 1).getPlayersOnTeam();
                    nActiveTeams--;
                }
            else if (nSoloPlayers == 0 && nActiveTeams != 0) {
                players = activeTeams.get(nActiveTeams - 1).getPlayersOnTeam();
                nActiveTeams--;
            } else {
                players = new ArrayList<>();
                players.add(soloPlayers.get(nSoloPlayers-1));
                nSoloPlayers--;
            }
            if (players.size() == 1)
                players.get(0).teleport(new Location(world, toX, world.getHighestBlockYAt((int) toX, (int) toZ) + 1, toZ));
            else {
                teleportPlayersInCircle(world, 2, players);
            }
            curDistanceAroundDegrees+=arcDistanceDegrees;
        }
    }

    public void teleportPlayersInCircle(World world, int radius, List<Player> players) {
        double arcDistanceDegrees = 360.0 / players.size();
        double curDistanceAroundDegrees = 0;
        for (Player player : players) {
            double toX = Math.cos(curDistanceAroundDegrees)*radius;
            double toZ = Math.sin(curDistanceAroundDegrees)*radius;
            player.teleport(new Location(world, toX, world.getHighestBlockYAt((int) toX, (int) toZ) + 1, toZ));
            curDistanceAroundDegrees+=arcDistanceDegrees;
        }
    }

    public Location findGoodCenter(World world, Location beg) {
        Location loc = beg.clone();
        for (int n = 1;;n++) {
            System.out.println("Search #" + n);
            int blocksOfOcean = countBlocksOfOcean(world, loc);
            if (blocksOfOcean <= 2) {
                return loc;
            }
            System.out.println("Big ocean found - blocksOfOcean = " + blocksOfOcean);
            Random rand = new Random();
            loc = (rand.nextInt(2) == 0) ? loc.add(-1000, 0, 0) : loc.add(0, 0, -1000);
        }
    }

    public int countBlocksOfOcean(World world, Location center) {
        int size = plugin.INITIAL_BORDER_SIZE / 2;
        Location loc = center.clone();
        int blocksOfOcean = 0;
        for (int i = -size + loc.getBlockX(); i <= size + loc.getBlockX(); i += 100) {
            for (int j = -size + loc.getBlockZ(); j <= size + loc.getBlockZ(); j += 100) {
                Biome cur = world.getHighestBlockAt(i, j).getBiome();
                for (Biome biome : plugin.BIOMES_DONT_WANT) {
                    if (biome == cur) {
                        blocksOfOcean++;
                    }
                }
            }
        }
        return blocksOfOcean;
    }

    public void nextStage(World world) {
        setStageIndex(getStageIndex() + 1);
        if (getStageIndex() >= stages.size()) return;
        borderController.loadStage(world, getCurrentStage());
        DisplayTimer.loadTimer(plugin, stages.get(stageIndex));
    }

    public void toggleTeams() {
        setTeamsEnabled(!isTeamsEnabled());
    }

    public boolean isTeamsEnabled() {
        return teamsEnabled;
    }

    public void setTeamsEnabled(boolean teamsEnabled) {
        this.teamsEnabled = teamsEnabled;
    }

    public Stage getCurrentStage() {
        return stages.get(stageIndex);
    }
    public BorderController getBorderController() {
        return borderController;
    }

    public void setBorderController(BorderController borderController) {
        this.borderController = borderController;
    }

    public int getStageIndex() {
        return stageIndex;
    }

    public void setStageIndex(int stageIndex) {
        this.stageIndex = stageIndex;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }

    private int minutesToSeconds(int min) {
        return min * 60;
    }

}
