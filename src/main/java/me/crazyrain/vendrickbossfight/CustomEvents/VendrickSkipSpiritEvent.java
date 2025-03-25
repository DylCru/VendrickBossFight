package me.crazyrain.vendrickbossfight.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VendrickSkipSpiritEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private String spiritData;

    public VendrickSkipSpiritEvent(String spiritData) {
        this.spiritData = spiritData;
    }

    public String getSpiritData() {
        return spiritData;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
