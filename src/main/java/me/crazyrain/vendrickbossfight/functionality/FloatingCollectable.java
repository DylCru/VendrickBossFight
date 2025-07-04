package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class FloatingCollectable {

    VendrickBossFight plugin = VendrickBossFight.getPlugin(VendrickBossFight.class);

    String name;
    ItemStack item;
    Location location;
    Particle particle;
    ArmorStand stand;
    ArmorStand nameStand;
    boolean collected;
    boolean fightOnly;
    int time;

    public FloatingCollectable(String name, ItemStack item, Location location, boolean fightOnly, int time, Particle particle){
        this.name = name;
        this.item = item;
        this.location = location;
        this.particle = particle;
        this.collected = false;
        this.fightOnly = fightOnly;
        this.time = time;
    }

    public void spawnStand(){
        stand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0,-1,0), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.getEquipment().setHelmet(item);
        stand.setMetadata("venCollect", new FixedMetadataValue(plugin,"venCollect"));
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);

        nameStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0,-0.75,0), EntityType.ARMOR_STAND);
        nameStand.setCustomNameVisible(true);
        nameStand.setCustomName(name);
        nameStand.setVisible(false);
        nameStand.setGravity(false);
        nameStand.setMetadata("venCollect", new FixedMetadataValue(plugin,"venCollect"));

        move();
        checkForPlayer();
        countDown();
    }

    private void countDown(){
        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                count++;
                if (count == time){
                    collected = true;
                    stand.remove();
                    nameStand.remove();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void move(){
        new BukkitRunnable(){
            float rot = -90f;
            double move = 0.0;
            boolean up = true;
            @Override
            public void run() {
                if (collected){
                    cancel();
                }

                if (up){
                    move += 0.003;
                    if (move > 0.05){
                        up = false;
                        move = -0.003;
                    }
                } else {
                    move -= 0.003;
                    if (move < -0.05){
                        up = true;
                        move = 0.003;
                    }
                }
                Location standLoc = stand.getLocation().clone().add(0,move,0);
                standLoc.setYaw(rot);
                rot += 3f;

                standLoc.getWorld().spawnParticle(particle, standLoc.clone().add(0,1.5, 0), 5);
                stand.teleport(standLoc);
                //nameStand.teleport(standLoc.clone().add(0,1,0));
            }
        }.runTaskTimer(plugin,0, 3);
    }

    private void checkForPlayer(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (collected){
                    cancel();
                }

                for (Entity e : stand.getNearbyEntities(0.1,0.1,0.1)){
                    if (e instanceof Player){
                        Player player = (Player) e;
                        if (fightOnly){
                            if (!plugin.getFightManager().getFighting().contains(player.getUniqueId())){
                                continue;
                            }
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
                        if (!(player.getInventory().firstEmpty() == -1)){
                            player.getInventory().addItem(item);
                            stand.remove();
                            nameStand.remove();
                            collected = true;
                            cancel();
                        } else {
                            player.sendMessage(ChatColor.RED + "Your inventory is full!");
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
