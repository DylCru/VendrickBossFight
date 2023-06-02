package me.crazyrain.vendrickbossfight.distortions.stormy;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BooleanSupplier;

public class Hurricane {

    VendrickBossFight plugin = VendrickBossFight.getPlugin(VendrickBossFight.class);

    Vendrick vendrick;
    float radius;
    double damage;
    int timer;
    int round;

    List<BossBar> status = new ArrayList<>();

    public Hurricane(Vendrick vendrick){
        this.vendrick = vendrick;
        radius = 4;
        damage = 13;
        timer = 0;
        round = 0;

        for (UUID id : plugin.fighting){
            BossBar bStatus = Bukkit.createBossBar(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Vendrick's Storm", BarColor.BLUE, BarStyle.SOLID);
            bStatus.addPlayer(Bukkit.getPlayer(id));
            status.add(bStatus);
        }

        spawnCircle();
        buffStorm();
        stormEffects();
    }


    private void stormEffects(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (!plugin.venSpawned){
                    cancel();
                }

                for (Entity e : vendrick.getVendrick().getNearbyEntities(radius, 2, radius)){
                    if (!(e instanceof  Player)){
                        continue;
                    }
                    Player player = (Player) e;
                    for (int i = 0; i < 5; i++){player.getWorld().strikeLightningEffect(player.getLocation());}
                    player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 1.4f);
                    player.damage(damage, plugin.vendrick.getVendrick());
                    player.sendMessage(ChatColor.RED + "This storm is really violent. You shouldn't stay in it for long.");
                }
            }
        }.runTaskTimer(plugin, 0, 40);
    }

    private void buffStorm(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (!plugin.venSpawned){
                    removeBar();
                    cancel();
                }

                if (timer == 7){
                    round++;
                    if (round % 2 == 1){
                        if (radius < 8){
                            radius += 0.5;
                            for (UUID id : plugin.fighting){
                                Player player = Bukkit.getPlayer(id);
                                player.sendMessage(ChatColor.RED + "The storm grows...");
                                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                                if (radius >= 8){
                                    player.sendMessage( ChatColor.RED + "The storm is at max size!");
                                }
                            }
                        }

                    } else {
                        if (damage < 25){
                            damage += 1;
                            for (UUID id : plugin.fighting){
                                Player player = Bukkit.getPlayer(id);
                                player.sendMessage(ChatColor.RED + "The storm gets stronger...");
                                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
                                if (radius >= 24){
                                    player.sendMessage( ChatColor.RED + "The storm is at max power!");
                                }
                            }
                        }

                    }
                    for (BossBar bar : status){
                        bar.setTitle(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Vendrick's Storm");
                    }
                    timer = 0;
                }
                timer++;
                for (BossBar bar : status){
                    if (timer == 1){
                        bar.setProgress(0);
                    }
                    bar.setProgress(timer / 7.0);
                }
            }
        }.runTaskTimer(plugin, 0, 15);
    }

    private void spawnCircle(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if (plugin.venSpawned){
                    makeCircle(vendrick.getVendrick().getLocation(), radius, Color.MAROON);
                } else {
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void setDamage(double damage){
        this.damage = damage;
        if (this.damage <= 10){
            this.damage = 10;
        }
        if (this.damage >= 25){
            this.radius = 24;
        }
    }
    public double getDamage() {return damage;}

    public void setRadius(float radius){
        this.radius = radius;
        if (this.radius <= 3){
            this.radius = 3;
        }
        if (this.radius >= 8){
            this.radius = 8;
        }
    }
    public float getRadius() {return radius;}

    public void removeBar(){
        for (BossBar bar : status){
            bar.removeAll();
        }
    }

    public void makeCircle(Location loc, Float radius, Color color){
        new BukkitRunnable(){
            Integer t = 0;
            @Override
            public void run() {
                if (t >= 20){
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

}
