package me.crazyrain.vendrickbossfight.attacks;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.swing.plaf.SpinnerUI;
import java.util.UUID;

public class Enrage {

    VendrickBossFight plugin;
    static Vendrick vendrick;
    LivingEntity eVendrick;


    public Enrage(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    public void init(Vendrick vendrick){
        Enrage.vendrick = vendrick;
        this.eVendrick = vendrick.getVendrick();
        startAttack();
    }


    public void startAttack(){
        
        new BukkitRunnable(){
            int line = 0;
            @Override
            public void run() {
                switch (line){
                    case 1:
                        for (UUID id : plugin.fighting){
                            Bukkit.getPlayer(id).sendMessage(Lang.ENRAGE1.toString());
                        }
                        break;
                    case 2:
                        for (UUID id : plugin.fighting){
                            Bukkit.getPlayer(id).sendMessage(Lang.ENRAGE2.toString());
                        }
                        break;
                    case 3:
                        for (UUID id : plugin.fighting){
                            Bukkit.getPlayer(id).sendMessage(Lang.ENRAGE3.toString());
                            eVendrick.getWorld().playSound(eVendrick.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.8f);
                        }
                        startParticle(eVendrick.getLocation());
                        break;
                    case 4:
                        for (UUID id : plugin.fighting){
                            Bukkit.getPlayer(id).sendMessage(Lang.ENRAGE4.toString());
                        }
                        break;
                }
                line++;
            }
        }.runTaskTimer(plugin, 0, 40);


        
        new BukkitRunnable(){

            @Override
            public void run() {
                vendrick.stopAttack();
            }
        }.runTaskLater(plugin, 20 * 10);
    }

    public void startParticle(Location startLoc){
        new BukkitRunnable(){
            double move = -0.1;
            @Override
            public void run() {
                startLoc.getWorld().spawnParticle(Particle.REDSTONE, startLoc.add(0,move, 0), 5, new Particle.DustOptions(Color.RED, 3));
                startLoc.getWorld().playSound(startLoc, Sound.BLOCK_SAND_BREAK, 1.0f, 1.0f);
                move -= 0.1;

                if (move <= -1){
                    startLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, startLoc, 1);
                    startLoc.getWorld().strikeLightning(startLoc);
                    eVendrick.addPotionEffect(PotionEffectType.SPEED.createEffect(10000, 2));
                    eVendrick.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(10000, 1));
                    for (UUID id : plugin.fighting){
                        Bukkit.getPlayer(id).setVelocity(new Vector(0,0.7,0));
                        Bukkit.getPlayer(id).getWorld().playSound(startLoc, Sound.ENTITY_WITHER_SPAWN, 4.0f, 1.0f);
                        Bukkit.getPlayer(id).getWorld().playSound(startLoc, Sound.ENTITY_GENERIC_EXPLODE, 4.0f, 1.3f);
                        Bukkit.getPlayer(id).sendMessage(ChatColor.RED + "The ground below you is starting to shatter!");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

   
}
