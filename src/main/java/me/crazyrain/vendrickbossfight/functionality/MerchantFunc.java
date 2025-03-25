package me.crazyrain.vendrickbossfight.functionality;


import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.npcs.EternalTrader;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import javax.swing.plaf.metal.MetalCheckBoxIcon;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MerchantFunc implements Listener {

    VendrickBossFight plugin;

    public MerchantFunc(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlaceRightCLick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if (!(player.getInventory().getItemInMainHand().hasItemMeta())) {
                return;
            }
            if (!(player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())) {
                return;
            }
            if (e.getClickedBlock().getType().equals(Material.AIR)) {
                return;
            }
            if (player.getInventory().getItemInMainHand().equals(ItemManager.tradeLoc)) {
                EternalTrader trader = new EternalTrader(VendrickBossFight.getPlugin(VendrickBossFight.class));
                if (!trader.spawnTrader(player.getWorld(), e.getClickedBlock().getLocation().add(0.5, 1, 0.5))){
                    e.setCancelled(true);

                    player.sendMessage(ChatColor.DARK_BLUE + "[VEN]" + ChatColor.RED  + " The merchant was unable to be placed. Please refer to the console");
                }
                player.getInventory().remove(player.getInventory().getItemInMainHand());
            }
        }
    }

    @EventHandler
    public void onMerchantRightClick(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getScoreboardTags().contains("EternalMerchant")){
            Merchant merchant = Bukkit.createMerchant("Eternal Merchant");

            ItemStack merchFrag = ItemManager.eternalFragment.clone();
            ItemStack merchEss = ItemManager.essenceOfEternity.clone();
            ItemStack merchInf = ItemManager.infinium.clone();

            List<MerchantRecipe> recipeList = new ArrayList<>();

            MerchantRecipe eternalStar = new MerchantRecipe(ItemManager.eternalStar, 10000);
            eternalStar.addIngredient(new ItemStack(Material.EMERALD, 8));
            eternalStar.addIngredient(new ItemStack(Material.DIAMOND_BLOCK));
            recipeList.add(eternalStar);

            merchFrag.setAmount(4);
            merchEss.setAmount(4);

            MerchantRecipe trueHatchet = new MerchantRecipe(ItemManager.trueEternalHatchet, 10000);
            trueHatchet.addIngredient(ItemManager.vendrickHatchet);
            trueHatchet.addIngredient(merchFrag);
            recipeList.add(trueHatchet);

            MerchantRecipe shatterStick = new MerchantRecipe(ItemManager.shatterStick, 10000);
            shatterStick.addIngredient(ItemManager.shatterSpine);
            shatterStick.addIngredient(merchEss);
            recipeList.add(shatterStick);

            merchEss.setAmount(2);
            merchFrag.setAmount(2);
            MerchantRecipe crust = new MerchantRecipe(ItemManager.pieCrust, 10000);
            crust.addIngredient(merchFrag);
            crust.addIngredient(merchEss);
            recipeList.add(crust);

            MerchantRecipe nutriment = new MerchantRecipe(ItemManager.nutrimentOfTheInfinite, 10000);
            nutriment.addIngredient(new ItemStack(Material.GOLDEN_APPLE));
            nutriment.addIngredient(ItemManager.pieCrust);
            recipeList.add(nutriment);

            merchFrag.setAmount(7);
            MerchantRecipe oven = new MerchantRecipe(ItemManager.oven, 10000);
            oven.addIngredient(merchFrag);
            oven.addIngredient(ItemManager.lusciousApple);
            recipeList.add(oven);

            MerchantRecipe nutrimentU = new MerchantRecipe(ItemManager.nutrimentU, 10000);
            nutrimentU.addIngredient(ItemManager.nutrimentOfTheInfinite);
            nutrimentU.addIngredient(ItemManager.oven);
            recipeList.add(nutrimentU);

            merchInf.setAmount(3);
            MerchantRecipe uncharged = new MerchantRecipe(ItemManager.unchargedRifle, 10000);
            uncharged.addIngredient(merchInf);
            uncharged.addIngredient(ItemManager.fusionChamber);
            recipeList.add(uncharged);

            MerchantRecipe rifle = new MerchantRecipe(ItemManager.energyRifle, 10000);
            rifle.addIngredient(ItemManager.voltaicCore);
            rifle.addIngredient(ItemManager.unchargedRifle);
            recipeList.add(rifle);

            ItemStack inf = ItemManager.infinium.clone();
            inf.setAmount(2);
            MerchantRecipe ench = new MerchantRecipe(ItemManager.enchantedInfinium, 10000);
            ench.addIngredient(inf);
            ench.addIngredient(ItemManager.plasmaTorch);
            recipeList.add(ench);

            ItemStack enchInf = ItemManager.enchantedInfinium.clone();
            enchInf.setAmount(2);

            inf.setAmount(3);
            MerchantRecipe helm = new MerchantRecipe(ItemManager.venHead, 10000);
            helm.addIngredient(enchInf);
            helm.addIngredient(inf);
            recipeList.add(helm);

            inf.setAmount(6);
            MerchantRecipe chest = new MerchantRecipe(ItemManager.venChest, 10000);
            chest.addIngredient(enchInf);
            chest.addIngredient(inf);
            recipeList.add(chest);

            inf.setAmount(5);
            MerchantRecipe legs = new MerchantRecipe(ItemManager.venLegs, 10000);
            legs.addIngredient(enchInf);
            legs.addIngredient(inf);
            recipeList.add(legs);

            inf.setAmount(2);
            MerchantRecipe boots = new MerchantRecipe(ItemManager.venBoots, 10000);
            boots.addIngredient(enchInf);
            boots.addIngredient(inf);
            recipeList.add(boots);


            merchant.setRecipes(recipeList);

            e.getPlayer().openMerchant(merchant, true);
        }
    }

    public void announce(String pName, String iName, UUID id){
        if (plugin.getConfig().getBoolean("announce-special")){
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(ChatColor.BOLD + pName + " has obtained the " + iName + "!" + ChatColor.GOLD + "" + ChatColor.BOLD + " Congratulations!");
            }
        }
        Bukkit.getPlayer(id).sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Congratulations!", ChatColor.GOLD + "You have obtained the " + iName, 10, 70, 20);
        Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.7f);

    }

    @EventHandler
    public void announceTrade(InventoryClickEvent e){
        try{
            if (!e.getCurrentItem().getItemMeta().hasLore()){
                return;
            }

            if (e.getClickedInventory().getType().equals(InventoryType.MERCHANT)){
                if (e.getRawSlot() == 2){
                    if (e.getCurrentItem().getItemMeta().getLore().contains(Rarity.SPECIAL.toString())){
                        announce(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName(), e.getWhoClicked().getUniqueId());
                    }
                    if (e.getCurrentItem().getItemMeta().getLore().contains(Rarity.INSANE.toString())){
                        announce(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName(), e.getWhoClicked().getUniqueId());
                    }
                }
            }
        }catch (NullPointerException ex){
            //internally shit and explode but don't tell anyone
        }
    }

    @EventHandler
    public void announceCraft(InventoryClickEvent e){
       try{
           if (!e.getCurrentItem().getItemMeta().hasLore()){
               return;
           }

           if (e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)){
               if (e.getRawSlot() == 0){
                   if (e.getCurrentItem().getItemMeta().getLore().contains(Rarity.SPECIAL.toString())){
                       announce(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName(), e.getWhoClicked().getUniqueId());
                   }
                   if (e.getCurrentItem().getItemMeta().getLore().contains(Rarity.INSANE.toString())){
                       announce(e.getWhoClicked().getName(), e.getCurrentItem().getItemMeta().getDisplayName(), e.getWhoClicked().getUniqueId());
                   }
               }
           }
       } catch (NullPointerException ex){
           //piss self but no one has to know
       }

    }

}
