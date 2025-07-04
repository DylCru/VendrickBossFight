package me.crazyrain.vendrickbossfight.vendrick.dark.spirits;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSkipSpiritEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.vendrick.dark.DarkVendrick;
import me.crazyrain.vendrickbossfight.vendrick.flaming.Inferno;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlameSpiritEvents implements Listener {
    ArmorStand flameBomb;
    boolean flameBombActive = false;
    Boolean flameBombDestroyed = false;
    int flameBombHealth = 3;
    boolean infernoing = false;
    Inferno inferno;
    VendrickBossFight plugin;

    public FlameSpiritEvents (VendrickBossFight plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerHitFlameSpirit(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)) { return;}
        if (!e.getEntity().hasMetadata("ven_spirit_flame")){ return;}

        Player player = (Player) e.getDamager();
        Entity spirit = e.getEntity();

        int bombChance = (int) (Math.random() * 11);
        if (bombChance >= 10 && !flameBombActive){
            List<Location> bombLocs = new ArrayList<>();
            List<ArmorStand> bombs = new ArrayList<>();
            flameBombDestroyed = false;
            flameBombActive = true;

            bombLocs.add(spirit.getLocation().clone().add(5,0,0));
            bombLocs.add(spirit.getLocation().clone().add(0,0,5));
            bombLocs.add(spirit.getLocation().clone().add(-5,0,0));
            bombLocs.add(spirit.getLocation().clone().add(0,0,-5));

            spirit.getLocation().getWorld().playSound(spirit.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 1.2f);

            for (Location loc : bombLocs){
                ArmorStand bomb = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                bomb.getEquipment().setHelmet(new ItemStack(Material.MAGMA_BLOCK));
                bomb.setVisible(false);
                bombs.add(bomb);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
            }

            // Spawn bombs --> choose active --> show active and duds --> explode active (if not destroyed) --> remove duds

            new BukkitRunnable(){
                @Override
                public void run() {
                    Boolean chosen = false;
                    for (int i = 0; i < bombs.size(); i++){
                        double chance = Math.random();
                        if (chance <= 0.25 || i == 3){
                            bombs.get(i).getEquipment().setHelmet(new ItemStack(Material.NETHER_WART_BLOCK)); // Active bomb chosen
                            flameBomb = bombs.get(i);
                            flameBomb.setMetadata("spirit_flame_bomb", new FixedMetadataValue(VendrickBossFight.plugin, "spirit_flame_bomb"));
                            flameBomb.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Flame Bomb Health: " + flameBombHealth);
                            flameBomb.setCustomNameVisible(true);
                            bombs.remove(bombs.get(i));
                            break;
                        }
                    }
                    for (ArmorStand b : bombs){
                        b.getEquipment().setHelmet(new ItemStack(Material.OBSIDIAN)); //Reveals the duds to the players
                        new BukkitRunnable(){ // Dud removal
                            @Override
                            public void run() {
                                b.getWorld().playSound(b.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);
                                b.getWorld().spawnParticle(Particle.SMOKE_NORMAL, b.getLocation(), 4);
                                b.remove();
                                bombs.remove(b);
                            }
                        }.runTaskLater(VendrickBossFight.plugin, 20 * 2);
                    }
                    spirit.getWorld().playSound(spirit.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 2.0f);
                    spirit.getWorld().playEffect(spirit.getLocation(), Effect.MOBSPAWNER_FLAMES, 4);
                    new BukkitRunnable(){ // FlameBomb explosion
                        @Override
                        public void run() {
                            if (!flameBombDestroyed){
                                flameBomb.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, flameBomb.getLocation(), 2);
                                flameBomb.getWorld().playSound(flameBomb.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
                                flameBomb.remove();
                                flameBomb = null;
                                flameBombDestroyed = false;
                                flameBombHealth = 5;

                                for (UUID id : VendrickBossFight.plugin.getFightManager().getFighting()){
                                    if (!spirit.isDead()){
                                        Player p = Bukkit.getPlayer(id);
                                        p.damage(1, spirit);
                                        p.setHealth(player.getHealth() - 5);
                                    }
                                }
                            }
                            flameBombActive = false;
                        }
                    }.runTaskLater(VendrickBossFight.plugin, 20 * 2);
                }
            }.runTaskLater(VendrickBossFight.plugin, 20 * 2);
        }

        int infChance = (int) (Math.random() * 9);
        if (infChance == 8 && !infernoing){
            List<UUID> pList = new ArrayList<>();
            pList.add(player.getUniqueId());
            inferno = new Inferno(spirit, pList);
            startInferno(spirit);
            ((LivingEntity) spirit).addPotionEffect(PotionEffectType.SLOW.createEffect(4, 2));
            infernoing = true;
        }
    }

    public void startInferno(Entity e){
        new BukkitRunnable(){
            int count = 0;
            float pitch = 0.0f;
            @Override
            public void run() {
                if (e.isDead()){
                    cancel();
                }

                if (count == 5){
                    inferno.blast();
                    e.getWorld().playSound(e.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.4f);
                    e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 20);
                    e.getWorld().playSound(e.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 3.0f, 1.0f);
                    infernoing = false;
                    cancel();
                }

                e.getWorld().playSound(e.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, pitch);
                e.getWorld().playEffect(e.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);

                count++;
                pitch += 0.5;
            }
        }.runTaskTimer(VendrickBossFight.plugin, 0, 10);
    }

    @EventHandler
    public void onPlayerHitFlameBomb(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)) {return;}
        if (!(e.getEntity().hasMetadata("spirit_flame_bomb"))) {return;}

        ArmorStand bomb = (ArmorStand) e.getEntity();

        e.setCancelled(true);
        flameBombHealth -= 1;
        bomb.getWorld().playSound(bomb.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 1.0f, 2.0f - ((flameBombHealth + 4 / 10f) * 2));
        flameBomb.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Flame Bomb Health: " + flameBombHealth);

        if (flameBombHealth <= 0){
            bomb.getWorld().playSound(bomb.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
            bomb.getWorld().playEffect(bomb.getLocation(), Effect.ENDER_DRAGON_DESTROY_BLOCK, 1);
            bomb.remove();
            flameBombDestroyed = true;
            flameBombHealth = 5;
        }

    }

    @EventHandler
    public void onFlameSpiritHitPlayer(EntityDamageByEntityEvent e){
        if (!(e.getDamager().hasMetadata("ven_spirit_flame"))) {return;}
        if (!(e.getEntity() instanceof Player)) {return;}

        e.getEntity().setFireTicks(60);
    }

    @EventHandler
    public void onSpiritSkipped(VendrickSkipSpiritEvent e) {
        if (!e.getSpiritData().equalsIgnoreCase("ven_spirit_flame")) {
            return;
        }

        ((DarkVendrick) plugin.getFightManager().getVendrick()).getSpirit().removeSpirit();
    }
}
