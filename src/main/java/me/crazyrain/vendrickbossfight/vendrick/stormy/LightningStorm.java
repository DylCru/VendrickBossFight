package me.crazyrain.vendrickbossfight.vendrick.stormy;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.FloatingCollectable;
import me.crazyrain.vendrickbossfight.items.ItemManager;
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
        Location loc = plugin.getFightManager().getVendrick().getEntity().getLocation().clone().add(0,-3.5,0);
        loc.setPitch(0);

        plugin.getFightManager().getVendrick().getEntity().teleport(loc);
        plugin.getFightManager().getVendrick().getEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, plugin.getFightManager().getVendrick().getEntity().getLocation(), 2);

        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                if (count == 40){
                    cancel();
                }

                plugin.getFightManager().getVendrick().getEntity().getWorld().playSound(plugin.getFightManager().getVendrick().getEntity().getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
                shootBalls(plugin.getFightManager().getVendrick().getEntity().getLocation().getDirection().normalize(), plugin.getFightManager().getVendrick().getEntity().getLocation().add(0,0.5,0));
                count++;
            }


        }.runTaskTimer(plugin, 0, 2);


        new BukkitRunnable(){
            int count = 0;
            float rot = 0;
            @Override
            public void run() {
                if (count == 40){ //This is done separate as not to break attack skipping
                    plugin.getFightManager().getVendrick().getEntity().setAI(true);
                    plugin.getFightManager().getVendrick().getEntity().setInvulnerable(false);
                    plugin.getFightManager().getVendrick().getEntity().setGlowing(false);
                    plugin.getFightManager().getVendrick().setPhase(0);
                    cancel();
                }

                Location loc = plugin.getFightManager().getVendrick().getEntity().getLocation();
                loc.setYaw(rot);
                plugin.getFightManager().getVendrick().getEntity().teleport(loc);
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
                    if (plugin.getFightManager().getFighting().contains(player.getUniqueId())){
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
