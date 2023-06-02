package me.crazyrain.vendrickbossfight.distortions.flaming;


import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Inferno {

    Entity entity;
    List<UUID> fighting;

    public Inferno(Entity entity, List<UUID> fighting){
        this.entity = entity;
        this.fighting = fighting;
    }

    public void blast(){
        for (Entity e : entity.getNearbyEntities(4,4,4)){
            if (fighting.contains(e.getUniqueId())){
                Player player = (Player) e;
                player.damage(8, entity);
                player.setFireTicks(80);
                player.setVelocity((player.getLocation().toVector().subtract(entity.getLocation().add(0,-1,0).toVector())).multiply(40).normalize());
            }
        }
    }
}
