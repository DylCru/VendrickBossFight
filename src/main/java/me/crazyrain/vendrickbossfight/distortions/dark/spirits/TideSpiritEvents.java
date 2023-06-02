package me.crazyrain.vendrickbossfight.distortions.dark.spirits;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSpiritSpawnEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TideSpiritEvents implements Listener {

    VendrickBossFight plugin;
    LivingEntity tideSpirit;

    public TideSpiritEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    private boolean isCritical(Player player) // Credit to https://www.spigotmc.org/threads/how-to-catch-critical-hits.221616/#post-2269179
    {
        return
                player.getFallDistance() > 0.0F &&
                        !player.isInsideVehicle() &&
                        !player.hasPotionEffect(PotionEffectType.BLINDNESS) &&
                        player.getLocation().getBlock().getType() != Material.LADDER &&
                        player.getLocation().getBlock().getType() != Material.VINE;
    }

    @EventHandler
    public void onTideSpiritSpawn(VendrickSpiritSpawnEvent e){
        if (e.getSpiritType().equalsIgnoreCase("tide")){
            plugin.countdown = new TsunamiCountdown(plugin, e.getSpirit());
            plugin.countdown.startCountdown();

            new BukkitRunnable(){
                int count = 0;
                @Override
                public void run() {
                    if (count == 3){
                        cancel();
                    }
                    for (UUID p : plugin.fighting){
                        Player player = Bukkit.getPlayer(p);
                        player.sendTitle(Lang.TSUNAMITITLE.toString(), Lang.TSUNAMISUBTITLE.toString(), 0, 10, 0);
                    }
                    count++;
                }
            }.runTaskTimer(plugin, 0, 20);
            for (UUID p : plugin.fighting){
                Bukkit.getPlayer(p).sendMessage(Lang.TSUNAMICHAT.toString());
            }
            new BukkitRunnable(){
                int count = 0;
                @Override
                public void run() {
                    if (count == 70){
                        cancel();
                    }
                    for (UUID p : plugin.fighting){
                        Bukkit.getPlayer(p).playSound(Bukkit.getPlayer(p).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 2.0f);
                    }
                    count++;
                }
            }.runTaskTimer(plugin,0, 1);
        }
    }

    @EventHandler
    public void onTideSpiritDeath(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("ven_spirit_tide")){
            try {
                plugin.countdown.removeBars();
                plugin.countdown.setActive(false);
            } catch (Exception ignored) {}
        }
    }

    @EventHandler
    public void onPlayerCritTideSpirit(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)) {return;}
        if (!(e.getEntity().hasMetadata("ven_spirit_tide"))) {return;}

        Player player = (Player) e.getDamager();

        if (isCritical(player)){
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1.0f, 1.5f);
            player.sendMessage(Lang.TIDENOCRIT.toString());
        }
    }

    @EventHandler
    public void onPlayerHitTideSpirit(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)) {return;}
        if (!(e.getEntity().hasMetadata("ven_spirit_tide"))) {return;}

        Entity spirit = e.getEntity();

        int spawnChance = (int) (Math.random() * 8);
        if (spawnChance == 7){
            double mobType = Math.random();

            List<Location> spawnLocs = new ArrayList<>();
            spawnLocs.add(spirit.getLocation().clone().add(5,40,0));
            spawnLocs.add(spirit.getLocation().clone().add(0,45,5));
            spawnLocs.add(spirit.getLocation().clone().add(-5,50,0));
            spawnLocs.add(spirit.getLocation().clone().add(0,55,-5));

            if (mobType >= 0.66){
                for (Location loc : spawnLocs){
                    loc.getWorld().spawnEntity(loc, EntityType.GUARDIAN);
                }
                spirit.getWorld().playSound(spirit.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1.0f, 1.0f);
            } else if (mobType >= 0.33) {
                Location spawnLoc = spirit.getLocation().clone().add(0,40,0);
                spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ELDER_GUARDIAN);
                spirit.getWorld().playSound(spirit.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 0.5f);
            } else {
                for (Location loc : spawnLocs){
                    Entity drowned = loc.getWorld().spawnEntity(loc, EntityType.DROWNED);
                    ((LivingEntity) drowned).getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
                    ((LivingEntity) drowned).getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                }
            }
        }
    }

    @EventHandler
    public void onGuardianFall(EntityDamageEvent e){
        if (plugin.venSpawned){
            if (e.getEntity().getType().equals(EntityType.GUARDIAN) ||
                    e.getEntity().getType().equals(EntityType.ELDER_GUARDIAN) ||
                    e.getEntity().getType().equals(EntityType.DROWNED)){
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSpiritOrGuardianDeath(EntityDeathEvent e){
        if (plugin.venSpawned){
            if (e.getEntity().hasMetadata("ven_spirit_tide")){
                e.getDrops().clear();
                for (Entity g : e.getEntity().getNearbyEntities(50,50,50)){
                    if (g.getType().equals(EntityType.GUARDIAN) || g.getType().equals(EntityType.ELDER_GUARDIAN) || g.getType().equals(EntityType.DROWNED)){
                        ((LivingEntity) g).damage(100);
                        g.getWorld().strikeLightningEffect(g.getLocation());
                    }
                }
            }
            if (e.getEntity().getType().equals(EntityType.GUARDIAN) || e.getEntity().getType().equals(EntityType.ELDER_GUARDIAN) || e.getEntity().getType().equals(EntityType.DROWNED)){
                e.getDrops().clear();
            }
        }
    }

}
