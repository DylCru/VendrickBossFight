package me.crazyrain.vendrickbossfight.mobs;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Wraith {

    Location loc;
    VendrickBossFight plugin;
    ItemStack wraithChestPlate;
    LivingEntity skele;

    public Wraith(Location loc, VendrickBossFight plugin){
        this.loc = loc;
        this.plugin = plugin;
        this.wraithChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        spawnMob();
        wraithAnimations();
    }

    public void spawnMob(){
        skele = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
        skele.setHealth(0.5);
        skele.setCustomName(ChatColor.GRAY + "" + ChatColor.BOLD + "Eternal Wraith");
        skele.setCustomNameVisible(true);
        skele.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(10000,1));
        skele.setMetadata("Wraith", new FixedMetadataValue(plugin, "wraith"));
        skele.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(10000, 1));
        skele.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        skele.getEquipment().setItemInOffHand(new ItemStack(Material.WITHER_ROSE));

        LeatherArmorMeta leatherMeta = (LeatherArmorMeta) wraithChestPlate.getItemMeta();
        leatherMeta.setColor(Color.BLACK);
        wraithChestPlate.setItemMeta(leatherMeta);

        skele.getEquipment().setChestplate(wraithChestPlate);
    }

    public void wraithAnimations(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (skele.isDead()){
                    cancel();
                }

                //TODO: Maybe add the chestplate animation back in
//            int r = 0;
//            int g = 0;
//            int b = 0;
//            boolean up = true;
//
//            int atkCooldown = 35;
//            boolean attacked = false;

//                LeatherArmorMeta meta = (LeatherArmorMeta) wraithChestPlate.getItemMeta();
//                meta.setColor(Color.fromRGB(r,g,b));
//                wraithChestPlate.setItemMeta(meta);
//                skele.getEquipment().getChestplate().setItemMeta(wraithChestPlate.getItemMeta());
//
//                if (up){
//                    r += 10; g += 10; b += 10;
//                } else {
//                    r -= 10; g -= 10; b -= 10;
//                }
//
//                if (r >= 250 && up){
//                    up = false;
//                } else if (r <= 0 && !up){
//                    up = true;
//                }
//
//                if (!attacked){
//                    for (Entity e : skele.getNearbyEntities(1,1,1)){
//                        if (e instanceof Player){
//                            Player p = (Player) e;
//                            p.damage(10, skele);
//                            attacked = true;
//                        }
//                    }
//                }
//
//                if (attacked){
//                    atkCooldown -= 1;
//                    if (atkCooldown == 0){
//                        attacked = false;
//                        atkCooldown = 35;
//                    }
//                }
//

                skele.getLocation().getWorld().spawnParticle(Particle.SPELL_WITCH, skele.getLocation().add(0, 0.5, 0), 2);
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
