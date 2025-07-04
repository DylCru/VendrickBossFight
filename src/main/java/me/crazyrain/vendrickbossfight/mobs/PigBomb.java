package me.crazyrain.vendrickbossfight.mobs;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class PigBomb {

    VendrickBossFight plugin;

    float time;
    Location loc;
    double xDir;
    double zDir;
    LivingEntity pigBomb;

    ArmorStand timeStand;

    public PigBomb(Location loc, double xDir, double zDir, VendrickBossFight plugin){
        this.loc = loc;
        this.xDir = xDir;
        this.zDir = zDir;
        this.plugin = plugin;
        this.time = 7.0F;

        spawnPig();

        new BukkitRunnable(){
            @Override
            public void run() {
                spawnTimeStand();
                startCountdown();
            }
        }.runTaskLater(plugin, 20);
    }

    public void spawnPig(){
        pigBomb = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.PIG);
        pigBomb.setHealth(1);
        pigBomb.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Pig Bomb");
        pigBomb.setCustomNameVisible(true);
        ((Pig) pigBomb).setAdult();
        pigBomb.addPotionEffect(PotionEffectType.SLOW.createEffect(100000, 20));
        pigBomb.setMetadata("PigBomb", new FixedMetadataValue(plugin, "pigbomb"));

        Vector pv = pigBomb.getLocation().getDirection();
        pv.setX(xDir);
        pv.setZ(zDir);
        pigBomb.setVelocity(pv);
    }

    public void spawnTimeStand(){
        timeStand = (ArmorStand) loc.getWorld().spawnEntity(pigBomb.getLocation().clone().add(0, -0.75, 0), EntityType.ARMOR_STAND);
        timeStand.setCustomName(ChatColor.GREEN + "" + time + "s");
        timeStand.setGravity(false);
        timeStand.setCustomNameVisible(true);
        timeStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        timeStand.setVisible(false);
        timeStand.setMetadata("BombTimer", new FixedMetadataValue(plugin, "bombtimer"));
        timeStand.setMarker(true);
        pigBomb.addPassenger(timeStand);
    }

    public void startCountdown(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (pigBomb.isDead()){
                    removeTimeStand();
                    cancel();
                }

                if (time > 5){
                    timeStand.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + "" + time + "s");
                } else if (time > 2){
                    timeStand.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + time + "s");
                } else {
                    timeStand.setCustomName(ChatColor.RED + "" + ChatColor.BOLD +  "" + time + "s");
                }
                time -= 0.1;
                time = Float.parseFloat(String.valueOf(BigDecimal.valueOf(time).setScale(1, RoundingMode.DOWN)));
                if (time <= 0){
                    explode();
                    timeStand.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "BOOM!");
                    removeTimeStand();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    public void removeTimeStand(){
        new BukkitRunnable(){
            @Override
            public void run() {
                timeStand.remove();
            }
        }.runTaskLater(plugin, 20);
    }

    public void explode(){
        pigBomb.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, pigBomb.getLocation(), 2);
        pigBomb.setHealth(0);
        for (UUID id : plugin.getFightManager().getFighting()){
            Player player = Bukkit.getPlayer(id);
            player.damage(5);
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "BOOM! " + ChatColor.RED + "A pig bomb exploded!");
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        }
    }
}
