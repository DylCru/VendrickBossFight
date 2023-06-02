package me.crazyrain.vendrickbossfight.CustomEvents;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class VendrickSpiritSpawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    String spiritType;
    List<UUID> fighting;
    Entity spirit;

    public VendrickSpiritSpawnEvent(String spiritType, List<UUID> fighting, Entity spirit){
        this.spiritType = spiritType;
        this.fighting = fighting;
        this.spirit = spirit;
    }

    public String getSpiritType() {
        return spiritType;
    }
    public List<UUID> getFighting() {
        return fighting;
    }
    public Entity getSpirit() {return spirit;}

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
