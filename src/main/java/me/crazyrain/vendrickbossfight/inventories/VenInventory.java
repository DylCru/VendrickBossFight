package me.crazyrain.vendrickbossfight.inventories;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VenInventory implements InventoryHolder {

    ItemStack[] contents;
    int[] border = {0,1,2,3,4,5,6,7,8,
                    45,46,47,48,49,50,51,52,53};
    Inventory inv;
    int pageNum;
    boolean lastPage;

    public VenInventory(String title, ItemStack[] contents, int pageNum, boolean lastPage){
        inv = Bukkit.createInventory(this, 43, title);
        this.contents = contents;
        this.pageNum = pageNum;
        this.lastPage = lastPage;
        init();
    }

    private void init(){
        for (int i = 0; i < inv.getSize(); i++){
            if (ArrayUtils.contains(border, i)){
                inv.setItem(i, createItem( " ", Material.BLACK_STAINED_GLASS_PANE, null));
            }
        }
        int index = 0;
        for (int i = 0; i < inv.getSize(); i++){
            if (!ArrayUtils.contains(border, i)){
                inv.setItem(i, contents[index]);
                index++;
            }
            if (index == contents.length){
                break;
            }
        }
        if (!lastPage){
            inv.setItem(53, createItem(ChatColor.GREEN + "Next Page", Material.ARROW, Collections.singletonList(ChatColor.DARK_GRAY + "Page " + (pageNum + 1))));
        }
        if (pageNum > 1){
            inv.setItem(45, createItem(ChatColor.GREEN + "Previous Page", Material.ARROW, Collections.singletonList(ChatColor.DARK_GRAY + "Page " + (pageNum - 1))));
        }
        inv.setItem(8, createItem(ChatColor.GOLD + "Search for an item", Material.OAK_SIGN, null));
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
