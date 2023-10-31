package me.frozen.uhc.Tasks;

import me.frozen.uhc.Models.GameBorder;
import me.frozen.uhc.UHC;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MoveBorder {

    private static BukkitTask task = null;
    public static void moveBorder(UHC plugin, double size, double time, double centerX, double centerZ, double floor, double roof) {
        GameBorder border = plugin.getGameBorder();
        border.setStatus(GameBorder.status.MOVING);

        double timeInSeconds = time*20;

        double sizeBlocksPerSecond = (border.getBorderSize() - size) / timeInSeconds;
        double centerXBlocksPerSecond = (border.getCenter().getX() - centerX) / timeInSeconds;
        double centerZBlocksPerSecond = (border.getCenter().getZ() - centerZ) / timeInSeconds;
        double floorBlocksPerSecond = (border.getBorderFloor() - floor) / timeInSeconds;
        double roofBlocksPerSecond = (border.getBorderRoof() - roof) / timeInSeconds;

        final int[] i = {0};
        if (task != null) {
            task.cancel();
            task = null;
        }
        task = new BukkitRunnable() {
            @Override
            public void run() {
                border.setBorderSize(border.getBorderSize() - sizeBlocksPerSecond);
                border.setBorderFloor(border.getBorderFloor() - floorBlocksPerSecond);
                border.setBorderRoof(border.getBorderRoof() - roofBlocksPerSecond);
                border.setCenter(border.getCenter().subtract(centerXBlocksPerSecond, 0, centerZBlocksPerSecond));
                i[0]++;
                if (i[0] >= time*20) {
                    border.setStatus(GameBorder.status.STATIONARY);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
