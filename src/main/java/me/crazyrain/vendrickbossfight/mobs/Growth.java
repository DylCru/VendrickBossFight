package me.crazyrain.vendrickbossfight.mobs;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Growth {

    Location loc;
    VendrickBossFight plugin;
    public LivingEntity growth;

    public Growth(Location loc, VendrickBossFight plugin){
        this.loc = loc.clone().add(0,-2,0);
        this.plugin = plugin;
        setupGrowth();
        this.loc = getValidLocation();
        spawnGrowth(this.loc.getBlockY());
    }

    public void setupGrowth(){
        growth = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.HUSK);
        ((Husk) growth).setBaby();
        growth.setCustomName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Eternal Growth");
        growth.setCustomNameVisible(true);
        AttributeModifier modifier = new AttributeModifier(Objects.requireNonNull(growth.getCustomName()), 30, AttributeModifier.Operation.ADD_NUMBER);
        growth.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(modifier);
        growth.setHealth(50);
        growth.getEquipment().setItemInMainHand(ItemManager.growthSword);
        growth.getEquipment().setItemInOffHand(ItemManager.growthSword);
        growth.setInvulnerable(true);
        growth.setGravity(false);
        growth.setGlowing(true);
        growth.setMetadata("Growth", new FixedMetadataValue(plugin, "growth"));
    }

    private Location getValidLocation(){
        Location testLoc = loc.clone().add(0,2,0);
        while (!testLoc.getBlock().getType().equals(Material.AIR)){
            testLoc = testLoc.clone().add(0,1,0);
        }
        return testLoc;
    }

    public void spawnGrowth(int targetY){
        new BukkitRunnable(){
            int timer = 0;
            Location loc = growth.getLocation().clone();
            @Override
            public void run() {
                if (loc.getBlockY() >= targetY){
                    growth.setInvulnerable(false);
                    growth.setGravity(true);
                    growth.setGlowing(false);
                    cancel();
                }

                loc = growth.getLocation().clone();
                loc.setY(loc.getY() + 0.1);
                growth.teleport(loc);
                loc.getWorld().playSound(loc, Sound.BLOCK_AZALEA_LEAVES_BREAK, 1.0f, 1.0f);
                timer++;
            }
        }.runTaskTimer(plugin, 0, 2);
    }

}
