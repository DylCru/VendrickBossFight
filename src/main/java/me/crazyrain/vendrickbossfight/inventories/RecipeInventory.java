package me.crazyrain.vendrickbossfight.inventories;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class RecipeInventory implements InventoryHolder {

    Inventory inv;
    HashMap<ItemStack, HashMap<Integer, ItemStack>> materialRecipes;
    int pageNum;
    boolean lastPage;
    int[] ingSlots = {11,12,13,20,21,22,29,30,31};
    int resultSlot = 24;

    public RecipeInventory(String title, int pageNum, boolean lastPage, HashMap<ItemStack, HashMap<Integer, ItemStack>> materialRecipes) {
        inv = Bukkit.createInventory(this, 54, title);
        this.pageNum = pageNum;
        this.lastPage = lastPage;
        this.materialRecipes = materialRecipes;
    }

    private void init() {
        for (int i = 0; i < inv.getSize(); i++){
            if (ArrayUtils.contains(ingSlots, i)){
                inv.setItem(i, createItem( " ", Material.BLACK_STAINED_GLASS_PANE, null));
            }
        }

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
}
