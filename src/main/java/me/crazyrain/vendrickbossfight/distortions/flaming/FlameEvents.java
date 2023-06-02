package me.crazyrain.vendrickbossfight.distortions.flaming;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.AttackCharge;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class FlameEvents implements Listener {

    VendrickBossFight plugin;
    Inferno inferno;

    boolean removing = false;
    boolean infernoing = false;

    public FlameEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onFlameAttack(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)){
            return;
        }

        if (e.getEntity().getScoreboardTags().contains("venFlame")){
            if (!removing){
                removeFireRes(e.getEntity());
                removing = true;
            }

            if (!infernoing){
                int chance = (int) (Math.random() * 17);

                if (chance >= 13){
                    for (UUID p : plugin.fighting){
                        AttackCharge charge = new AttackCharge(ChatColor.GOLD + "" + ChatColor.BOLD + "Inferno", Bukkit.getPlayer(p));
                        startInferno(e.getEntity());
                        inferno = new Inferno(plugin.vendrick.getVendrick(), plugin.fighting);
                        infernoing = true;
                    }
                }
            }
        }
    }

    @EventHandler
    public void resetVariables(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("Vendrick")){
            removing = false;
            infernoing = false;
        }
    }

    public void startInferno(Entity e){
        new BukkitRunnable(){
            int count = 0;
            float pitch = 0.0f;
            @Override
            public void run() {
                if (count == 5){
                    inferno.blast();
                    e.getWorld().playSound(e.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.4f);
                    e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 20);
                    e.getWorld().playSound(e.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 3.0f, 1.0f);
                    infernoing = false;
                    cancel();
                }

                e.getWorld().playSound(e.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, pitch);
                e.getWorld().playEffect(e.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);

                count++;
                pitch += 0.5;
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public void removeFireRes(Entity e){
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
                            if (p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                                p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                                p.spawnParticle(Particle.FLAME, p.getLocation(), 10);
                                p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.8f);
                                p.sendMessage(Lang.NOFIRE.toString());
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 3);
    }

    @EventHandler
    public void ignitePlayer(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) e.getEntity();
        if (e.getDamager().getScoreboardTags().contains("venFlame")){
            player.setFireTicks(60);
        }
    }

}
