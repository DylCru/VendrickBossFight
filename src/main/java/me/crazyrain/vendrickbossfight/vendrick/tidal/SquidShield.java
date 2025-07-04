package me.crazyrain.vendrickbossfight.vendrick.tidal;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.vendrick.Vendrick;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class SquidShield {
    Vendrick vendrick;
    List<UUID> fighting;
    VendrickBossFight plugin = VendrickBossFight.getPlugin(VendrickBossFight.class);
    Squid entity;

    boolean spawn = true;

    public SquidShield(Vendrick vendrick, List<UUID> fighting){
        this.vendrick = vendrick;
        this.fighting = fighting;
        throwSpawn();
    }

    public void stopSpawn(){
        this.spawn = false;
    }

    public Squid getEntity() {
        return entity;
    }

    public void throwSpawn(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (vendrick.getEntity().isDead() || !plugin.getFightManager().isVenSpawned()){
                    cancel();
                }

                if (plugin.getFightManager().getSquids() > 1 && spawn){
                    ArmorStand spawn = (ArmorStand) vendrick.getEntity().getWorld().spawnEntity(vendrick.getEntity().getLocation().clone().add(0,1,0), EntityType.ARMOR_STAND);
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
        if (plugin.getFightManager().isVenSpawned()){
            entity = (Squid) loc.getWorld().spawnEntity(loc, EntityType.SQUID);

            entity.setCustomName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD +  "Squid Shield");
            entity.setGlowing(true);
            entity.setCustomNameVisible(true);
            entity.setMetadata("SquidShield", new FixedMetadataValue(plugin, "squidshield"));
            ((TidalVendrick) plugin.getFightManager().getVendrick()).getSheilds().add(entity);
            plugin.getFightManager().setSquids(plugin.getFightManager().getSquids() - 1);

            new BukkitRunnable(){
                double move = 0.1;
                int count = 0;

                @Override
                public void run() {
                    if (!plugin.getFightManager().isVenSpawned() || vendrick.getEntity().isDead()) {
                        entity.remove();
                        cancel();
                    }
                    if (count == 15){
                        cancel();
                    }
                    loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_SPLASH, 1.0f, 0.5f);
                    entity.teleport(loc.add(0,move,0));
                    count++;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
}
