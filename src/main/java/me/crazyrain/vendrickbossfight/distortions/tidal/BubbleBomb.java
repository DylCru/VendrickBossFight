package me.crazyrain.vendrickbossfight.distortions.tidal;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BubbleBomb {

    Location venLoc;
    VendrickBossFight plugin;
    ArmorStand bubble;
    List<UUID> fighting;
    int radius = 2;
    boolean active = false;

    public BubbleBomb(Location venLoc, VendrickBossFight plugin, List<UUID> fighting) {
        this.venLoc = venLoc;
        this.plugin = plugin;
        this.fighting = fighting;
    }

    public void startAttack() {
        active = true;
        spawnBubbleStand();
        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                if (!active) {
                    cancel();
                }
                if (count == 2) {
                    plugin.vendrick.setSkipable(true);
                    for (UUID id : fighting){
                        Bukkit.getPlayer(id).sendMessage(Lang.BUBBLEHELP.toString());
                    }
                    playSound();
                    spawnParticles(bubble.getLocation(), radius, 1000);
                }
                if (count == 7) {
                    explode();
                    this.cancel();
                }
                count++;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void stopAttack() {
        this.active = false;
    }

    private void spawnBubbleStand() {
            Vector vector = new Vector(
                    ((Math.random() * (1.5 - -1.5)) + -1.5),
                    0.75,
                    (Math.random() * (1.5 - -1.5)) + -1.5);
            bubble = (ArmorStand) venLoc.getWorld().spawnEntity(venLoc, EntityType.ARMOR_STAND);
            bubble.setVisible(false);
            bubble.setSmall(true);
            bubble.setVelocity(vector);
            bubble.setMetadata("VenBubble", new FixedMetadataValue(plugin, "VenBubble"));
    }


    private void spawnParticles(Location location, double radius, int density) {
        location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 100 || !active) { // 5 seconds at 20 ticks per second
                    this.cancel();
                    return;
                }

                for (int i = 0; i < density; i++) {
                    double theta = Math.random() * 2 * Math.PI;
                    double phi = Math.acos(2 * Math.random() - 1);

                    double x = radius * Math.sin(phi) * Math.cos(theta);
                    double y = radius * Math.sin(phi) * Math.sin(theta);
                    double z = radius * Math.cos(phi);

                    location.getWorld().spawnParticle(Particle.WATER_BUBBLE, location.clone().add(x, y, z),1, 0, 0, 0, 0);
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Runs every tick
    }

    private void playSound() {
        new BukkitRunnable(){
            int count = 0;
            float pitch = 0.0f;
            @Override
            public void run() {
                if (count == 5){
                    cancel();
                }

                venLoc.getWorld().playSound(venLoc, Sound.ENTITY_HOSTILE_SPLASH, 2.0f, pitch);
                venLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, venLoc, 20);

                count++;
                pitch += 0.5;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void explode() {
        List<UUID> safe = new ArrayList<>();
        for (Entity p : bubble.getNearbyEntities(radius, radius, radius)) {
            if (p instanceof Player) {
                if (this.fighting.contains(p.getUniqueId())) {
                    safe.add(p.getUniqueId());
                }
            }
        }
        for (UUID id : this.fighting) {
            Player player = Bukkit.getPlayer(id);
            assert player != null;
            if (!safe.contains(id)) {
                player.damage(10, plugin.vendrick.getVendrick());
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
                player.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
            }
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        }


        this.bubble.remove();
        this.plugin.vendrick.stopAttack();
        if (plugin.runeHandler != null) {
            plugin.runeHandler.setPaused(false);
        }
    }
}
