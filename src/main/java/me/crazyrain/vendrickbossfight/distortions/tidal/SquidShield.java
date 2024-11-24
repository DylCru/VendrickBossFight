package me.crazyrain.vendrickbossfight.distortions.tidal;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class SquidShield {
    Vendrick vendrick;
    List<UUID> fighting;
    VendrickBossFight plugin = VendrickBossFight.getPlugin(VendrickBossFight.class);

    boolean spawn = true;

    public SquidShield(Vendrick vendrick, List<UUID> fighting){
        this.vendrick = vendrick;
        this.fighting = fighting;
        throwSpawn();
    }

    public void stopSpawn(){
        this.spawn = false;
    }

    public void throwSpawn(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (vendrick.getVendrick().isDead() || !plugin.venSpawned){
                    cancel();
                }

                if (plugin.squids > 1 && spawn){
                    ArmorStand spawn = (ArmorStand) vendrick.getVendrick().getWorld().spawnEntity(vendrick.getVendrick().getLocation().clone().add(0,1,0), EntityType.ARMOR_STAND);
                    spawn.setSmall(true);
                    spawn.setInvulnerable(true);
                    spawn.setVisible(false);

                    double Zforce = Math.random() * 2;
                    double Xforce = Math.random() * 2;


                    int direction = (int) (Math.random() * 5);

                    Vector vector = spawn.getLocation().getDirection();

                    switch (direction){
                        case 1:
                            vector.setX(Xforce);
                            vector.setZ(Zforce);
                            break;
                        case 2:
                            vector.setX(-Xforce);
                            vector.setZ(Zforce);
                            break;
                        case 3:
                            vector.setX(Xforce);
                            vector.setZ(-Zforce);
                            break;
                        case 4:
                            vector.setX(-Xforce);
                            vector.setZ(-Zforce);
                    }
                    spawn.setVelocity(vector);

                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            spawnSquid(spawn.getLocation().clone().add(0,-1,0));
                            spawn.remove();
                        }
                    }.runTaskLater(plugin, 60);
                }
            }
        }.runTaskTimer(plugin, 0, 100);
    }

    public void spawnSquid(Location loc){
        if (plugin.venSpawned){
            Squid shield = (Squid) loc.getWorld().spawnEntity(loc, EntityType.SQUID);

            shield.setCustomName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD +  "Squid Shield");
            shield.setGlowing(true);
            shield.setCustomNameVisible(true);
            shield.setMetadata("SquidShield", new FixedMetadataValue(plugin, "squidshield"));
            plugin.squids--;

            new BukkitRunnable(){
                double move = 0.1;
                int count = 0;

                @Override
                public void run() {
                    if (!plugin.venSpawned || vendrick.getVendrick().isDead()) {
                        shield.remove();
                        cancel();
                    }
                    if (count == 15){
                        cancel();
                    }
                    loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_SPLASH, 1.0f, 0.5f);
                    shield.teleport(loc.add(0,move,0));
                    count++;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
}
