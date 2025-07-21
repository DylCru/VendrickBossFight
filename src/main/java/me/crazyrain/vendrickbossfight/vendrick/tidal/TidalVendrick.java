package me.crazyrain.vendrickbossfight.vendrick.tidal;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStartEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.Distortion;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import me.crazyrain.vendrickbossfight.vendrick.Vendrick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TidalVendrick extends Vendrick {

    final int DEFAULT_HEALTH = 500;
    int health;
    List<UUID> players = new ArrayList<>();
    Vindicator vendrick;
    VendrickBossFight plugin;
    Location spawnLoc;
    int phase;
    boolean skipable = false;
    ArrayList<Entity> sheilds = new ArrayList<>();
    BubbleBomb bubbleBomb;

    public TidalVendrick(List<UUID> players, Location spawnLoc, VendrickBossFight plugin) {
        super(players, spawnLoc, plugin);
        this.players = players;
        this.plugin = plugin;
        this.spawnLoc = spawnLoc;
    }

    @Override
    public void spawnBoss(){
        vendrick = (Vindicator) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.VINDICATOR);
        vendrick.setCustomName(ChatColor.BLUE + "" + ChatColor.BOLD + "TIDAL " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Vendrick");
        vendrick.getEquipment().setItemInMainHand(ItemManager.vendrickHatchet);
        vendrick.setPatrolLeader(false);

        vendrick.setMetadata("Vendrick", new FixedMetadataValue(plugin, "vendrick"));
        vendrick.addScoreboardTag("venTide");
        vendrick.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100000, 2));
        vendrick.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(100000, 1));
        vendrick.addPotionEffect(PotionEffectType.SPEED.createEffect(100000, 2));

        int configHealth = plugin.getConfig().getInt("vendrick-health");
        if (configHealth == 0) {
            health = DEFAULT_HEALTH;
        } else {
            health = configHealth;
        }

        Bukkit.getServer().getPluginManager().callEvent(new VendrickFightStartEvent(players, getDistortion(), getDifficulty()));
    }

    public double getHealth(){
        return health;
    }

    @Override
    public LivingEntity getEntity(){
        return vendrick;
    }

    @Override
    public void startAttack(int phase){
        vendrick.setAI(false);
        vendrick.setInvulnerable(true);
        vendrick.setGlowing(true);
        vendrick.teleport(vendrick.getLocation().add(0,4,0));
        this.phase = phase;

        vendrick.getLocation().getWorld().playSound(vendrick.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 10, 0.6f);
    }

    @Override
    public void stopAttack(){
        vendrick.setAI(true);
        vendrick.setInvulnerable(false);
        vendrick.setGlowing(false);
        setPhase(0);
        setSkipable(false);
    }

    @Override
    public void setSkipable(Boolean skipable){
        this.skipable = skipable;
    }
    @Override
    public boolean getSkipable(){
        return skipable;
    }

    @Override
    public int getPhase(){
        return phase;
    }
    @Override
    public void setPhase(int phase){
        this.phase = phase;
    }

    @Override
    public boolean wasDistorted(){
        return true;
    }

    @Override
    public int getDifficulty() {return 3;}

    @Override
    public Distortion getDistortion(){ return Distortion.WATER;}

    public ArrayList<Entity> getSheilds() {
        return sheilds;
    }

    public void removeSheilds() {
        for (Entity e : getSheilds()) {
            try {
                e.remove();
            } catch (Exception ignored) {}
        }
    }

    public void setBubbleBomb(BubbleBomb bubbleBomb) {
        this.bubbleBomb = bubbleBomb;
    }

    public BubbleBomb getBubbleBomb() {
        return bubbleBomb;
    }
}
