package me.crazyrain.vendrickbossfight.distortions.stormy;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.FloatingCollectable;
import me.crazyrain.vendrickbossfight.functionality.ItemManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LightningStorm {
    
    VendrickBossFight plugin;
    
    public LightningStorm(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    static int spawnLimit = 5;
    int spawned = 0;

    public void movement(){
        Location loc = plugin.vendrick.getVendrick().getLocation().clone().add(0,-3.5,0);
        loc.setPitch(0);

        plugin.vendrick.getVendrick().teleport(loc);
        plugin.vendrick.getVendrick().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, plugin.vendrick.getVendrick().getLocation(), 2);

        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                if (count == 40){
                    cancel();
                }

                plugin.vendrick.getVendrick().getWorld().playSound(plugin.vendrick.getVendrick().getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
                shootBalls(plugin.vendrick.getVendrick().getLocation().getDirection().normalize(), plugin.vendrick.getVendrick().getLocation().add(0,0.5,0));
                count++;
            }


        }.runTaskTimer(plugin, 0, 2);


        new BukkitRunnable(){
            int count = 0;
            float rot = 0;
            @Override
            public void run() {
                if (count == 40){ //This is done separate as not to break attack skipping
                    plugin.vendrick.getVendrick().setAI(true);
                    plugin.vendrick.getVendrick().setInvulnerable(false);
                    plugin.vendrick.getVendrick().setGlowing(false);
                    plugin.vendrick.setPhase(0);
                    cancel();
                }

                Location loc = plugin.vendrick.getVendrick().getLocation();
                loc.setYaw(rot);
                plugin.vendrick.getVendrick().teleport(loc);
                rot += 10;
                count++;
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    public void shootBalls(Vector vector, Location location){
        ArmorStand ball = (ArmorStand) location.getWorld().spawnEntity(location.add(vector), EntityType.ARMOR_STAND);
        ball.getEquipment().setHelmet(new ItemStack(Material.BLUE_ICE));
        ball.setVisible(false);
        ball.setCustomName(ChatColor.DARK_BLUE + "Ball Lightning");
        ball.setSmall(true);
        ball.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);

        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                if (count == 10){
                    int spawnChance = (int) (Math.random() * 50);
                    if (spawnChance >= 48){
                        FloatingCollectable collectable = new FloatingCollectable(ItemManager.ballLightning.getItemMeta().getDisplayName()
                                , ItemManager.ballLightning
                                , ball.getLocation()
                                , true,
                                8,
                                Particle.NAUTILUS);
                        collectable.spawnStand();
                    }
                }

                if (count == 20){
                    ball.remove();
                    cancel();
                }

                ball.setVelocity(vector);

                for (Entity e : ball.getNearbyEntities(0.2,0.2,0.2)){
                    if (e instanceof ArmorStand || !(e instanceof Player)){
                        continue;
                    }
                    
                    LivingEntity player = (LivingEntity) e;
                    if (plugin.fighting.contains(player.getUniqueId())){
                        player.damage(20, ball);
                        player.getWorld().strikeLightningEffect(player.getLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 2.0f);
                        player.sendMessage(ChatColor.DARK_GRAY + "The Ball Lightning struck you for 20 damage!");
                        ball.remove();
                        cancel();
                    }
                }

                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
