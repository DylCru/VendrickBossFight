package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.vendrick.Vendrick;
import me.crazyrain.vendrickbossfight.vendrick.dark.DarkRuneHandler;
import me.crazyrain.vendrickbossfight.vendrick.dark.DarkVendrick;
import me.crazyrain.vendrickbossfight.vendrick.dark.spirits.TsunamiCountdown;
import me.crazyrain.vendrickbossfight.vendrick.stormy.Hurricane;
import me.crazyrain.vendrickbossfight.vendrick.tidal.TidalVendrick;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FightManager {

    private boolean venSpawned = false;
    private List<UUID> fighting = new ArrayList<>();
    private int phase;
    private boolean skipable;
    private HashMap<UUID, ItemStack[]> pInv = new HashMap<>();
    private List<UUID> losers = new ArrayList<>();

    private Vendrick vendrick;

    private Hurricane hurricane;
    private DarkRuneHandler runeHandler;
    private TsunamiCountdown countdown;
    private int squids = 4;

    public FightManager() {}

    public void damageBoss(double damage) {
        vendrick.damage(damage);
    }

    public void clearDown() {
        vendrick.getEntity().remove();
        try {
            runeHandler.clearStand();
        } catch (Exception ignored) {}
        try {
            hurricane.removeBar();
        } catch (Exception ignored) {}
        try {
            countdown.removeBars();
        } catch (Exception ignored) {}
        try {
            ((TidalVendrick) vendrick).removeSheilds();
        } catch (Exception ignored) {}
        try {
            ((DarkVendrick) vendrick).getSpirit().removeSpirit();
        } catch (Exception ignored) {}
    }

    public List<UUID> getFighting() {
        return fighting;
    }

    public void setFighting(List<UUID> fighting) {
        this.fighting = fighting;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public boolean isSkipable() {
        return skipable;
    }

    public void setSkipable(boolean skippable) {
        this.skipable = skippable;
    }

    public Vendrick getVendrick() {
        return vendrick;
    }

    public void setVendrick(Vendrick vendrick) {
        this.vendrick = vendrick;
    }

    public Hurricane getHurricane() {
        return hurricane;
    }

    public void setHurricane(Hurricane hurricane) {
        this.hurricane = hurricane;
    }

    public DarkRuneHandler getRuneHandler() {
        return runeHandler;
    }

    public void setRuneHandler(DarkRuneHandler runeHandler) {
        this.runeHandler = runeHandler;
    }

    public TsunamiCountdown getCountdown() {
        return countdown;
    }

    public void setCountdown(TsunamiCountdown countdown) {
        this.countdown = countdown;
    }

    public HashMap<UUID, ItemStack[]> getpInv() {
        return pInv;
    }

    public void setpInv(HashMap<UUID, ItemStack[]> pInv) {
        this.pInv = pInv;
    }

    public boolean isVenSpawned() {
        return venSpawned;
    }

    public void setVenSpawned(boolean venSpawned) {
        this.venSpawned = venSpawned;
    }

    public int getSquids() {
        return squids;
    }

    public void setSquids(int squids) {
        this.squids = squids;
    }

    public List<UUID> getLosers() {
        return losers;
    }

    public void setLosers(List<UUID> losers) {
        this.losers = losers;
    }

    public void addLoser(UUID id) {
        losers.add(id);
    }

}
