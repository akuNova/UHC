package me.frozen.uhc.Events;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.frozen.uhc.UHC;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String newName = UHC.getSpecialPlayer(player);
        String fullName = /*UHC.getSpecialPlayerFull(player);*/ newName;

        PlayerProfile profile = player.getPlayerProfile();
        profile.setName(newName);
        player.setPlayerProfile(profile);
        player.setDisplayName(fullName);
        player.setPlayerListName(fullName);

        player.setScoreboard(UHC.getScoreboard());
    }
}
