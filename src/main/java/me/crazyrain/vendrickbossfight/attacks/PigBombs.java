package me.crazyrain.vendrickbossfight.attacks;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.mobs.PigBomb;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;


public class PigBombs implements Listener {

    VendrickBossFight plugin;

    List<UUID> players;

    public static Integer pigAmount = 4;
    public static boolean pigsDead = false;

    public PigBombs(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    public void init(Vendrick vendrick, List<UUID> players){
        this.players = players;
        pigAmount = 4;
        pigsDead = false;
        spawnPigs(plugin.vendrick.getVendrick().getLocation().add(0,4,0));
    }

    public void spawnPigs(Location loc){
        new BukkitRunnable(){
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    double rand = Math.random();
                    switch (i){
                        case 0:
                            PigBomb bomb = new PigBomb(loc, 1, rand, plugin);
                            break;
                        case 1:
                            PigBomb bomb2 = new PigBomb(loc, -1, rand, plugin);
                            break;
                        case 2:
                            PigBomb bomb3 = new PigBomb(loc, -rand, 1, plugin);
                            break;
                        case 3:
                            PigBomb bomb4 = new PigBomb(loc, -rand, -1, plugin);
                            break;
                    }
                }
                countDown();
                plugin.vendrick.setSkipable(true);

                for (UUID p : plugin.fighting){
                    Bukkit.getPlayer(p).sendMessage(ChatColor.RED + "Vendrick threw " + ChatColor.GOLD + ChatColor.BOLD + "PIG BOMBS! " + ChatColor.RED + "Diffuse them quickly!");
                }

            }
        }.runTaskLater(plugin, 20 * 3);

    }

    public void countDown(){
        new BukkitRunnable(){
            @Override
            public void run() {
                plugin.vendrick.stopAttack();
                if (plugin.runeHandler != null) {
                    plugin.runeHandler.setPaused(false);
                }
            }
        }.runTaskLater(plugin, 20 * 7);
    }

    @EventHandler
    public void onPigKill(EntityDeathEvent e){
        if (e.getEntity() instanceof Pig){
            if (e.getEntity().hasMetadata("PigBomb")){
                if (e.getEntity().getKiller() == null){
                    return;
                }
                pigAmount -= 1;

                if (pigAmount <= 0){
                    plugin.vendrick.stopAttack();
                    if (plugin.runeHandler != null) {
                        plugin.runeHandler.setPaused(false);
                    }
                    for (UUID p : plugin.fighting){
                        Bukkit.getPlayer(p).sendMessage(ChatColor.GREEN + "All the pig bombs were diffused!");
                        Bukkit.getPlayer(p).playSound(Bukkit.getPlayer(p).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10 , 1f);
                    }
                    pigsDead = true;
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e){
        if (e.getHitEntity() == null){
            return;
        }

        if (e.getHitEntity().hasMetadata("PigBomb")){
            e.getHitEntity().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, e.getHitEntity().getLocation(), 20);
            e.getHitEntity().getWorld().playSound(e.getHitEntity().getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 2.0f);
        }
    }

    @EventHandler
    public void stopPigAttack(EntityDamageByEntityEvent e){
        if (!plugin.fighting.contains(e.getDamager().getUniqueId()) && !e.getDamager().isOp()){
            if (e.getEntity().hasMetadata("PigBomb")){
                e.setCancelled(true);
                e.getDamager().sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "The defusal process is too confusing for a soul as pure as yours");
            }
        }
    }

    @EventHandler
    public void stopFallDmg(EntityDamageEvent e){
        if (e.getEntity() instanceof Pig){
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                if (e.getEntity().hasMetadata("PigBomb")){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void stopDrops(EntityDeathEvent e){
        if (e.getEntity() instanceof Pig){
            if (e.getEntity().hasMetadata("PigBomb")){
                e.getDrops().clear();
            }
        }
    }

    public void skipAttack(){
        for (Entity e : plugin.vendrick.getVendrick().getNearbyEntities(30,30,30)){
            if (e.hasMetadata("PigBomb")){
                e.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation().clone().add(0,0.5,0), 10);
                e.remove();
            }
        }
        pigsDead = true;
        plugin.vendrick.stopAttack();
        if (plugin.runeHandler != null) {
            plugin.runeHandler.setPaused(false);
        }
        for (UUID p : plugin.fighting){
            Bukkit.getPlayer(p).sendMessage(ChatColor.GREEN + "All the pig bombs were diffused!");
            Bukkit.getPlayer(p).playSound(Bukkit.getPlayer(p).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10 , 1f);
        }
    }
}
