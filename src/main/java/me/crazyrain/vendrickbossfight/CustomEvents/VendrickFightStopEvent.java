package me.crazyrain.vendrickbossfight.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class VendrickFightStopEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private List<UUID> players;
    private List<UUID> losers;
    private List<UUID> winners;
    private String distortion;
    private int difficulty;

    public VendrickFightStopEvent(List<UUID> players, List<UUID> winners, List<UUID> losers, String distortion, int difficulty){
        this.players = players;
        this.distortion = distortion;
        this.difficulty = difficulty;
        this.winners = winners;
        this.losers = losers;
    }

    public List<UUID> getLosers() {
        return losers;
    }

    public List<UUID> getWinners() {
        return winners;
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
