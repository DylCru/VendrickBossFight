package me.crazyrain.vendrickbossfight.attacks;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.mobs.Growth;
import me.crazyrain.vendrickbossfight.vendrick.Vendrick;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import java.util.UUID;

public class ZombieHoard implements Listener {

    VendrickBossFight plugin;

    public static Integer zombieAmount = 6;
    public static Integer spawned = 0;

    static Vendrick vendrick;

    public ZombieHoard(VendrickBossFight plugin) {
        this.plugin = plugin;
    }

    public void init(Vendrick vendrick){
        ZombieHoard.vendrick = vendrick;
        startAttack();
    }

    public void startAttack(){
        Location loc = new Location(vendrick.getEntity().getWorld(), vendrick.getEntity().getLocation().getBlockX(), vendrick.getEntity().getLocation().getBlockY() + 4, vendrick.getEntity().getLocation().getBlockZ());
        for (UUID id : plugin.getFightManager().getFighting()){
            Bukkit.getPlayer(id).sendMessage(ChatColor.RED + "Vendrick is using the power of " + ChatColor.DARK_GREEN  + "" + ChatColor.BOLD + "THE HORDE" + ChatColor.RED + " to protect himself!");
        }

        zombieAmount = 6;
        spawned = 0;

        spawnZombies(vendrick.getEntity().getLocation());
    }


    public void spawnZombies(Location loc){
        LivingEntity venMob = vendrick.getEntity();
        for (int i = 0; i < 6; i++){
            int x = 0, z= 0;
            if (i % 2 == 0){
                x = 1;
            } else {
                x = -1;
            }
            if (i >= 4){
                z = -1;
            } else if (i >= 2) {
                z = 1;
            }

            Growth growth = new Growth(venMob.getLocation().add(x,0,z), plugin);
        }
        spawned = 6;
        vendrick.setSkipable(true);
    }


    @EventHandler
    public void onVenZombieKill(EntityDeathEvent e){
        if (e.getEntity() instanceof Zombie){
            if (e.getEntity().hasMetadata("Growth")){
                zombieAmount -= 1;

                if (zombieAmount <= 0){
                    vendrick.stopAttack();
                    if (plugin.getFightManager().getRuneHandler() != null) {
                        plugin.getFightManager().getRuneHandler().setPaused(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void stopAttack(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Projectile){
            return;
        }
        if (!plugin.getFightManager().getFighting().contains(e.getDamager().getUniqueId()) && !e.getDamager().isOp()){
            if (e.getEntity().hasMetadata("Growth")){
                e.setCancelled(true);
                e.getDamager().sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "The fumes from this growth make a pure soul like yours be completely powerless against it");
            }
        }
    }

    @EventHandler
    public void stopFallDmg(EntityDamageEvent e){
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
            if (e.getEntity() instanceof Husk){
                if (e.getEntity().hasMetadata("Growth")){
                        e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void stopDrops(EntityDeathEvent e){
        if (e.getEntity() instanceof Husk){
            if (e.getEntity().hasMetadata("Growth")){
                e.getDrops().clear();
            }
        }
    }
}
