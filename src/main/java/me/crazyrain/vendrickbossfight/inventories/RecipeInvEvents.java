package me.crazyrain.vendrickbossfight.inventories;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.PlayerInventory;

public class RecipeInvEvents implements Listener {
    VendrickBossFight plugin;

    public RecipeInvEvents (VendrickBossFight plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void stopClicks(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !(e.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof RecipeInventory)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onNextPageClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null ||!(e.getClickedInventory().getHolder() instanceof RecipeInventory)){
            return;
        }

        if (e.getSlot() == 44 && e.getCurrentItem().getType().equals(Material.ARROW)) {
            plugin.getCraftManager().getInventoryInstance(e.getWhoClicked().getUniqueId()).newPage(true);
        }

        if (e.getSlot() == 36  && e.getCurrentItem().getType().equals(Material.ARROW)) {
            plugin.getCraftManager().getInventoryInstance(e.getWhoClicked().getUniqueId()).newPage(false);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!((e.getInventory().getHolder()) instanceof RecipeInventory)) {
            return;
        }
        if (!plugin.getCraftManager().getOpenInventories().containsKey(e.getPlayer().getUniqueId())) {
            return;
        }
        plugin.getCraftManager().removeInventoryInstance(e.getPlayer().getUniqueId());
    }

}
