package me.frozen.uhc.GUIs;

import me.frozen.uhc.Models.GameTeam;
import me.frozen.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TeamsGUI implements Listener {
    private final UHC plugin;
    private final Inventory inv;
    private ItemStack openTeamMenuItem;

    public TeamsGUI(UHC plugin) {
        this.plugin = plugin;
        inv = Bukkit.createInventory(null, 27, "Select a team");
        initializeTeamMenuItem();
    }

    public void initializeTeamMenuItem() {
        openTeamMenuItem = new ItemStack(Material.SHULKER_SHELL);
        ItemMeta meta = openTeamMenuItem.getItemMeta();
        meta.setDisplayName("Team Menu");
        openTeamMenuItem.setItemMeta(meta);
    }

    public void initializeItems() {
        for (GameTeam team : GameTeam.getAllTeams()) {
            int i = 0;
            Material glass = null;
            Material wool = null;
            switch (team.getTeamName()) {
                case "Red" -> {
                    i = 2;
                    glass = Material.RED_STAINED_GLASS;
                    wool = Material.RED_WOOL;
                }
                case "Orange" -> {
                    i = 3;
                    glass = Material.ORANGE_STAINED_GLASS;
                    wool = Material.ORANGE_WOOL;
                }
                case "Yellow" -> {
                    i = 4;
                    glass = Material.YELLOW_STAINED_GLASS;
                    wool = Material.YELLOW_WOOL;
                }
                case "Lime" -> {
                    i = 5;
                    glass = Material.LIME_STAINED_GLASS;
                    wool = Material.LIME_WOOL;
                }
                case "Green" -> {
                    i = 6;
                    glass = Material.GREEN_STAINED_GLASS;
                    wool = Material.GREEN_WOOL;
                }
                case "Cyan" -> {
                    i = 11;
                    glass = Material.CYAN_STAINED_GLASS;
                    wool = Material.CYAN_WOOL;
                }
                case "Aqua" -> {
                    i = 12;
                    glass = Material.LIGHT_BLUE_STAINED_GLASS;
                    wool = Material.LIGHT_BLUE_WOOL;
                }
                case "Blue" -> {
                    i = 13;
                    glass = Material.BLUE_STAINED_GLASS;
                    wool = Material.BLUE_WOOL;
                }
                case "Purple" -> {
                    i = 14;
                    glass = Material.PURPLE_STAINED_GLASS;
                    wool = Material.PURPLE_WOOL;
                }
                case "Pink" -> {
                    i = 15;
                    glass = Material.PINK_STAINED_GLASS;
                    wool = Material.PINK_WOOL;
                }
            }
            ArrayList<Player> playersOnTeam = GameTeam.getPlayersOnTeam(team);
            int numberOfPlayersOnTeam = playersOnTeam.size();
            boolean teamEmpty = numberOfPlayersOnTeam == 0;
            ArrayList<String> playersOnTeamNames = new ArrayList<>();
            for (Player player : playersOnTeam) playersOnTeamNames.add(player.getName());
            inv.setItem(i, createGuiItem(teamEmpty ? glass : wool, teamEmpty ? 1 : numberOfPlayersOnTeam, "Click to join", playersOnTeamNames));

            inv.setItem(21, createGuiItem(Material.GLASS, 1, "Leave team", null));
            inv.setItem(23, createGuiItem(Material.BARRIER, 1, "Exit", null));
        }
    }

    protected ItemStack createGuiItem(final Material material, int amount, final String name, final ArrayList<String> lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent) {
        initializeItems();
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player player = (Player)e.getWhoClicked();
        String teamName = null;

        switch (e.getRawSlot()) {
            case 2 -> teamName = "Red";
            case 3 -> teamName = "Orange";
            case 4 -> teamName = "Yellow";
            case 5 -> teamName = "Lime";
            case 6 -> teamName = "Green";
            case 11 -> teamName = "Cyan";
            case 12 -> teamName = "Aqua";
            case 13 -> teamName = "Blue";
            case 14 -> teamName = "Purple";
            case 15 -> teamName = "Pink";
            case 21 -> {
                if (GameTeam.hasTeam(player)) GameTeam.getTeam(player).remove(player);
                reloadInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                System.out.println(player.getName() + " has left their team");
                return;
            }
            case 23 -> {
                player.closeInventory();
                return;
            }
        }

        GameTeam team = GameTeam.getTeam(teamName);
        assert team != null;
        team.joinTeam(player);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
        System.out.println(player.getName() + " joined team " + team.getTeamName());
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                reloadInventory();
            }
        }, 10);
    }

    protected void reloadInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().equals(inv))
                openInventory(player);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if (e.getAction().isRightClick() && e.getItem() != null && e.getItem().equals(plugin.getTeamsGUI().getOpenTeamMenuItem()))
            plugin.getTeamsGUI().openInventory(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().equals(plugin.getTeamsGUI().getOpenTeamMenuItem()))
            e.setCancelled(true);
    }

    public ItemStack getOpenTeamMenuItem() {
        return openTeamMenuItem;
    }

    public void setOpenTeamMenuItem(ItemStack openTeamMenuItem) {
        this.openTeamMenuItem = openTeamMenuItem;
    }
}
