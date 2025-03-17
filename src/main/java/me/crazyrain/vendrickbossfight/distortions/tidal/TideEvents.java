package me.crazyrain.vendrickbossfight.distortions.tidal;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.AttackCharge;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TideEvents implements Listener {

    VendrickBossFight plugin;
    Tsunami tsunami;

    boolean removing = false;
    boolean waving = false;
    boolean spawnSquids = false;

    public TideEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        if (e.getEntity().getType() == EntityType.VINDICATOR){
            if (!(e.getDamager() instanceof Player)){
                return;
            }

            if (e.getEntity().getScoreboardTags().contains("venTide")){
                if (!spawnSquids){
                    SquidShield shield = new SquidShield(plugin.vendrick, plugin.fighting);
                    spawnSquids = true;
                }

                if (!removing){
                    removeSpeed(e.getDamager());
                }
//                if (!waving){
//                    int chance = (int) (Math.random() * 17);
//
//                    if (chance >= 13){
//                        for (UUID p : plugin.fighting){
//                            AttackCharge charge = new AttackCharge(ChatColor.BLUE + "" + ChatColor.BOLD + "Tsunami", Bukkit.getPlayer(p));
//                            startWave(e.getEntity());
//                            tsunami = new Tsunami(plugin.vendrick, plugin.fighting);
//                            waving = true;
//                        }
//                    }
//                }
            }
        }
    }

//    public void startWave(Entity e){
//        new BukkitRunnable(){
//            int count = 0;
//            float pitch = 0.0f;
//            @Override
//            public void run() {
//                if (count == 5){
//                    tsunami.spawnWaves();
//                    e.getWorld().spawnParticle(Particle.WATER_SPLASH, e.getLocation(), 20);
//                    e.getWorld().playSound(e.getLocation(), Sound.ENTITY_HOSTILE_SPLASH, 3.0f, 2.0f);
//                    waving = false;
//                    cancel();
//                }
//
//                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_HOSTILE_SPLASH, 2.0f, pitch);
//                e.getWorld().spawnParticle(Particle.WATER_SPLASH, e.getLocation(), 20);
//
//                count++;
//                pitch += 0.5;
//            }
//        }.runTaskTimer(plugin, 0, 10);
//    }

    public void removeSpeed(Entity e){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (e.isDead()) {
                    cancel();
                }

                for (Entity en : e.getNearbyEntities(10, 10, 10)) {
                    if (en instanceof Player) {
                        Player p = (Player) en;
                        if (plugin.fighting.contains(p.getUniqueId())) {
                            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                                p.removePotionEffect(PotionEffectType.SPEED);
                                p.spawnParticle(Particle.WATER_BUBBLE, p.getLocation(), 20);
                                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0f, 1.8f);
                                p.sendMessage(Lang.NOSPEED.toString());
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 3);
    }

//    //remove tidal wave blocks
//    @EventHandler
//    public void removeWave(EntityChangeBlockEvent e){
//        if (e.getEntity().getType() == EntityType.FALLING_BLOCK){
//            FallingBlock fb = (FallingBlock) e.getEntity();
//            if (fb.getBlockData().getMaterial() == Material.ICE){
//                Bukkit.broadcastMessage("ice landed");
////                e.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    public void killShield(EntityDamageByEntityEvent e){
        if (e.getEntity().hasMetadata("SquidShield")){
            if (!plugin.fighting.contains(e.getDamager().getUniqueId()) && !e.getDamager().isOp()){
                e.setCancelled(true);
                e.getDamager().sendMessage(ChatColor.DARK_GRAY + ""  + ChatColor.ITALIC + "Your pure attack cannot pierce through the shield");
                return;
            }

            e.getEntity().getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, e.getEntity().getLocation().add(0,1.5,0), 30);
            e.getEntity().remove();
            plugin.squids++;
        }
    }

    @EventHandler
    public void stopDrops(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("SquidShield")){
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void resetVariables(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("Vendrick")){
            removing = false;
            waving = false;
            spawnSquids = false;

            for (Entity en : e.getEntity().getNearbyEntities( 40, 40, 40)){
                if (en.hasMetadata("SquidShield")){
                    en.remove();
                }
            }
            plugin.squids = 4;
        }
    }

    @EventHandler
    public void preventDeath(EntityDamageEvent e){
        if (e.getEntity().hasMetadata("SquidShield")){
            if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) || e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
                e.setCancelled(true);
            }
        }
    }


}
