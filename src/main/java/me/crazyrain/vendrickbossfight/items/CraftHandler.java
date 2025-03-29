package me.crazyrain.vendrickbossfight.items;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CraftHandler implements Listener {

    VendrickBossFight plugin;
    HashMap<ItemStack, HashMap<Integer, ItemStack>> recipes;

    public CraftHandler(VendrickBossFight plugin){
        this.plugin = plugin;
        this.recipes = plugin.getCraftManager().getRecipes();
    }

    //used for custom crafts
    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e){
        if (!plugin.getConfig().getBoolean("can-craft")) {
            return;
        }
        if(e.getInventory().getMatrix().length > 9){
            return;
        }
        boolean completeRecipe = false;

        for (ItemStack result : recipes.keySet()) {
            if (completeRecipe) {
                break;
            }
            completeRecipe = checkCraft(result, e.getInventory(), recipes.get(result));
        }

        if (!completeRecipe) {
            //Need to still manually prevent being able to craft iron blocks
            checkCraft(null, e.getInventory(), new HashMap<>() {{
                for (int i = 0; i < 9; i++) {
                    put(i, ItemManager.infinium);
                }
            }});
            checkCraft(null, e.getInventory(), new HashMap<>() {{
                for (int i = 0; i < 9; i++) {
                    put(i, ItemManager.enchantedInfinium);
                }
            }});

            preventVanillaCrafting(e.getInventory());
        }
    }

    public boolean checkCraft(ItemStack result, CraftingInventory inv, HashMap<Integer, ItemStack> ingredients){
        ItemStack[] matrix = inv.getMatrix();
        for(int i = 0; i < 9; i++){
            if(ingredients.containsKey(i)){
                if(matrix[i] == null || !matrix[i].equals(ingredients.get(i))){
                    return false;
                }
            } else {
                if(matrix[i] != null){
                    return false;
                }
            }
        }
        Bukkit.broadcastMessage("Set result to item");
        inv.setResult(result);
        return true;
    }

    public void preventVanillaCrafting(CraftingInventory inv) {
        int customItemsInCraft = 0;
        for (ItemStack item : inv.getMatrix()) {
            if (item != null) {
                ItemStack single = item.clone();
                single.setAmount(1);
                if (ArrayUtils.contains(ItemManager.allItems, single)) {
                    customItemsInCraft++;
                }
            }
        }
        if (customItemsInCraft >= 1) {
            inv.setResult(null);
        }
    }

    @EventHandler
    public void showRecipesForMaterial(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        if (e.getItem() == null) {
            return;
        }
        if (!NBTEditor.contains(e.getItem(), NBTEditor.CUSTOM_DATA, "VEN_ITEM_TYPE")) {
            return;
        }
        if (!NBTEditor.getString(e.getItem(), NBTEditor.CUSTOM_DATA, "VEN_ITEM_TYPE").equalsIgnoreCase(ItemType.MATERIAL.getItemType())) {
            return;
        }
        ItemStack item = e.getItem().clone();
        item.setAmount(1);
        HashMap<ItemStack, HashMap<Integer, ItemStack>> materialRecipes = plugin.getCraftManager().getAllRecipesForMaterial(item);

        e.getPlayer().sendMessage(materialRecipes.toString() + "recipes");
    }
}
