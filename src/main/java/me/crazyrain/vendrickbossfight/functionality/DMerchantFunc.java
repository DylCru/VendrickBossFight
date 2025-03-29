package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import me.crazyrain.vendrickbossfight.npcs.DistortedTrader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class DMerchantFunc implements Listener {

    VendrickBossFight plugin;

    public DMerchantFunc(VendrickBossFight plugin){
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
            if (player.getInventory().getItemInMainHand().equals(ItemManager.DtradeLoc)) {
                DistortedTrader trader = new DistortedTrader(VendrickBossFight.getPlugin(VendrickBossFight.class));
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
        if (e.getRightClicked().getScoreboardTags().contains("DistortedMerchant")){
            Merchant merchant = Bukkit.createMerchant("Distorted Merchant");

            ItemStack flameCore = ItemManager.flameCore.clone();
            ItemStack waveCore = ItemManager.waveCore.clone();
            ItemStack elecCore = ItemManager.voltaicCore.clone();
            ItemStack voidCore = ItemManager.voidCore.clone();
            ItemStack vFrag = ItemManager.voidCoreFragment.clone();
            ItemStack frag = ItemManager.eternalFragment.clone();
            ItemStack ess = ItemManager.essenceOfEternity.clone();

            List<MerchantRecipe> recipeList = new ArrayList<>();

            frag.setAmount(3);
            MerchantRecipe partA = new MerchantRecipe(ItemManager.catalystPartA, 10000);
            partA.addIngredient(frag);
            partA.addIngredient(new ItemStack(Material.END_CRYSTAL));
            recipeList.add(partA);

            ess.setAmount(3);
            MerchantRecipe partB = new MerchantRecipe(ItemManager.catalystPartB, 10000);
            partB.addIngredient(ess);
            partB.addIngredient(new ItemStack(Material.END_CRYSTAL));
            recipeList.add(partB);

            MerchantRecipe catalyst = new MerchantRecipe(ItemManager.theCatalyst, 10000);
            catalyst.addIngredient(ItemManager.catalystPartA);
            catalyst.addIngredient(ItemManager.catalystPartB);
            recipeList.add(catalyst);

            vFrag.setAmount(3);
            MerchantRecipe vCore = new MerchantRecipe(ItemManager.voidCore, 10000);
            vCore.addIngredient(vFrag);
            recipeList.add(vCore);

            MerchantRecipe distortedStar = new MerchantRecipe(ItemManager.volatileStar, 10000);
            distortedStar.addIngredient(ItemManager.eternalStar);
            distortedStar.addIngredient(ItemManager.theCatalyst);
            recipeList.add(distortedStar);

            flameCore.setAmount(3);
            MerchantRecipe flameStar = new MerchantRecipe(ItemManager.flamingStar, 10000);
            flameStar.addIngredient(flameCore);
            flameStar.addIngredient(ItemManager.volatileStar);
            recipeList.add(flameStar);

            waveCore.setAmount(3);
            MerchantRecipe tideStar = new MerchantRecipe(ItemManager.tidalStar, 10000);
            tideStar.addIngredient(waveCore);
            tideStar.addIngredient(ItemManager.volatileStar);
            recipeList.add(tideStar);

            elecCore.setAmount(3);
            MerchantRecipe stormStar = new MerchantRecipe(ItemManager.stormStar, 10000);
            stormStar.addIngredient(elecCore);
            stormStar.addIngredient(ItemManager.volatileStar);
            recipeList.add(stormStar);

            voidCore.setAmount(3);
            MerchantRecipe darkStar = new MerchantRecipe(ItemManager.darkStar, 10000);
            darkStar.addIngredient(voidCore);
            darkStar.addIngredient(ItemManager.volatileStar);
            recipeList.add(darkStar);

            merchant.setRecipes(recipeList);

            e.getPlayer().openMerchant(merchant, true);
        }
    }
}
