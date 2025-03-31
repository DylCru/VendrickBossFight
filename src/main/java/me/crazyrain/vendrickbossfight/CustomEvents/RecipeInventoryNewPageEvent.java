package me.crazyrain.vendrickbossfight.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RecipeInventoryNewPageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    boolean forward;

    public RecipeInventoryNewPageEvent(boolean forward) {
        this.forward = forward;
    }

    public boolean isForward() {
        return this.forward;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
