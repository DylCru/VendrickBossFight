package me.crazyrain.vendrickbossfight.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class VendrickFightStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private List<UUID> players;
    private String distortion;
    private int difficulty;

    public VendrickFightStartEvent(List<UUID> players, String distortion, int difficulty){
        this.players = players;
        this.distortion = distortion;
        this.difficulty = difficulty;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public String getDistortion() {
        return distortion;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
