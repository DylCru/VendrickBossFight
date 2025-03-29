package me.crazyrain.vendrickbossfight.items;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.inventory.ItemStack;

public enum ItemType {
    ITEM("VEN_ITEM"),
    MATERIAL("VEN_MATERIAL"),
    BLOCK("VEN_BLOCK");

    private final String itemType;

    ItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    public static String getItemTypeFromItem(ItemStack item) {
        return NBTEditor.getString(item, NBTEditor.CUSTOM_DATA, "VEN_ITEM_TYPE");
    }
}
