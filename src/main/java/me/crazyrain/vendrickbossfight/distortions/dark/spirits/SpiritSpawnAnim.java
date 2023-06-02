package me.crazyrain.vendrickbossfight.distortions.dark.spirits;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class SpiritSpawnAnim {

    Location vLoc;
    Color partColor;
    VendrickBossFight plugin;

    public SpiritSpawnAnim(Location vLoc, Color partColor, VendrickBossFight plugin){
        this.vLoc = vLoc;
        this.partColor = partColor;
        this.plugin = plugin;
        spawnParticle();
    }

    // Spawn Particle above -> strike lightning  -> move particle down -> spawn spirit

    private void spawnParticle(){
        new BukkitRunnable(){
            int timer = 0;
            @Override
            public void run() {
                if (timer == 20){
                    lightningEffect();
                    cancel();
                }

                vLoc.getWorld().spawnParticle(Particle.REDSTONE, vLoc.clone().add(0,3,0), 3, new Particle.DustOptions(partColor, 6));
                vLoc.getWorld().spawnParticle(Particle.REDSTONE, vLoc.clone().add(0,-1,0), 3, new Particle.DustOptions(partColor, 2));
                vLoc.getWorld().spawnParticle(Particle.REDSTONE, vLoc.clone().add(1,1.5,0), 3, new Particle.DustOptions(partColor, 2));
                vLoc.getWorld().spawnParticle(Particle.REDSTONE, vLoc.clone().add(0,1.5,1), 3, new Particle.DustOptions(partColor, 2));
                vLoc.getWorld().spawnParticle(Particle.REDSTONE, vLoc.clone().add(-1,1.5,0), 3, new Particle.DustOptions(partColor, 2));
                vLoc.getWorld().spawnParticle(Particle.REDSTONE, vLoc.clone().add(0,1.5,-1), 3, new Particle.DustOptions(partColor, 2));
                timer++;
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    private void lightningEffect(){
        new BukkitRunnable(){
            int turn = 0;
            int timer = 0;
            @Override
            public void run() {
                if (timer == 16){
                    vLoc.getWorld().playSound(vLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 0.9f);
                    vLoc.getWorld().playSound(vLoc, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.7f, 0.7f);
                    vLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vLoc, 2);
                    cancel();
                }
                switch (turn){
                    case 0:
                        vLoc.getWorld().strikeLightningEffect(vLoc.clone().add(1,-6,1));
                        vLoc.getWorld().playSound(vLoc, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.7f, 0.9f);
                        break;
                    case 1:
                        vLoc.getWorld().strikeLightningEffect(vLoc.clone().add(-1,-6,1));
                        vLoc.getWorld().playSound(vLoc, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.7f, 0.9f);
                        break;
                    case 2:
                        vLoc.getWorld().strikeLightningEffect(vLoc.clone().add(1,-6,-1));
                        vLoc.getWorld().playSound(vLoc, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.7f, 0.9f);
                        break;
                    case 3:
                        vLoc.getWorld().strikeLightningEffect(vLoc.clone().add(-1,-6,-1));
                        vLoc.getWorld().playSound(vLoc, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.7f, 0.9f);
                        break;
                }
                turn++;
                if (turn == 4){
                    turn = 0;
                }
                timer++;
            }
        }.runTaskTimer(plugin, 0, 5);
    }
}
