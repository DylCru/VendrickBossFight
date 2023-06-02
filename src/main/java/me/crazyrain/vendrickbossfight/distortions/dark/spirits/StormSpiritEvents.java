package me.crazyrain.vendrickbossfight.distortions.dark.spirits;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSpiritSpawnEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StormSpiritEvents implements Listener {

    HashMap<UUID, Integer> charges = new HashMap<>();
    VendrickBossFight plugin;
    Boolean active = true;

    public StormSpiritEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onStormSpiritSpawn(VendrickSpiritSpawnEvent e){
        if (e.getSpiritType().equals("storm")){
            charges.clear();
            lightningStrike(e.getSpirit());
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!active) {
                        cancel();
                    }
                    ballTimer(e.getSpirit().getLocation().getDirection(), e.getSpirit());
                }
            }.runTaskTimer(plugin, 0, 100);
        }
    }

    @EventHandler
    public void onStormSpiritDeath(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("ven_spirit_storm")){
            active = false;
        }
    }

    private void lightningStrike(Entity spirit){
        new BukkitRunnable(){
            List<Location> pLocs = new ArrayList<>();
            @Override
            public void run() {
                if (!active){
                    cancel();
                }

                for (UUID p : plugin.fighting){
                    pLocs.add(Bukkit.getPlayer(p).getLocation());
                    Bukkit.getPlayer(p).getWorld().spawnParticle(Particle.REDSTONE, Bukkit.getPlayer(p).getLocation().add(0,1,0), 3, new Particle.DustOptions(Color.YELLOW, 5));
                }

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        for (Location loc : pLocs){
                            loc.getWorld().strikeLightningEffect(loc);
                            for (Entity e : loc.getWorld().getNearbyEntities(loc, 0.5,0.5,0.5)){
                                if (e instanceof Player){
                                    ((Player) e).damage(35, spirit);
                                    ((Player) e).playSound(e.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.5f);
                                }
                            }
                        }
                        pLocs.clear();
                    }
                }.runTaskLater(plugin, 15);
            }
        }.runTaskTimer(plugin, 0, 40);
    }

    @EventHandler
    public void onSpiritHitPlayer(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)) {return;}
        if (!(e.getDamager().hasMetadata("ven_spirit_storm"))) {return;}

        Player player = (Player) e.getEntity();
        UUID id = player.getUniqueId();

        if (charges.keySet().contains(id)){
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 1.8f);
            charges.replace(id, charges.get(id) + 1);
            if (charges.get(id) == 5){
                paralyzePlayer(player);
                charges.remove(id);
            }
        } else {
            charges.put(id, 1);
            player.sendMessage(Lang.STORMINFLICT.toString());
        }
    }

    public void paralyzePlayer(Player player){
        player.addPotionEffect(PotionEffectType.SLOW.createEffect(60, 10));
        player.damage(1);
        player.setHealth(player.getHealth() / 3);
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.5f, 2.0f);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.8f);
        player.sendMessage(Lang.STORMPARALYZE.toString());
    }

    public void ballTimer(Vector vector, Entity spirit){
       new BukkitRunnable(){
           int count = 0;
           @Override
           public void run() {
               if (count == 3 && active){
                   cancel();
               }

               shootBall(vector, spirit.getLocation().add(0, 0.5,0));

               count++;
           }
       }.runTaskTimer(plugin, 0, 15);
    }

    public void shootBall(Vector vector, Location location){
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
                        player.damage(40, ball);
                        player.getWorld().strikeLightningEffect(player.getLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 2.0f);
                        player.sendMessage(Lang.STORMBALLHIT.toString());
                        ball.remove();
                        cancel();
                    }
                }

                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
