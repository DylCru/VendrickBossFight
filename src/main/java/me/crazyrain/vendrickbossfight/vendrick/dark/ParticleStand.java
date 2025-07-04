package me.crazyrain.vendrickbossfight.vendrick.dark;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleStand {

    Location loc;
    Color colour;
    VendrickBossFight plugin;
    LivingEntity toFollow;
    ArmorStand stand;
    int timer = 8;
    boolean active = true;

    public ParticleStand(Location loc, Color colour, VendrickBossFight plugin, LivingEntity toFollow){
        this.loc = loc;
        this.colour = colour;
        this.plugin = plugin;
        this.toFollow = toFollow;
        spawnStand();
        moveStand();
        particles();
        countdown();
    }

    public void spawnStand(){
        stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
    }

    public void moveStand(){
        new BukkitRunnable(){
            float rot = -90f;
            @Override
            public void run() {
                if (toFollow.isDead()){
                    stand.remove();
                    cancel();
                }
                if (!active){
                    cancel();
                }

                Location ballLoc = stand.getLocation();
                ballLoc.setX(toFollow.getLocation().getX());
                ballLoc.setY(toFollow.getLocation().getY());
                ballLoc.setZ(toFollow.getLocation().getZ());
                ballLoc.setYaw(rot);
                rot += 20f;
                stand.teleport(ballLoc);
            }
        }.runTaskTimer(plugin,0, 1);
    }

    public void particles(){
        new BukkitRunnable(){
            double move = 0.0;
            boolean up = true;
            @Override
            public void run() {
                if (toFollow.isDead()){
                    cancel();
                }
                if (!active){
                    cancel();
                }

                if (up){
                    move += 0.03;
                    if (move > 1){
                        up = false;
                    }
                } else {
                    move -= 0.03;
                    if (move < -1){
                        up = true;
                    }
                }

                Location loc = stand.getEyeLocation().clone().add(0,move,0);
                loc.add(stand.getLocation().getDirection().multiply(2));
                loc.add(0,-0.5,0);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, new Particle.DustOptions(colour, 1));
            }
        }.runTaskTimer(plugin,0, 1);
    }

    public void countdown(){
        new BukkitRunnable(){
            @Override
            public void run() {
                timer--;
                if (timer == 0){
                    stand.remove();
                    active = false;
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0 ,20);
    }

}

