package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.npcs.EternalTrader;
import me.crazyrain.vendrickbossfight.npcs.MaterialTrader;
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

public class MaterialMerchFunc implements Listener {

    VendrickBossFight plugin;

    public MaterialMerchFunc(VendrickBossFight plugin){
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
            if (player.getInventory().getItemInMainHand().equals(ItemManager.MtradeLoc)) {
                MaterialTrader trader = new MaterialTrader(VendrickBossFight.getPlugin(VendrickBossFight.class));
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
        if (e.getRightClicked().getScoreboardTags().contains("MaterialMerchant")){
            Merchant merchant = Bukkit.createMerchant("Material Merchant");

            ItemStack merchFrag = ItemManager.eternalFragment.clone();
            ItemStack merchEss = ItemManager.essenceOfEternity.clone();
            ItemStack merchInf = ItemManager.infinium.clone();

            List<MerchantRecipe> recipeList = new ArrayList<>();

            merchFrag.setAmount(2);
            merchEss.setAmount(2);
            merchInf.setAmount(2);

            MerchantRecipe fToE = new MerchantRecipe(ItemManager.essenceOfEternity, 10000);
            fToE.addIngredient(merchFrag);
            recipeList.add(fToE);

            MerchantRecipe fToI = new MerchantRecipe(ItemManager.infinium, 10000);
            fToI.addIngredient(merchFrag);
            recipeList.add(fToI);

            MerchantRecipe eToF = new MerchantRecipe(ItemManager.eternalFragment, 10000);
            eToF.addIngredient(merchEss);
            recipeList.add(eToF);

            MerchantRecipe eToI = new MerchantRecipe(ItemManager.infinium, 10000);
            eToI.addIngredient(merchEss);
            recipeList.add(eToI);

            MerchantRecipe iToF = new MerchantRecipe(ItemManager.eternalFragment, 10000);
            iToF.addIngredient(merchInf);
            recipeList.add(iToF);

            MerchantRecipe iToE = new MerchantRecipe(ItemManager.essenceOfEternity, 10000);
            iToE.addIngredient(merchInf);
            recipeList.add(iToE);

            MerchantRecipe stToE = new MerchantRecipe(ItemManager.infinium, 10000);
            stToE.addIngredient(ItemManager.shatterSpine);
            recipeList.add(stToE);

            MerchantRecipe hToE = new MerchantRecipe(ItemManager.infinium, 10000);
            hToE.addIngredient(ItemManager.vendrickHatchet);
            recipeList.add(hToE);

            merchant.setRecipes(recipeList);

            e.getPlayer().openMerchant(merchant, true);
        }
    }
}
