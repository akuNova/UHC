package me.frozen.uhc.Events;

import me.frozen.uhc.UHC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Random;

public class OnPlayerDeath implements Listener {
    private final UHC plugin;
    ItemStack blood = new ItemStack(Material.RED_DYE);
    ItemStack bones = new ItemStack(Material.BONE);

    public OnPlayerDeath(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta)head.getItemMeta();
        skullMeta.setOwningPlayer(player);
        head.setItemMeta(skullMeta);

//        ItemMeta itemMeta = head.getItemMeta();
//        itemMeta.setDisplayName(e.getEntity() + "'s head");
//        head.setItemMeta(itemMeta);

        World world = player.getWorld();
        Location loc = player.getLocation();

        player.setGameMode(GameMode.SPECTATOR);

        world.dropItemNaturally(loc, head);
        Random rand = new Random();
        for (int i = rand.nextInt(10) + 10; i > 0; i--) {
            ItemMeta meta = blood.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Integer.toString(i));
            meta.setLore(lore);
            meta.setDisplayName("PlayerBlood");
            blood.setItemMeta(meta);
            world.dropItemNaturally(loc, blood);
        }
        for (int i = rand.nextInt(10) + 10; i > 0; i--) {
            ItemMeta meta = bones.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Integer.toString(i));
            meta.setLore(lore);
            meta.setDisplayName("PlayerBones");
            bones.setItemMeta(meta);
            world.dropItemNaturally(loc, bones);
        }
        for (ItemStack item : e.getDrops()) {
            world.dropItemNaturally(loc, item);
        }
        e.setDeathMessage("");
        Component mainTitle = Component.text("");
        Component subtitle = Component.text(ChatColor.GOLD + "" + ChatColor.ITALIC + player.getDisplayName() + ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + " has been eliminated");
        Title title = Title.title(mainTitle, subtitle);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showTitle(title);
            p.playSound(p, Sound.BLOCK_BELL_USE, 1, 0.5F);
            p.playSound(p, Sound.BLOCK_BELL_RESONATE, 1, 1);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        if (e.getItem().getItemStack().lore() != null || e.getItem().getItemStack().lore() != null) e.setCancelled(true);
    }
}
