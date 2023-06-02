package me.crazyrain.vendrickbossfight.attacks;


import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Shatter {

    VendrickBossFight plugin;

    public Shatter(VendrickBossFight plugin) {
        this.plugin = plugin;
    }

    public void startShatter(Player player) {
        Location loc = player.getLocation();

        makeCircle(checkForAir(loc.getX(), loc.getY() + 1, loc.getZ(), loc.getWorld()).add(0,1,0), 2f, Color.RED);
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 10, 1);

        new BukkitRunnable(){

            @Override
            public void run() {
                makeCircle(checkForAir(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld()).add(0,1,0), 2f, Color.BLACK);
                damagePlayer(loc);
            }
        }.runTaskLater(plugin, 20 * 2);
    }




    public void makeCircle(Location loc, Float radius, Color color){
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
                    particleLoc.add(0, (d / 90.0), 0);
                    loc.getWorld().spawnParticle(Particle.REDSTONE,  particleLoc, 1, new Particle.DustOptions(color, 1));
                }
                t += 3;
            }
        }.runTaskTimer(plugin, 0, 3);
    }

    public void damagePlayer(Location loc){
        ArmorStand radiusCheck = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        radiusCheck.setInvulnerable(true);
        radiusCheck.setVisible(false);

        for (Entity p : radiusCheck.getNearbyEntities(1.5, 3, 1.5)){
            if (p instanceof LivingEntity){
                if (p instanceof Player){
                    if (!((Player) p).getGameMode().equals(GameMode.CREATIVE)){
                           ((Player) p).damage(10);
                           Vector pv = p.getLocation().getDirection();
                           pv.setY(1);
                           p.setVelocity(pv);
                    }
                }
            }
        }

        loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 10f, 1);
        loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BREAK, 10f, 1);
        loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_BREAK_BLOCK, 10f, 1);

        radiusCheck.remove();
    }



    public void restoreBlocks(Location loc, World world, Material mat){
        new BukkitRunnable(){

            @Override
            public void run() {
                world.getBlockAt(loc).setType(mat);
            }
        }.runTaskLater(plugin, 20 * 4);
    }

    public static Location checkForAir(Double x, Double y, Double z, World world){
        Location loc = new Location(world, x,y,z);
        int offset = 0;
        boolean notAir = false;
        while (!(notAir)){
            loc = new Location(world, x, y - offset, z);
            if (loc.getBlock().getType() != Material.AIR){
                notAir = true;
            } else {
                offset += 1;
            }
        }
        return loc;
    }
}
