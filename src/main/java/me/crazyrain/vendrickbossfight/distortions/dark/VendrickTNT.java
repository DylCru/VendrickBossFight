package me.crazyrain.vendrickbossfight.distortions.dark;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class VendrickTNT implements Listener{

    public static void launchTNT(LivingEntity entity){
        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                if (count == 0) { cancel();}
                Location loc = entity.getLocation();
                TNTPrimed venTNT = (TNTPrimed) loc.getWorld().spawnEntity(loc.clone().add(0,2,0), EntityType.PRIMED_TNT);

                float mult = (float) (Math.random() / 3);
                Vector tntVel = entity.getEyeLocation().getDirection().normalize().multiply(mult);

                Location landLoc = loc.clone();
                landLoc.add(entity.getLocation().getDirection().multiply(mult * 20));
                makeCircle(landLoc.add(0,0.2,0), 4f, Color.RED);


                tntVel.setY(0.5);
                venTNT.setVelocity(tntVel);
                venTNT.setSource(entity);
                venTNT.setFuseTicks(27);
                venTNT.setYield(3);
                venTNT.setMetadata("VendrickTNT", new FixedMetadataValue(VendrickBossFight.plugin, "vendricktnt"));
                loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
                count++;
            }
        }.runTaskTimer(VendrickBossFight.plugin, 0, 20);
    }

    public static void makeCircle(Location loc, Float radius, Color color){
        new BukkitRunnable(){
            Integer t = 0;
            @Override
            public void run() {
                if (t >= 30){
                    cancel();
                }


                for (int d = 0; d <= 90; d += 1) {
                    Location particleLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                    particleLoc.setX(loc.getX() + Math.cos(d) * radius);
                    particleLoc.setZ(loc.getZ() + Math.sin(d) * radius);
                    particleLoc.add(0, d / 9000.0, 0);
                    loc.getWorld().spawnParticle(Particle.REDSTONE,  particleLoc, 1, new Particle.DustOptions(color, 1));
                }
                t += 3;
            }
        }.runTaskTimer(VendrickBossFight.plugin, 0, 3);
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e){
        if (!(e.getEntity() instanceof TNTPrimed)){
            return;
        }

        Entity tnt = e.getEntity();
        if (tnt.hasMetadata("VendrickTNT")){
            e.setCancelled(true);
            e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.7f);
            e.getLocation().getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().clone().add(0, 0.2,0), 40, new Particle.DustOptions(Color.BLACK, 1));
            e.getLocation().getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation().clone().add(0, 0.2,0), 40);

        }
    }

    @EventHandler
    public void onTNTdamage(EntityDamageByEntityEvent e){
        if (e.getDamager().hasMetadata("VendrickTNT")){
            if (!(e.getDamager() instanceof TNTPrimed)){
                return;
            }
            if (!(e.getEntity() instanceof Player)){
                e.setCancelled(true);
                return;
            }

            Player player = (Player) e.getEntity();
            double health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double damage = health * 0.25;
            e.setDamage(8);
            try {
                player.setHealth((player.getHealth() - damage));
            } catch (Exception ignored){}
            e.getEntity().setVelocity(new Vector(0,-2,0));
        }
    }
}
