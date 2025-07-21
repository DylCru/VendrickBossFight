package me.crazyrain.vendrickbossfight.vendrick.flaming;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickDeathEvent;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vindicator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlamingVendrick extends Vendrick{

    final int DEFAULT_HEALTH = 500;
    int health;
    List<UUID> players = new ArrayList<>();
    Vindicator vendrick;
    VendrickBossFight plugin;
    Location spawnLoc;
    int phase;
    boolean skipable = false;

    ItemStack fireHatchet = ItemManager.vendrickHatchet.clone();

    public FlamingVendrick(List<UUID> players, Location spawnLoc, VendrickBossFight plugin) {
        super(players, spawnLoc, plugin);
        this.players = players;
        this.plugin = plugin;
        this.spawnLoc = spawnLoc;
    }

    @Override
    public void spawnBoss(){
        vendrick = (Vindicator) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.VINDICATOR);
        vendrick.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "FLAMING " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Vendrick");
        vendrick.getEquipment().setItemInMainHand(fireHatchet);
        vendrick.setPatrolLeader(false);

        vendrick.setMetadata("Vendrick", new FixedMetadataValue(plugin, "vendrick"));
        vendrick.addScoreboardTag("venFlame");
        vendrick.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100000, 2));
        vendrick.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(100000, 1));
        vendrick.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(100000, 1));

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

    public void damage(double damage) {
        health -= damage;
        if (health <= 0) {
            Bukkit.broadcastMessage("Vendrick has been killed");
            getEntity().setHealth(0);
            plugin.getServer().getPluginManager().callEvent(new VendrickDeathEvent(this, null));
        }
        Bukkit.broadcastMessage("Health Remaining: " + health);
    }

    @Override
    public boolean wasDistorted(){
        return true;
    } //keep
    @Override
    public int getDifficulty() {return 2;} //keep

    @Override
    public Distortion getDistortion(){ return Distortion.FIRE;} //keep
}
