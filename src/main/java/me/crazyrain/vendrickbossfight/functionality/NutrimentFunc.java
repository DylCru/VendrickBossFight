package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class NutrimentFunc implements Listener {

    @EventHandler
    public void onStartEat(PlayerInteractEvent e){
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){return;}

        if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {return;}
        if (!e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PUMPKIN_PIE)){return;}
        if (e.getPlayer().getInventory().getItemInMainHand().equals(ItemManager.nutrimentU) || e.getPlayer().getInventory().getItemInOffHand().equals(ItemManager.nutrimentU)){
            if (VendrickBossFight.plugin.getConfig().getStringList("disabled-items").contains(e.getPlayer().getEquipment().getItemInMainHand().getItemMeta().getDisplayName())){
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "This item is currently disabled!");
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e){
        if (!(e.getItem().hasItemMeta())){
            return;
        }
        if (!(e.getItem().getItemMeta().hasDisplayName())){
            return;
        }
        if (e.getItem().getType().equals(Material.PUMPKIN_PIE)){

            Player player = e.getPlayer();
            if (player.getInventory().getItemInMainHand().equals(ItemManager.nutrimentU) || player.getInventory().getItemInOffHand().equals(ItemManager.nutrimentU)){
                e.setCancelled(true);
                player.getInventory().getItemInMainHand().setAmount(1);
                player.updateInventory();

                player.addPotionEffect(PotionEffectType.HEAL.createEffect(1, 3));
                player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(20 * 6, 1));
                player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(20 * 5, 2));
                player.setFoodLevel(player.getFoodLevel() + 12);

                player.setCooldown(Material.PUMPKIN_PIE, 20 * 35);

                player.spawnParticle(Particle.FLASH, player.getLocation(), 1);
                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1, 2.0f);
            }


            if (player.getInventory().getItemInMainHand().equals(ItemManager.nutrimentOfTheInfinite) || player.getInventory().getItemInOffHand().equals(ItemManager.nutrimentOfTheInfinite)){
                e.setCancelled(true);
                player.getInventory().getItemInMainHand().setAmount(1);
                player.updateInventory();

                player.addPotionEffect(PotionEffectType.HEAL.createEffect(1, 1));
                player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(20 * 3, 1));
                player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(20 * 5, 0));
                player.setFoodLevel(player.getFoodLevel() + 6);

                player.setCooldown(Material.PUMPKIN_PIE, 20 * 25);

                player.spawnParticle(Particle.PORTAL, player.getLocation(), 5);
                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1, 2.0f);
            }
        }
    }
}
