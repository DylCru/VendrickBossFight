package me.crazyrain.vendrickbossfight.distortions.dark;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class DarkRuneHandler {

    VendrickBossFight plugin;
    int timer = 6;
    LivingEntity entity;
    ArmorStand runeStand;
    String activeRune;
    boolean paused = false;
    boolean active = true;
    String[] nums = {"⓿", "❶", "❷", "❸", "❹", "❺", "❻"};

    String[] runeNames = {"Explosive", "Life Steal", "Ballistic", "Wither Soul"};
    HashMap<String, Material> runeBlock = new HashMap<String, Material>(){{{
        put(runeNames[0], Material.TNT);
        put(runeNames[1], Material.NETHER_WART_BLOCK);
        put(runeNames[2], Material.AIR); //Vendrick will instead have some spinning particles around him
        put(runeNames[3], Material.WITHER_SKELETON_SKULL);
    }}};
    HashMap<String, ChatColor> runeNameColour = new HashMap<String, ChatColor>(){{{
        put(runeNames[0], ChatColor.WHITE);
        put(runeNames[1], ChatColor.LIGHT_PURPLE);
        put(runeNames[2], ChatColor.RED);
        put(runeNames[3], ChatColor.DARK_GRAY);
    }}};

    public DarkRuneHandler(LivingEntity entity, VendrickBossFight plugin){
        this.entity = entity;
        this.plugin = plugin;
        spawnRuneStand();
        countDown();
        rollForRune();
    }

    public String getActiveRune() {
        return activeRune;
    }

    public void spawnRuneStand(){
        runeStand = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation().clone().add(0,1,0), EntityType.ARMOR_STAND);
        runeStand.setVisible(false);
        runeStand.setMetadata("Ven_Rune_Stand", new FixedMetadataValue(plugin, "ven_rune_stand"));
        runeStand.setCustomName(runeNameColour.get(getActiveRune()) + "" + ChatColor.BOLD + getActiveRune() + runeNameColour.get(getActiveRune()) + nums[timer]);
        runeStand.setCustomNameVisible(true);
        runeStand.setGravity(false);
        for (EquipmentSlot slot : EquipmentSlot.values()){
            runeStand.addEquipmentLock(slot, ArmorStand.LockType.REMOVING_OR_CHANGING);
        }
        moveStand();
    }

    public void setPaused(Boolean paused){
        this.paused = paused;
    }
    public void setActive(Boolean active) { this.active = active;}

    public void moveStand(){
        new BukkitRunnable(){
            int rot = 0;
            @Override
            public void run() {
                rot += 6;
                Location location = entity.getLocation().clone().add(0,1,0);
                location.setYaw(rot);
                runeStand.teleport(location);
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void clearStand(){
        runeStand.remove();
    }

    private void changeBlock(){
        runeStand.getEquipment().setHelmet(new ItemStack(runeBlock.get(getActiveRune())));
        entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, entity.getLocation().add(0, 4, 0), 5);
        entity.getWorld().spawnParticle(Particle.SPELL, entity.getLocation().add(0, 2.5, 0), 6);
        entity.getWorld().spawnParticle(Particle.SPELL_WITCH, entity.getLocation().add(0, 3, 0), 15);
        entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 2.0f);
    }
    private void changeName(){
        runeStand.setCustomName(runeNameColour.get(getActiveRune()) + "" + ChatColor.BOLD + getActiveRune() + " " + runeNameColour.get(getActiveRune()) + nums[timer]);
    }
    private void rollForRune(){
        int rune = (int) (Math.random() * 4);
        while(runeNames[rune].equals(activeRune)){
            rune = (int) (Math.random() * 4);
        }
        activeRune = runeNames[rune];

        if (activeRune.equals(runeNames[0])){
            tntRune();
        }
        if (activeRune.equals(runeNames[2])){
            entity.addPotionEffect(PotionEffectType.SPEED.createEffect(120, 1));
            ParticleStand stand = new ParticleStand(entity.getLocation(), Color.RED, plugin, entity);
        }

        changeBlock();
    }

    public void tntRune(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (getActiveRune().equals(runeNames[0])){
                    if (!paused){
                        VendrickTNT.launchTNT(entity);
                    }
                    if(!active){
                        cancel();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 40);
    }

    private void countDown(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!paused){
                    timer--;
                    changeName();
                }
                if (!active){
                    cancel();
                }
                if (timer == 0){
                    timer = 6;
                    rollForRune();
                    changeName();
                }
            }
        }.runTaskTimer(plugin, 0, 15);
    }
}

