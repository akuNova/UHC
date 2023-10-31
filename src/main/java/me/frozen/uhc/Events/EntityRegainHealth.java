package me.frozen.uhc.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealth implements Listener {
    @EventHandler
    public void whenEntityRegainHealth(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) e.setCancelled(true);
    }
}
