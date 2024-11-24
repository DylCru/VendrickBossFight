package me.crazyrain.vendrickbossfight.distortions.tidal;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tsunami {

    Vendrick vendrick;
    List<UUID> fighting;

    Location[] plusZ = new Location[5];
    Location[] negZ = new Location[5];
    Location[] plusX = new Location[5];
    Location[] negX = new Location[5];

    List<Location[]> locs = new ArrayList<>();

    FallingBlock wave;

    public Tsunami(Vendrick vendrick, List<UUID> fighting){
        this.vendrick = vendrick;
        this.fighting = fighting;
    }

    public void spawnWaves(){
        loadLocs();
        waveX();
        waveNegX();
        waveZ();
        waveNegZ();
    }

    public void loadLocs(){
        plusZ[0] = negZ[0] = negX[0] = vendrick.getVendrick().getLocation();

        plusX[0] = vendrick.getVendrick().getLocation().clone().add(1,0,0);
        plusX[1] = vendrick.getVendrick().getLocation().clone().add(1,0,1);
        plusX[2] = vendrick.getVendrick().getLocation().clone().add(1,0,2);
        plusX[3] = vendrick.getVendrick().getLocation().clone().add(1,0,-1);
        plusX[4] = vendrick.getVendrick().getLocation().clone().add(1,0,-2);

        negX[0] = vendrick.getVendrick().getLocation().clone().add(-1,0,0);
        negX[1] = vendrick.getVendrick().getLocation().clone().add(-1,0,1);
        negX[2] = vendrick.getVendrick().getLocation().clone().add(-1,0,2);
        negX[3] = vendrick.getVendrick().getLocation().clone().add(-1,0,-1);
        negX[4] = vendrick.getVendrick().getLocation().clone().add(-1,0,-2);

        negZ[0] = vendrick.getVendrick().getLocation().clone().add(0,0,-1);
        negZ[1] = vendrick.getVendrick().getLocation().clone().add(1,0,-1);
        negZ[2] = vendrick.getVendrick().getLocation().clone().add(2,0,-1);
        negZ[3] = vendrick.getVendrick().getLocation().clone().add(-1,0,-1);
        negZ[4] = vendrick.getVendrick().getLocation().clone().add(-2,0,-1);

        plusZ[0] = vendrick.getVendrick().getLocation().clone().add(0,0,1);
        plusZ[1] = vendrick.getVendrick().getLocation().clone().add(1,0,1);
        plusZ[2] = vendrick.getVendrick().getLocation().clone().add(2,0,1);
        plusZ[3] = vendrick.getVendrick().getLocation().clone().add(-1,0,1);
        plusZ[4] = vendrick.getVendrick().getLocation().clone().add(-2, 0,1);
    }

    public void waveX(){
        new BukkitRunnable(){
            int timer = 0;
            @Override
            public void run() {
                if (timer == 10){
                    cancel();
                }

                for (Location loc : plusX){
                    Location move = loc.add(1, 0,0);
                    Location spawn = move.clone().add(0,2,0);
                    wave = vendrick.getVendrick().getWorld().spawnFallingBlock(spawn, Material.ICE.createBlockData());
                    wave.setPersistent(true);
                    wave.setInvulnerable(true);

                    for(Entity e: loc.getWorld().getNearbyEntities(loc, 1,1,1)){
                        if (e == vendrick.getVendrick() || e instanceof FallingBlock){
                            continue;
                        }
                        if (e instanceof Player && VendrickBossFight.getPlugin(VendrickBossFight.class).fighting.contains(e.getUniqueId())){
                            e.setVelocity(new Vector(0,1,0));
                            ((Player) e).damage(3, vendrick.getVendrick());
                        }
                    }
                }

                timer++;
            }
        }.runTaskTimer(VendrickBossFight.getPlugin(VendrickBossFight.class), 0,1);
    }

    public void waveNegX(){
        new BukkitRunnable(){
            int timer = 0;
            @Override
            public void run() {
                if (timer == 10){
                    cancel();
                }

                for (Location loc : negX){
                    Location move = loc.add(-1, 0,0);
                    Location spawn = move.clone().add(0,2,0);
                    wave = vendrick.getVendrick().getWorld().spawnFallingBlock(spawn, Material.ICE.createBlockData());
                    wave.setPersistent(true);
                    wave.setInvulnerable(true);

                    for(Entity e: loc.getWorld().getNearbyEntities(loc, 1,1,1)){
                        if (e == vendrick.getVendrick() || e instanceof FallingBlock){
                            continue;
                        }
                        if (e instanceof Player && VendrickBossFight.getPlugin(VendrickBossFight.class).fighting.contains(e.getUniqueId())){
                            e.setVelocity(new Vector(0,1,0));
                            ((Player) e).damage(3, vendrick.getVendrick());
                        }
                    }
                }

                timer++;
            }
        }.runTaskTimer(VendrickBossFight.getPlugin(VendrickBossFight.class), 0,1);
    }

    public void waveZ(){
        new BukkitRunnable(){
            int timer = 0;
            @Override
            public void run() {
                if (timer == 10){
                    cancel();
                }

                for (Location loc : plusZ){
                    Location move = loc.add(0, 0,1);
                    Location spawn = move.clone().add(0,2,0);
                    wave = vendrick.getVendrick().getWorld().spawnFallingBlock(spawn, Material.ICE.createBlockData());
                    wave.setPersistent(true);
                    wave.setInvulnerable(true);

                    for(Entity e: loc.getWorld().getNearbyEntities(loc, 1,1,1)){
                        if (e == vendrick.getVendrick() || e instanceof FallingBlock){
                            continue;
                        }
                        if (e instanceof Player && VendrickBossFight.getPlugin(VendrickBossFight.class).fighting.contains(e.getUniqueId())){
                            e.setVelocity(new Vector(0,1,0));
                            ((Player) e).damage(3, vendrick.getVendrick());
                        }
                    }
                }

                timer++;
            }
        }.runTaskTimer(VendrickBossFight.getPlugin(VendrickBossFight.class), 0,1);
    }

    public void waveNegZ(){
        new BukkitRunnable(){
            int timer = 0;
            @Override
            public void run() {
                if (timer == 10){
                    cancel();
                }

                for (Location loc : negZ){
                    Location move = loc.add(0, 0,-1);
                    Location spawn = move.clone().add(0,2,0);
                    wave = vendrick.getVendrick().getWorld().spawnFallingBlock(spawn, Material.ICE.createBlockData());
                    wave.setPersistent(true);
                    wave.setInvulnerable(true);

                    for (Entity e: loc.getWorld().getNearbyEntities(loc, 1,1,1)){
                        if (e == vendrick.getVendrick() || e instanceof FallingBlock){
                            continue;
                        }
                        if (e instanceof Player && VendrickBossFight.getPlugin(VendrickBossFight.class).fighting.contains(e.getUniqueId())){
                            e.setVelocity(new Vector(0,1,0));
                            ((Player) e).damage(3, vendrick.getVendrick());
                        }
                    }
                }

                timer++;
            }
        }.runTaskTimer(VendrickBossFight.getPlugin(VendrickBossFight.class), 0,1);
    }
    
    

}
