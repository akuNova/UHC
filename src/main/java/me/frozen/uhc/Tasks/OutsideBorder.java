package me.frozen.uhc.Tasks;

import me.frozen.uhc.Models.GameBorder;
import me.frozen.uhc.UHC;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OutsideBorder {

    private static final long TIMER = 5;

    public static void startOutsideBorderCheck(UHC plugin, GameBorder gameBorder) {
        new BukkitRunnable() {
            @Override
            public void run() {
                double minX = gameBorder.getCenter().getBlockX() - (gameBorder.getBorderSize() / 2);
                double minZ = gameBorder.getCenter().getBlockZ() - (gameBorder.getBorderSize() / 2);
                double maxX = minX + gameBorder.getBorderSize();
                double maxZ = minZ + gameBorder.getBorderSize();
                double minY = gameBorder.getBorderFloor();
                double maxY = gameBorder.getBorderRoof();

                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SPECTATOR) break;
                    double playerX = player.getLocation().getX();
                    double playerZ = player.getLocation().getZ();
                    double playerY = player.getLocation().getY();
                    // if player is outside border
                    if (playerX < minX || playerX > maxX) {
                        playerOutsideBorder(player);
                    }
                    if (playerY < minY || playerY > maxY) {
                        playerOutsideBorder(player);
                    }
                    if (playerZ < minZ || playerZ > maxZ) {
                        playerOutsideBorder(player);
                    }

                    // if player is close to border
                    if (playerX < minX + 20 || playerX > maxX - 20) {
                        playerCloseToBorder(player);
                    }
                    if (playerY < minY + 10 || playerY > maxY - 10) {
                        playerCloseToBorder(player);
                    }
                    if (playerZ < minZ + 20 || playerZ > maxZ - 20) {
                        playerCloseToBorder(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, TIMER);
    }

    private static void playerOutsideBorder(Player player) {
        player.damage(1);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().clone().add(0, 1, 0), 5, 0, 0, 0, 0.1);
        player.playSound(player, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
    }

    private static void playerCloseToBorder(Player player) {
        player.getWorld().spawnParticle(Particle.WATER_SPLASH, player.getLocation().clone().add(0, 2, 0), 30, 0.2, 0, 0.2);
    }
}
