package me.crazyrain.vendrickbossfight.attacks;

import me.crazyrain.vendrickbossfight.items.ItemManager;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerShatter implements Listener {
    VendrickBossFight plugin;

    public PlayerShatter(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    List<UUID> onCooldown = new ArrayList<>();

    @EventHandler
    public void onMobHit(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            if (!(onCooldown.contains(player.getUniqueId()))){
                if (player.getInventory().getItemInMainHand().hasItemMeta()){
                if (Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).hasDisplayName()){
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Objects.requireNonNull(ItemManager.shatterStick.getItemMeta()).getDisplayName())){
                        if (plugin.getConfig().getStringList("disabled-items").contains(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName())){
                            player.sendMessage(ChatColor.RED + "This item is currently disabled!");
                            return;
                        }

                        onCooldown.add(player.getUniqueId());
                        cooldown(player);
                        Entity entity = e.getEntity();
                        stunMob((LivingEntity) entity);
                        Location loc = entity.getLocation();
                        Integer x = entity.getLocation().getBlockX();
                        Integer y = entity.getLocation().getBlockY();
                        Integer z = entity.getLocation().getBlockZ();
                        makeCircle(new Location(entity.getWorld(),x,y,z), 2f, Color.RED);


                        Boolean showCooldown = plugin.getConfig().getBoolean("ShowCooldown");

                        if (showCooldown){
                            String cooldown = ChatColor.GREEN + "Used " + ChatColor.GOLD + "" + ChatColor.BOLD + "SHATTER";
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(cooldown));
                        }

                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                makeCircle(new Location(entity.getWorld(),x,y,z), 2f, Color.MAROON);
                                loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 5, 1);
                                placeObby(entity, x,y,z,loc);
                            }
                        }.runTaskLater(plugin, 20 * 2);
                        }
                    }
                }
            }
        }
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

    public void placeObby(Entity entity, Integer x, Integer y, Integer z, Location loc){
        new BukkitRunnable(){

            @Override
            public void run() {
                makeCircle(new Location(entity.getWorld(),x,y,z), 2f, Color.BLACK);
                loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 10f, 1);
                loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BREAK, 10f, 1);
                loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_BREAK_BLOCK, 10f, 1);
                damageMobs(loc);
            }
        }.runTaskLater(plugin, 20 * 2);
    }

    public void damageMobs(Location loc){
        ArmorStand mobCheckStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        mobCheckStand.setInvulnerable(true);
        mobCheckStand.setVisible(true);

        for (Entity e : mobCheckStand.getNearbyEntities(1.5,3,1.5)){
            if (e instanceof LivingEntity){
                LivingEntity mob = (LivingEntity) e;
                if (!(mob instanceof Player)){
                    Vector vector = mob.getLocation().getDirection();
                    vector.setY(1);
                    mob.setVelocity(vector);
                    if (plugin.getConfig().get("PlayerShatterDmg") == null || plugin.getConfig().getInt("PlayerShatterDmg") == 0){
                        mob.damage(15);
                    } else {
                        int damage = plugin.getConfig().getInt("PlayerShatterDmg");
                        mob.damage(damage);
                    }
                    mob.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, mob.getLocation(), 5);
                }
            }
        }

        loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ(),  10);
        mobCheckStand.remove();
    }

    public void cooldown(Player player){
        new BukkitRunnable(){

            @Override
            public void run() {

                boolean showCooldown = plugin.getConfig().getBoolean("ShowCooldown");

                if (showCooldown){
                    String cooldown = ChatColor.GOLD + "" + ChatColor.BOLD + "SHATTER " + ChatColor.GREEN + "is now off cooldown!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(cooldown));
                }
                onCooldown.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 20 * 7);
    }

    public void stunMob(LivingEntity e){
        e.setAI(false);
        e.setGlowing(true);
        new BukkitRunnable(){

            @Override
            public void run() {
                e.setAI(true);
                e.setGlowing(false);
            }
        }.runTaskLater(plugin, 20 * 4);
    }
}
