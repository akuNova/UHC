package me.frozen.uhc.Events;

import me.frozen.uhc.Controllers.GameController;
import me.frozen.uhc.UHC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class BeforeGameStart implements Listener {
    private final UHC plugin;

    public BeforeGameStart(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void whenPlayerDamaged(EntityDamageEvent e) {
        if (plugin.getGameController().getStatus() != GameController.Status.INACTIVE) return;
        if (!(e.getEntity() instanceof Player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void whenPlayerHunger(FoodLevelChangeEvent e) {
        if (plugin.getGameController().getStatus() != GameController.Status.INACTIVE) return;
        if (!(e.getEntity() instanceof Player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void whenPlayerBreaksBlock(BlockBreakEvent e) {
        if (plugin.getGameController().getStatus() == GameController.Status.INACTIVE) e.setCancelled(true);
    }

    @EventHandler
    public void whenPlayerPicksUpItem(EntityPickupItemEvent e) {
        if (plugin.getGameController().getStatus() != GameController.Status.INACTIVE) return;
        if (!(e.getEntity() instanceof Player)) return;
        e.setCancelled(true);
    }
}
