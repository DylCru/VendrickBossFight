package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AttackCharge {
    String title;
    BarColor color;
    Double period;
    Player player;


    public AttackCharge(String title, Player player){
        this.title = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Charging: " + title;
        this.color = BarColor.RED;
        this.period = 0.02;
        this.player = player;
        animateBar();
    }

    public void animateBar(){
        BossBar bar = Bukkit.createBossBar(this.title, this.color, BarStyle.SEGMENTED_10);
        bar.setProgress(0);
        bar.addPlayer(player);
        Double increment = this.period;

        new BukkitRunnable(){
            Double progress = 0.0;
            @Override
            public void run() {
                if ((progress += increment) < 1){
                    progress += increment;
                    try{
                        bar.setProgress(progress);
                    } catch (IllegalArgumentException e){
                        bar.setProgress(1);
                    }
                } else {
                    bar.removeAll();
                    cancel();
                }
            }
        }.runTaskTimer(VendrickBossFight.getPlugin(VendrickBossFight.class), 0, 2);

    }
}
