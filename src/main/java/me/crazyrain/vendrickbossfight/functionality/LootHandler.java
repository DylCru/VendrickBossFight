package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LootHandler {

    ItemStack emDrop = new ItemStack(Material.EMERALD_BLOCK, 2);
    ItemStack toDrop = new ItemStack(Material.TOTEM_OF_UNDYING, 2);

    static Double[] chances = {0.15, 0.11, 0.11, 0.10, 0.08, 0.05, 0.03, 0.02, 0.02, 0.01, 0.007, 0.007, 0.007};
    private static final HashMap<ItemStack, Double> itemChances = new HashMap<>();
    private static final HashMap<Material, String> itemRarity = new HashMap<>();
    private static final HashMap<ItemStack, Integer> itemReq = new HashMap<>();

    public LootHandler(){
        initMaps();
        refreshChances();
    }

    public void refreshChances(){
        List<Double> newChances = new ArrayList<>(VendrickBossFight.plugin.getConfig().getDoubleList("item-chances"));
        chances = newChances.toArray(new Double[0]);
        initMaps();
    }

    private void initMaps(){
        itemReq.clear();
        itemRarity.clear();
        itemChances.clear();

        itemReq.put(emDrop, 0);
        itemReq.put(ItemManager.eternalFragment,0);
        itemReq.put(ItemManager.essenceOfEternity, 0);
        itemReq.put(ItemManager.voidCore, 2);
        itemReq.put(ItemManager.infinium, 2);
        itemReq.put(toDrop, 0);
        itemReq.put(ItemManager.plasmaTorch, 5);
        itemReq.put(ItemManager.vendrickHatchet, 0);
        itemReq.put(ItemManager.shatterSpine, 0);
        itemReq.put(ItemManager.theCatalyst, 0);
        itemReq.put(ItemManager.lusciousApple, 2);
        itemReq.put(ItemManager.fusionChamber, 4);
        itemReq.put(ItemManager.vendrickTalisman, 5);

        itemRarity.put(emDrop.getType(), Rarity.RARE.toString());
        itemRarity.put(ItemManager.eternalFragment.getType(),Rarity.RARE.toString());
        itemRarity.put(ItemManager.essenceOfEternity.getType(), Rarity.RARE.toString());
        itemRarity.put(ItemManager.voidCore.getType(), Rarity.RARE.toString());
        itemRarity.put(ItemManager.infinium.getType(), Rarity.RARE.toString());
        itemRarity.put(toDrop.getType(), Rarity.SPECIAL.toString());
        itemRarity.put(ItemManager.plasmaTorch.getType(), Rarity.SPECIAL.toString());
        itemRarity.put(ItemManager.vendrickHatchet.getType(), Rarity.SPECIAL.toString());
        itemRarity.put(ItemManager.shatterSpine.getType(), Rarity.SPECIAL.toString());
        itemRarity.put(ItemManager.theCatalyst.getType(), Rarity.SPECIAL.toString());
        itemRarity.put(ItemManager.lusciousApple.getType(), Rarity.INSANE.toString());
        itemRarity.put(ItemManager.fusionChamber.getType(), Rarity.INSANE.toString());
        itemRarity.put(ItemManager.vendrickTalisman.getType(), Rarity.INSANE.toString());

        itemChances.put(emDrop, chances[0]);
        itemChances.put(ItemManager.eternalFragment, chances[1]);
        itemChances.put(ItemManager.essenceOfEternity, chances[2]);
        itemChances.put(ItemManager.voidCore, chances[3]);
        itemChances.put(ItemManager.infinium, chances[4]);
        itemChances.put(toDrop, chances[5]);
        itemChances.put(ItemManager.plasmaTorch, chances[6]);
        itemChances.put(ItemManager.vendrickHatchet, chances[7]);
        itemChances.put(ItemManager.shatterSpine, chances[8]);
        itemChances.put(ItemManager.theCatalyst, chances[9]);
        itemChances.put(ItemManager.lusciousApple, chances[10]);
        itemChances.put(ItemManager.fusionChamber, chances[11]);
        itemChances.put(ItemManager.vendrickTalisman, chances[12]);
    }

    public void lootRoll(Player player, int difficulty){
        List<ItemStack> loot = new ArrayList<>();
        double roll;

        for (ItemStack item : itemChances.keySet()){
            if (VendrickBossFight.plugin.getConfig().getStringList("disabled-items").contains(item.getItemMeta().getDisplayName())){
                continue;
            }


            roll = Double.parseDouble(new BigDecimal(String.valueOf(Math.random())).setScale(4, RoundingMode.DOWN).toString());
            if (roll <= itemChances.get(item)){
                if (difficulty >= itemReq.get(item)) {
                    if (itemRarity.get(item.getType()).contains("Rare")) {
                        if (!item.getItemMeta().hasDisplayName()) {
                            player.sendMessage(Lang.RAREDROP.toString() + " " + ChatColor.DARK_GREEN + ChatColor.BOLD + Lang.EMERALDDROP);
                        } else {
                            player.sendMessage(Lang.RAREDROP.toString() + " " + item.getItemMeta().getDisplayName());
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                    }
                    if (itemRarity.get(item.getType()).contains("Special")) {
                        if (!item.getItemMeta().hasDisplayName()) {
                            player.sendMessage(Lang.EPICDROP.toString() + " " + ChatColor.GOLD + ChatColor.BOLD + Lang.TOTEMDROP);
                        } else {
                            player.sendMessage(Lang.EPICDROP.toString() + " " + item.getItemMeta().getDisplayName());
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                    }
                    if (itemRarity.get(item.getType()).contains("Insane")) {
                        player.sendMessage(Lang.SPECIALDROP.toString() + " " + item.getItemMeta().getDisplayName());
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1.0f, 0.5f);
                    }
                    loot.add(item);
                }
            }

        }
        int emAmount;
        if (difficulty >= 2){
            emAmount = (int) (Math.random() * (100 - 5) + 5);
        } else {
            emAmount = (int) (Math.random() * (32 - 5) + 5);
        }
        loot.add(new ItemStack(Material.EMERALD, emAmount));
        loot.add(new ItemStack(Material.DIAMOND, emAmount / 3));
        if (difficulty == 5){
            loot.add(ItemManager.voidCoreFragment);
            player.sendMessage(ChatColor.DARK_GRAY + "Received: Emerald x" + emAmount + " Diamond x" + emAmount / 3 + " Void Core Fragment x1" );
        } else {
            player.sendMessage(ChatColor.DARK_GRAY + "Received: Emerald x" + emAmount + " Diamond x" + emAmount / 3);
        }
        for (ItemStack item : loot){
            if (player.getInventory().firstEmpty() == -1){
                player.getWorld().dropItem(player.getLocation(), item);
                player.sendMessage(ChatColor.RED + "An item didn't fit in your inventory so it was dropped on the ground!");
            } else {
                player.getInventory().addItem(item);
            }
        }
    }

}
