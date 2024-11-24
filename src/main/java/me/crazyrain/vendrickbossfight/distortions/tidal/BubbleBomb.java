package me.crazyrain.vendrickbossfight.distortions.tidal;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class BubbleBomb {

    Location venLoc;
    List<UUID> safe;

    public BubbleBomb(Location venLoc) {
        this.venLoc = venLoc;
    }

    public void startAttack() {
        spawnBubble();
    }

    private void spawnBubble() {
        for (int i = 0; i < 100; i++) {
            Vector vector = new Vector(
                    ((Math.random() * (1.5 - -1.5)) + -1.5),
                    0.75,
                    (Math.random() * (1.5 - -1.5)) + -1.5);
            ArmorStand spawnStand = (ArmorStand) venLoc.getWorld().spawnEntity(venLoc, EntityType.ARMOR_STAND);
            spawnStand.setVisible(true);
            spawnStand.setSmall(true);
            spawnStand.setVelocity(vector);
            spawnStand.setGlowing(true);
        }
    }

    //TODO: Add bubble bomb functionality and replace tsunami with it
}
