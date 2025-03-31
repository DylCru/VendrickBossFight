package me.crazyrain.vendrickbossfight.inventories;

import me.crazyrain.vendrickbossfight.CustomEvents.RecipeInventoryNewPageEvent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class RecipeInventory implements InventoryHolder, Listener {

    Inventory inv;
    HashMap<ItemStack, HashMap<Integer, ItemStack>> materialRecipes;
    int pageNum = 1;
    boolean lastPage = false;
    int[] ingSlots = {11,12,13,20,21,22,29,30,31};
    ItemStack[] results;
    ItemStack result;
    int resultSlot = 24;

    public RecipeInventory(String title, HashMap<ItemStack, HashMap<Integer, ItemStack>> materialRecipes) {
        inv = Bukkit.createInventory(this, 45, title);
        this.materialRecipes = materialRecipes;
        this.results = materialRecipes.keySet().toArray(new ItemStack[0]);
        result = this.results[0];
        init();
    }

    private void init() {
        inv.clear();
        for (int i : materialRecipes.get(result).keySet()) {
            inv.setItem(ingSlots[i], materialRecipes.get(result).get(i));
        }
        for (int i = 0; i < inv.getSize(); i++){
            if (!ArrayUtils.contains(ingSlots, i)){
                inv.setItem(i, createItem( " ", Material.BLACK_STAINED_GLASS_PANE, null));
            }
        }
        if (!lastPage && results.length > 1) {
            inv.setItem(44, createItem(ChatColor.GREEN + "Next Page", Material.ARROW, null));
        }
        if (pageNum > 1) {
            inv.setItem(36, createItem(ChatColor.GREEN + "Previous Page", Material.ARROW, null));
        }
        inv.setItem(resultSlot, result);
    }

    private ItemStack createItem(String name, Material mat, List<String> lore){
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    @EventHandler
    public void onNewPage(RecipeInventoryNewPageEvent e) {
        if (e.isForward()) {
            this.pageNum++;
        } else {
            this.pageNum--;
        }

        this.result = this.results[pageNum - 1];
        lastPage = this.pageNum == this.results.length;
        init();
    }

}
