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

public class EternalTrader {

    VendrickBossFight plugin;

    public EternalTrader(VendrickBossFight plugin){
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

            LivingEntity trader = (LivingEntity) world.spawnEntity(location.add(0,120,0), EntityType.valueOf(plugin.getConfig().getString("trader-type")));
            trader.setInvulnerable(true);
            trader.setSilent(true);
            trader.setCustomName(ChatColor.BOLD + "" + ChatColor.DARK_BLUE + "Eternal Merchant");
            trader.setMetadata("EternalMerchant", new FixedMetadataValue(plugin, "eternalmerchant"));
            trader.addScoreboardTag("EternalMerchant");
            trader.addScoreboardTag("VenMerchant");

            ArmorStand noMove = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
            noMove.setMarker(true);
            noMove.setVisible(false);
            noMove.setInvulnerable(true);
            noMove.setSmall(true);
            noMove.setMetadata("EternalMerchant", new FixedMetadataValue(plugin, "eternalmerchant"));
            noMove.addPassenger(trader);

        } catch (IllegalArgumentException | ClassCastException e){
            plugin.getLogger().log(Level.WARNING, "An invalid entity was entered in the config. The Eternal Merchant was unable to be spawned.");
            return false;
        }
        return true;
    }
}
