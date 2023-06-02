package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class ItemGlow implements Listener {

    private static final HashMap<Rarity, ChatColor> rarityColours = new HashMap<Rarity, ChatColor>(){{{
        put(Rarity.RARE, ChatColor.BLUE);
        put(Rarity.EPIC, ChatColor.DARK_PURPLE);
        put(Rarity.SPECIAL, ChatColor.LIGHT_PURPLE);
        put(Rarity.INSANE, ChatColor.RED);
    }}};

    private static Team rare;
    private static Team epic;
    private static Team special;
    private static Team insane;


    public static void initTeams(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        try {
            rare = board.registerNewTeam("rareTeam");
            epic = board.registerNewTeam("epicTeam");
            special = board.registerNewTeam("specialTeam");
            insane = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("insaneTeam");

            rare.setColor(rarityColours.get(Rarity.RARE));
            epic.setColor(rarityColours.get(Rarity.EPIC));
            special.setColor(rarityColours.get(Rarity.SPECIAL));
            insane.setColor(rarityColours.get(Rarity.INSANE));
        } catch(Exception ignored){}
    }

    public static void removeTeams(){
        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
            if (team.getDisplayName().equalsIgnoreCase("insaneteam")){
                team.unregister();
            } else if (team.getDisplayName().equalsIgnoreCase("epicteam")){
                team.unregister();
            } else if (team.getDisplayName().equalsIgnoreCase("specialteam")){
                team.unregister();
            } else if (team.getDisplayName().equalsIgnoreCase("rareteam")){
                team.unregister();
            }
        }
    }


    @EventHandler
    public void onVendrickItemSpawn(ItemSpawnEvent e){
        Item item = e.getEntity();
        if (!item.getItemStack().hasItemMeta()){
            return;
        }
        if (!item.getItemStack().getItemMeta().hasLore()){
            return;
        }
        if (item.getItemStack().getItemMeta().getLore().contains(Rarity.RARE.toString())){
            rare.addEntry(item.getUniqueId().toString());
        }
        if (item.getItemStack().getItemMeta().getLore().contains(Rarity.EPIC.toString())){
            epic.addEntry(item.getUniqueId().toString());
        }
        if (item.getItemStack().getItemMeta().getLore().contains(Rarity.SPECIAL.toString())){
            special.addEntry(item.getUniqueId().toString());
        }
        if (item.getItemStack().getItemMeta().getLore().contains(Rarity.INSANE.toString())){
            insane.addEntry(item.getUniqueId().toString());
        }

        item.setGlowing(true);

    }

}
