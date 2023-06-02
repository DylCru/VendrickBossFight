package me.crazyrain.vendrickbossfight.functionality;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.UUID;

public class Bar {

    Player player;
    BossBar bar;

    public Bar(Player player, String title, BarColor color){
        this.player = player;
        bar = Bukkit.createBossBar(title, color, BarStyle.SEGMENTED_10);
    }

    public UUID getPlayer(){
        return player.getUniqueId();
    }

    public void add(){
        bar.addPlayer(player);
    }

    public void remove(){
        bar.removePlayer(player);
    }

    public void fill(Double progress){
        try {
            bar.setProgress(progress);
        } catch (Exception ignored){}
    }



}
