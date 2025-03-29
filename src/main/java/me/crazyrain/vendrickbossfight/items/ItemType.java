package me.crazyrain.vendrickbossfight.items;

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
}
