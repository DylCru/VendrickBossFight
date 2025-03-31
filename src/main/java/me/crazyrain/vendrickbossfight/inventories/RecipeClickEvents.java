package me.crazyrain.vendrickbossfight.inventories;

import me.crazyrain.vendrickbossfight.CustomEvents.RecipeInventoryNewPageEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RecipeClickEvents implements Listener {
    @EventHandler
    public void stopClicks(InventoryClickEvent e) {
        if (e.getClickedInventory() == null ||!(e.getClickedInventory().getHolder() instanceof RecipeInventory)){
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
            e.getWhoClicked().sendMessage("Clicked the forward arrow!");
            VendrickBossFight.plugin.getServer().getPluginManager().callEvent(new RecipeInventoryNewPageEvent(true));
        }
        if (e.getSlot() == 36  && e.getCurrentItem().getType().equals(Material.ARROW)) {
            e.getWhoClicked().sendMessage("Clicked the back arrow!");
            VendrickBossFight.plugin.getServer().getPluginManager().callEvent(new RecipeInventoryNewPageEvent(false));
        }
    }
}
