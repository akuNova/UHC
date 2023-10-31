package me.frozen.uhc.Events;

import me.frozen.uhc.Models.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Teams implements Listener {
    @EventHandler
    public void onTeamAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))return;
        if (!(e.getEntity() instanceof Player)) return;
        GameTeam team = GameTeam.getTeam((Player) e.getEntity());
        if (team == null) return;
        ArrayList<Player> players = team.getPlayersOnTeam();
        if (players.contains((Player) e.getDamager())) e.setCancelled(true);
    }
}
