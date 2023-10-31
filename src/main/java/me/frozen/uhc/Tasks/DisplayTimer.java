package me.frozen.uhc.Tasks;

import me.frozen.uhc.Models.Stage;
import me.frozen.uhc.UHC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DisplayTimer {

    private static BukkitTask task = null;
    private static int minutes;
    private static int seconds;

    public static void loadTimer(UHC plugin, Stage stage) {
//        if (task != null) {
//            task.cancel();
//            task = null;
//        }
//
//        minutes = stage.getTime() / 60;
//        seconds = stage.getTime() % 60;
//
//        task = new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (Player player : plugin.getServer().getOnlinePlayers()) {
//                    player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(minutes + ":" + (seconds >= 10 ? "" : "0" ) + seconds));
//                }
//                if (seconds == 0) {
//                    minutes--;
//                    seconds = 59;
//                } else {
//                    seconds--;
//                }
//                if (minutes <= 0 && seconds <= 0) this.cancel();
//            }
//        }.runTaskTimer(plugin, 0L, 20L);
    }
}
