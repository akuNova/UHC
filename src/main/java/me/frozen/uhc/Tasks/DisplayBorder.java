package me.frozen.uhc.Tasks;

import me.frozen.uhc.Models.GameBorder;
import me.frozen.uhc.UHC;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import static me.frozen.uhc.Models.GameBorder.status.STATIONARY;

public class DisplayBorder {

    private static final int OFFSET = 4; // how many blocks between the particles
    private static final long TIMER = 2; // how long it takes to move up RISE
    private static final double RISE = 0.2; // how many blocks to rise each time

    public static void startBorderCheck(UHC plugin, GameBorder gameBorder) {
        final double[] o = {0};
        new BukkitRunnable() {
            @Override
            public void run() {

                double minX = gameBorder.getCenter().getBlockX() - (gameBorder.getBorderSize() / 2);
                double minZ = gameBorder.getCenter().getBlockZ() - (gameBorder.getBorderSize() / 2);
                double maxX = minX + gameBorder.getBorderSize();
                double maxZ = minZ + gameBorder.getBorderSize();
                double yFloor = gameBorder.getBorderFloor();
                double yRoof = gameBorder.getBorderRoof();
                double yLimit = gameBorder.getBorderSize() / 2;

                // Display particles along the border
                for (double x = minX; x <= maxX; x++) {
                    spawnParticle(gameBorder, gameBorder.getWorld(), x, minZ, o[0], yFloor, yRoof);
                    spawnParticle(gameBorder, gameBorder.getWorld(), x, maxZ, o[0], yFloor, yRoof);
                }

                for (double z = minZ; z <= maxZ; z++) {
                    spawnParticle(gameBorder, gameBorder.getWorld(), minX, z, o[0], yFloor, yRoof);
                    spawnParticle(gameBorder, gameBorder.getWorld(), maxX, z, o[0], yFloor, yRoof);
                }

                for (double size = minX; size <= maxX; size++) {
                    spawnParticleFloor(gameBorder, gameBorder.getWorld(), yFloor, yLimit, o[0], size, minX, minZ);
                    spawnParticleFloor(gameBorder, gameBorder.getWorld(), yRoof, yLimit, o[0], size, minX, minZ);
                }

                o[0] = (o[0] >= OFFSET) ? 0 : o[0] + RISE;
            }
        }.runTaskTimer(plugin, 0L, TIMER);
    }

    private static void spawnParticle(GameBorder gameBorder, World world, double x, double z, double yOffset, double yFloor, double yRoof) {
        for (double i = yFloor+yOffset; i < yRoof; i+=OFFSET) {
            Location particleLocation = new Location(world, x + 0.5, i, z + 0.5);
            world.spawnParticle(Particle.REDSTONE, particleLocation, 1, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0F));
        }
    }

    private static void spawnParticleFloor(GameBorder gameBorder, World world, double y, double size, double sizeOffset, double horizontalOffset, double minX, double minZ) {
        for (double i = -size+sizeOffset+0.5; i < size; i+=OFFSET) {
            if (i+horizontalOffset > size || i-horizontalOffset > size) break;
            if (i+horizontalOffset > -size+0.5 && i-horizontalOffset > -size+0.5) {
                Location particleLocation = new Location(world, i+horizontalOffset, y, i-horizontalOffset);
                world.spawnParticle(Particle.REDSTONE, particleLocation, 1, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0F)); //color if moving
            }
        }
    }
}
