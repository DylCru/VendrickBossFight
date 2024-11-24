package me.crazyrain.vendrickbossfight.attacks;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.mobs.Wraith;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class PortalWraiths implements Listener {

        VendrickBossFight plugin;
        static Vendrick vendrick;
        List<UUID> players;

        public static Integer portalHealth = 5;
        public static Boolean showParticles = true;
        public static Boolean spawnMobs = true;

        public static ArmorStand portal;

        public static Integer rot = 0;

        public PortalWraiths(VendrickBossFight plugin){
            this.plugin = plugin;
        }

        public void init(Vendrick vendrick, List<UUID> players, boolean attack){
            PortalWraiths.vendrick = vendrick;
            this.players = players;
            if (attack){
                spawnPortals(PortalWraiths.vendrick.getVendrick().getLocation(), this.players);
            }
        }

        public void stopAttack(){
            vendrick.setPhase(0);
            vendrick.stopAttack();
            if (plugin.runeHandler != null) {
                plugin.runeHandler.setPaused(false);
            }
        }

        public void stopParticles(){
            showParticles = false;
        }

        public void spawnPortals(Location loc, List<UUID> players){
            new BukkitRunnable(){

                @Override
                public void run() {
                    portalHealth = 5;
                    showParticles = true;
                    spawnMobs = true;
                    rot = 0;

                    loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_FLAP, 5f, 1.5f);

                    portal = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    portal.getEquipment().setHelmet(new ItemStack(Material.BLACK_CONCRETE));
                    portal.setVisible(false);
                    portal.setMetadata("Portal", new FixedMetadataValue(plugin, "portal"));
                    portal.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Portal health: " + ChatColor.DARK_RED + ChatColor.BOLD + portalHealth);
                    portal.setCustomNameVisible(true);
                    portal.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);

                    for (UUID id : players){
                        Bukkit.getPlayer(id).sendMessage(ChatColor.RED + "Vendrick opened a portal to the " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "ETERNAL WRAITHS! " + ChatColor.RED + "Quick! Destroy the portal!");
                    }

                    double Zforce = Math.random() * 2;
                    double Xforce = Math.random() * 2;


                    Integer direction = (int) (Math.random() * 5);

                    Vector vector = portal.getLocation().getDirection();

                    switch (direction){
                        case 1:
                            vector.setX(Xforce);
                            vector.setZ(Zforce);
                            break;
                        case 2:
                            vector.setX(-Xforce);
                            vector.setZ(Zforce);
                            break;
                        case 3:
                            vector.setX(Xforce);
                            vector.setZ(-Zforce);
                            break;
                        case 4:
                            vector.setX(-Xforce);
                            vector.setZ(-Zforce);
                    }
                    vector.setY(0.2);
                    portal.setVelocity(vector);
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            startParticles(portal.getLocation(), 1);
                            rotatePortal(portal);
                            spawnMobs(portal.getLocation());
                        }
                    }.runTaskLater(plugin, 20);
                }
            }.runTaskLater(plugin, 20 * 3);
        }

        public void startParticles(Location loc, Integer size){
            new BukkitRunnable(){

                @Override
                public void run() {
                    if (showParticles){
                        for (int d = 0; d <= 90; d += 1) {
                            Location particleLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                            particleLoc.setX(loc.getX() + Math.cos(d) * size);
                            particleLoc.setZ(loc.getZ() + Math.sin(d) * size);
                            particleLoc.add(0, (d / 36), 0);
                            loc.getWorld().spawnParticle(Particle.REDSTONE,  particleLoc, 1, new Particle.DustOptions(Color.PURPLE, 1));
                        }
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 5);
        }

        public void rotatePortal(Entity e){
            new BukkitRunnable(){
                @Override
                public void run() {
                    Location portalLoc = e.getLocation();
                    rot += 10;
                    portalLoc.setYaw(rot);

                    e.teleport(portalLoc);
                }
            }.runTaskTimer(plugin, 0, 1);
        }

        public void spawnMobs(Location location){
            vendrick.setSkipable(true);

            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!plugin.venSpawned){
                        clearAll();
                        cancel();
                    }

                    if (spawnMobs){
                        for (int i = 0; i <  4; i++){
                            switch (i){
                                case 0:
                                    Wraith wraith = new Wraith(location.clone().add(1,0,1), plugin);
                                    break;
                                case 1:
                                    Wraith wraith2 = new Wraith(location.clone().add(-1,0,1), plugin);
                                    break;
                                case 2:
                                    Wraith wraith3 = new Wraith(location.clone().add(1,0,-1), plugin);
                                    break;
                                case 3:
                                    Wraith wraith4 = new Wraith(location.clone().add(-1,0,-1), plugin);
                                    break;
                            }

                        }
                        location.getWorld().strikeLightningEffect(location);
                        location.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1.2f);
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20 * 3);
        }

        @EventHandler
        public void stopDrops(EntityDeathEvent e){
            if (e.getEntity().hasMetadata("Wraith")){
                e.getDrops().clear();
            }
        }

        @EventHandler
        public void damagePortal(EntityDamageByEntityEvent e){
            if (e.getDamager() instanceof Player){
                if (e.getEntity().hasMetadata("Portal")){
                    e.setDamage(0);

                    if (!plugin.fighting.contains(e.getDamager().getUniqueId()) && !e.getDamager().isOp()){
                        e.getDamager().sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "The portal reacts violently to your attack");
                        e.getDamager().sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Your soul is too pure");
                        launchPlayer((Player) e.getDamager(), e.getEntity().getLocation().clone().add(0,-1,0), 100);
                        return;
                    }

                    portalHealth -= 1;
                    switch (portalHealth){
                        case 4:
                            e.getEntity().setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Portal health: " + ChatColor.RED + ChatColor.BOLD + portalHealth);
                            launchPlayer((Player) e.getDamager(), e.getEntity().getLocation().clone().add(0,-1,0), 2 * 10);
                            break;
                        case 3:
                            e.getEntity().setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Portal health: " + ChatColor.GOLD + ChatColor.BOLD + portalHealth);
                            launchPlayer((Player) e.getDamager(), e.getEntity().getLocation().clone().add(0,-1,0), 4 * 10);
                            break;
                        case 2:
                            e.getEntity().setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Portal health: " + ChatColor.DARK_GREEN + ChatColor.BOLD + portalHealth);
                            launchPlayer((Player) e.getDamager(), e.getEntity().getLocation().clone().add(0,-1,0), 6 * 10);
                            break;
                        case 1:
                            e.getEntity().setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Portal health: " + ChatColor.GREEN + ChatColor.BOLD + portalHealth);
                            launchPlayer((Player) e.getDamager(), e.getEntity().getLocation().clone().add(0,-1,0), 8 * 10);
                            break;
                        case 0:
                            e.getEntity().remove();
                            e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, e.getEntity().getLocation(), 2);
                            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 2.0f, 0.9f);
                            launchPlayer((Player) e.getDamager(), e.getEntity().getLocation(), 10 * 10);

                            for (UUID player : plugin.fighting){
                                Bukkit.getPlayer(player).sendMessage(ChatColor.GREEN + "The portal was destroyed!");
                                Bukkit.getPlayer(player).playSound(e.getDamager().getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
                            }

                            showParticles = false;
                            spawnMobs = false;

                            ItemStack potion = new ItemStack(Material.SPLASH_POTION);
                            PotionMeta pMeta = (PotionMeta) potion.getItemMeta();
                            assert pMeta != null;
                            pMeta.addCustomEffect(PotionEffectType.HEAL.createEffect(1,1),false);
                            potion.setItemMeta(pMeta);
                            ThrownPotion tp = (ThrownPotion) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SPLASH_POTION);
                            tp.setItem(potion);

                            stopAttack();
                    }
                }
            }
        }

        public void launchPlayer(Player p, Location loc, Integer force) {
            if (p.getGameMode().equals(GameMode.CREATIVE)){
                return;
            }
            p.setVelocity((p.getLocation().toVector().subtract(loc.toVector())).multiply(force).normalize());
        }

        public void stopSpawning(){
            spawnMobs = false;
        }

        public void skipAttack(){
            stopSpawning();
            stopParticles();
            stopAttack();
            for (UUID player : plugin.fighting){
                Bukkit.getPlayer(player).sendMessage(ChatColor.GREEN + "The portal was destroyed!");
                Bukkit.getPlayer(player).playSound(Bukkit.getPlayer(player).getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            }

            ItemStack potion = new ItemStack(Material.SPLASH_POTION);
            PotionMeta pMeta = (PotionMeta) potion.getItemMeta();
            assert pMeta != null;
            pMeta.addCustomEffect(PotionEffectType.HEAL.createEffect(1,1),false);
            potion.setItemMeta(pMeta);
            ThrownPotion tp = (ThrownPotion) portal.getWorld().spawnEntity(portal.getLocation(), EntityType.SPLASH_POTION);
            tp.setItem(potion);

            portal.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, portal.getLocation(), 2);
            portal.getWorld().playSound(portal.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 2.0f, 0.9f);
            portal.remove();

        }

        public void clearAll(){
            portal.remove();
            showParticles = false;
        }

    }
