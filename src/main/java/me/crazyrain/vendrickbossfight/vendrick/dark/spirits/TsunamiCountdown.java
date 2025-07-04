package me.crazyrain.vendrickbossfight.vendrick.dark.spirits;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TsunamiCountdown {

    int seconds = 60;
    int totalTime;
    VendrickBossFight plugin;
    List<BossBar> bars = new ArrayList<>();
    Boolean active = true;
    Entity spirit;

    public TsunamiCountdown(VendrickBossFight plugin, Entity spirit){
        this.plugin = plugin;
        this.spirit = spirit;

        if (plugin.getFightManager().getFighting().size() > 1){
            if (plugin.getFightManager().getFighting().size() <= 3){
                seconds -= ((plugin.getFightManager().getFighting().size() - 1) * 10);
            } else {
                seconds = 40;
            }
        }

        totalTime = seconds;
    }

    int secondsElapsed = 0;
    public void startCountdown(){
        createBar();
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!active){
                    cancel();
                }

                if (seconds == 1){
                    endFight();
                    removeBars();
                    cancel();
                }

                if (seconds <= 10){
                    for (UUID p : plugin.getFightManager().getFighting()){
                        Bukkit.getPlayer(p).getWorld().playSound(Bukkit.getPlayer(p).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                    }
                }

                seconds--;
                secondsElapsed++;

                for (BossBar bar : bars){
                    bar.setProgress(1 - (float) (secondsElapsed / totalTime));
                    bar.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Seconds until annihilation: " + seconds);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void createBar(){
        for (UUID p : plugin.getFightManager().getFighting()){
            Player player = Bukkit.getPlayer(p);
            BossBar bar = Bukkit.createBossBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Seconds until annihilation: ",
                    BarColor.BLUE, BarStyle.SOLID);
            bar.addPlayer(player);
            bars.add(bar);
        }
    }

    public void removeBars(){
        for (BossBar bar : bars){
            bar.removeAll();
        }
    }

    public void setActive(Boolean active){
        this.active = active;
    }

    private void endFight(){
        for (UUID p : plugin.getFightManager().getFighting()){
            Player player = Bukkit.getPlayer(p);
            player.sendMessage(Lang.TSUNAMITIMEUP.toString());
            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 4);
            player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.5f, 0.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 1.4f);
            player.setHealth(0);
        }
    }

}
