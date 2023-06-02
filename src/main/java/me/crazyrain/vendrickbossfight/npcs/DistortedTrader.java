package me.crazyrain.vendrickbossfight.npcs;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.logging.Level;

public class DistortedTrader {

    VendrickBossFight plugin;

    public DistortedTrader(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    public boolean checkEntity(World world, Location location){
        try{
            LivingEntity check = (LivingEntity) world.spawnEntity(location.subtract(0,120,0), EntityType.valueOf(plugin.getConfig().getString("trader-type")));
            check.setCustomName("test");
            check.remove();
        } catch (ClassCastException | IllegalArgumentException e){
            return false;
        }
        return true;
    }

    public boolean spawnTrader(World world, Location location){
        try{
            if (!checkEntity(world, location)){
                throw new ClassCastException();
            }

            LivingEntity trader = (LivingEntity) world.spawnEntity(location.add(0,120,0), EntityType.valueOf(plugin.getConfig().getString("D-trader-type")));
            trader.setInvulnerable(true);
            trader.setSilent(true);
            trader.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "Distorted Merchant");
            trader.setMetadata("DistortedMerchant", new FixedMetadataValue(plugin, "distortedmerchant"));
            trader.addScoreboardTag("DistortedMerchant");
            trader.addScoreboardTag("VenMerchant");

            ArmorStand noMove = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
            noMove.setMarker(true);
            noMove.setVisible(false);
            noMove.setInvulnerable(true);
            noMove.setSmall(true);
            noMove.setMetadata("DistortedMerchant", new FixedMetadataValue(plugin, "distortedmerchant"));
            noMove.addScoreboardTag("VenMerchant");
            noMove.addPassenger(trader);

        } catch (IllegalArgumentException | ClassCastException e){
            plugin.getLogger().log(Level.WARNING, "An invalid entity was entered in the config. The Distorted Merchant was unable to be spawned.");
            return false;
        }
        return true;
    }
}
