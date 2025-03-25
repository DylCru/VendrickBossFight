package me.crazyrain.vendrickbossfight.distortions.tidal;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStopEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.AttackCharge;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TideEvents implements Listener {

    VendrickBossFight plugin;

    boolean removing = false;
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
            }
        }
    }

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
    public void onVendrickDeath(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("Vendrick") && plugin.vendrick.getDistortion().equalsIgnoreCase("tidal")){
            resetVariables();
        }
    }

    public void resetVariables() {
        removing = false;
        spawnSquids = false;

        for (Entity squid : ((TidalVendrick) plugin.vendrick).getSheilds()) {
            try {
                squid.remove();
            } catch (Exception ignored) {}
        }
        plugin.squids = 0;
    }

    @EventHandler
    public void preventDeath(EntityDamageEvent e){
        if (e.getEntity().hasMetadata("SquidShield")){
            if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) || e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void clearSquidsOnFightStop(VendrickFightStopEvent e) {
        if (e.getDistortion().equalsIgnoreCase("tidal")) {
            resetVariables();
        }
    }
}
