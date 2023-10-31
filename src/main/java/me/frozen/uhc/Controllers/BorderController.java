package me.frozen.uhc.Controllers;

import me.frozen.uhc.Models.GameBorder;
import me.frozen.uhc.Models.Stage;
import me.frozen.uhc.Tasks.DisplayBorder;
import me.frozen.uhc.Tasks.MoveBorder;
import me.frozen.uhc.Tasks.OutsideBorder;
import me.frozen.uhc.UHC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BorderController {
    private final UHC plugin;
    private GameBorder gameBorder;
    private BukkitTask loadStageTask = null;

    public BorderController(UHC plugin) {
        this.plugin = plugin;
        gameBorder = null;
    }

    public BorderController(UHC plugin, GameBorder gameBorder) {
        this.plugin = plugin;
        this.gameBorder = gameBorder;
    }

    public void loadStage(World world, Stage stage) {
        if (loadStageTask != null) {
            loadStageTask.cancel();
            loadStageTask = null;
        }
        Component mainTitle = Component.text("");
        Component subtitle = Component.text(stage.getMessage());
        Title title = Title.title(mainTitle, subtitle);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showTitle(title);
            p.playSound(p, Sound.ITEM_TRIDENT_THUNDER, 1, 2);
        }
        if (gameBorder == null) createBorder(world, stage.getSize(), new Location(world, 0, 0, 0));
        MoveBorder.moveBorder(plugin, stage.getSize(), stage.isBorderMove() ? (double) stage.getTime() : 0.05, plugin.getGameBorder().getCenter().getX(), plugin.getGameBorder().getCenter().getZ(), stage.getyFloor(), stage.getyRoof());
        loadStageTask = new BukkitRunnable() {
            @Override
            public void run() {
                plugin.gameController.nextStage(world);
            }
        }.runTaskLater(plugin, stage.getTime() * 20L);
    }



    public void createBorder(World world, double borderSize) {
        gameBorder = new GameBorder(world, borderSize);
        DisplayBorder.startBorderCheck(plugin, gameBorder);
        OutsideBorder.startOutsideBorderCheck(plugin, gameBorder);
    }
    public void createBorder(World world, double borderSize, Location center) {
        gameBorder = new GameBorder(world, borderSize, center);
        DisplayBorder.startBorderCheck(plugin, gameBorder);
        OutsideBorder.startOutsideBorderCheck(plugin, gameBorder);
    }

    public GameBorder getGameBorder() {
        return gameBorder;
    }

    public void setGameBorder(GameBorder gameBorder) {
        this.gameBorder = gameBorder;
    }
}
