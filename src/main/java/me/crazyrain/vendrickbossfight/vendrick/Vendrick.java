package me.crazyrain.vendrickbossfight.vendrick;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStartEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vindicator;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vendrick {

    final int DEFAULT_HEALTH = 500;
    int health;
    List<UUID> players = new ArrayList<>();
    Vindicator vendrick;
    int phase = 0;
    VendrickBossFight plugin;
    Location spawnLoc;
    boolean skipable = false;

    public Vendrick(List<UUID> players, Location spawnLoc, VendrickBossFight plugin){
        this.players = players;
        this.plugin = plugin;
        this.spawnLoc = spawnLoc;
    }

    public void spawnBoss(){
        vendrick = (Vindicator) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.VINDICATOR);
        vendrick.setCustomName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Vendrick");
        vendrick.getEquipment().setItemInMainHand(ItemManager.vendrickHatchet);
        vendrick.setPatrolLeader(false);
        vendrick.setMetadata("Vendrick", new FixedMetadataValue(plugin, "vendrick"));
        vendrick.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100000, 1));

        int configHealth = plugin.getConfig().getInt("vendrick-health");
        if (configHealth == 0) {
            health = DEFAULT_HEALTH;
        } else {
            health = configHealth;
        }

        Bukkit.broadcastMessage("Vendrick has " + health + " health");

//        if (plugin.getConfig().getInt("vendrick-health") > 2048){ //remove
//            plugin.getConfig().set("vendrick-health", 2048);
//            AttributeModifier modifier = new AttributeModifier(Objects.requireNonNull(vendrick.getCustomName()), 2048, AttributeModifier.Operation.ADD_NUMBER);
//            vendrick.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(modifier);
//            vendrick.setHealth(2048);
//
//        } else if (plugin.getConfig().getInt("vendrick-health") < 500){
//            AttributeModifier modifier = new AttributeModifier(Objects.requireNonNull(vendrick.getCustomName()), 500, AttributeModifier.Operation.ADD_NUMBER);
//            plugin.getConfig().set("vendrick-health", 500);
//            vendrick.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(modifier);
//            vendrick.setHealth(500);
//
//        } else {
//            AttributeModifier modifier = new AttributeModifier(Objects.requireNonNull(vendrick.getCustomName()), plugin.getConfig().getInt("vendrick-health"), AttributeModifier.Operation.ADD_NUMBER);
//            vendrick.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(modifier);
//            vendrick.setHealth(plugin.getConfig().getInt("vendrick-health"));
//        }
//        vendrick.setHealth(plugin.getConfig().getInt("vendrick-health"));
        Bukkit.getServer().getPluginManager().callEvent(new VendrickFightStartEvent(players, getDistortion(), getDifficulty()));
    }

    public List<UUID> getPlayers(){
        return players;
    }

    public double getHealth(){
        return vendrick.getHealth();
    }

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

    public void stopAttack(){
        vendrick.setAI(true);
        vendrick.setInvulnerable(false);
        vendrick.setGlowing(false);
        setPhase(0);
        setSkipable(false);
    }

    public void setSkipable(Boolean skipable){
        this.skipable = skipable;
    }
    public boolean getSkipable(){
        return skipable;
    }

    public void damage(double damage) {
        health -= damage;
        if (health <= 0) {
            Bukkit.broadcastMessage("Vendrick has been killed");
        }
        Bukkit.broadcastMessage("Health Remaining: " + health);
    }

    public int getPhase(){
        return phase;
    }
    public void setPhase(int phase){
        this.phase = phase;
    }

    public boolean wasDistorted(){
        return false;
    }
    public int getDifficulty() {return 1;}

    public String getDistortion(){ return "Normal";}

}
