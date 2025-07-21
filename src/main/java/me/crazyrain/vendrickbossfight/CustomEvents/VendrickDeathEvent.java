package me.crazyrain.vendrickbossfight.CustomEvents;

import me.crazyrain.vendrickbossfight.vendrick.Vendrick;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VendrickDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    Vendrick vendrick;
    Player killer;

    public VendrickDeathEvent(Vendrick vendrick, Player killer) {
        this.vendrick = vendrick;
        this.killer = killer;
    }

    public Vendrick getVendrick() {
        return vendrick;
    }

    public Player getKiller() {
        return killer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
