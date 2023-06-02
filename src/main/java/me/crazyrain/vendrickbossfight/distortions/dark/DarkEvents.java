package me.crazyrain.vendrickbossfight.distortions.dark;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStartEvent;
import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSpiritSpawnEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.distortions.dark.spirits.DistSpirit;
import me.crazyrain.vendrickbossfight.distortions.dark.spirits.SpiritSpawnAnim;
import me.crazyrain.vendrickbossfight.functionality.Bar;
import me.crazyrain.vendrickbossfight.functionality.ItemManager;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DarkEvents implements Listener {

    List<String> spawned = new ArrayList<>();

    VendrickBossFight plugin;

    public DarkEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    boolean endExplodeActive = true;
    LivingEntity eKing;

    @EventHandler
    public void onDarkVendrickSpawn(VendrickFightStartEvent e){
        if (e.getDistortion().equalsIgnoreCase("dark")){
            VendrickBossFight.plugin.runeHandler = new DarkRuneHandler(VendrickBossFight.plugin.vendrick.getVendrick(), VendrickBossFight.plugin);
            endExplodeActive = true;
            spawned.clear();
        }
    }

    @EventHandler
    public void onDarkVendrickDeath(EntityDeathEvent e){
        if (!e.getEntity().getScoreboardTags().contains("venDark")) {return;}
        e.getDrops().clear();
        if (endExplodeActive){
            spawned.clear();
            plugin.runeHandler.setActive(false);
            plugin.runeHandler.clearStand();
            plugin.runeHandler = null;

            if (!plugin.getConfig().getBoolean("skip-dark-cutscene")){
                e.getEntity().setHealth(1);
                e.getEntity().setAI(false);
                e.getEntity().setInvulnerable(true);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        endText(e.getEntity(), e.getEntity().getLocation());
                    }
                }.runTaskLater(plugin, 40);
            } else {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        for (UUID p : plugin.fighting){
                            victory(Bukkit.getPlayer(p));
                        }
                        plugin.fighting.clear();
                        plugin.venSpawned = false;
                    }
                }.runTaskLater(plugin, 40);
            }
        }
    }

    private void endText(LivingEntity e, Location loc){
        new BukkitRunnable(){
            int text = -1;
            @Override
            public void run() {
                text += 1;
                for (UUID p : plugin.fighting){
                    Player player = Bukkit.getPlayer(p);
                    switch (text){
                        case 0:
                            player.sendMessage(Lang.DARKEND1.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.9f);
                            break;
                        case 1:
                            player.sendMessage(Lang.DARKEND2.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.9f);
                            break;
                        case 2:
                            player.sendMessage(Lang.DARKEND3.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.9f);
                            break;
                        case 3:
                            player.sendMessage(Lang.DARKEND4.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.9f);
                            break;
                        case 4:
                            player.sendMessage(Lang.DARKEND5.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
                            break;
                        case 5:
                            player.sendMessage(Lang.DARKEND6.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.9f);
                            break;
                        case 6:
                            eKing = (LivingEntity) loc.getWorld().spawnEntity(loc.clone().add(0,3,0), EntityType.WITHER);
                            eKing.setAI(false);
                            eKing.setGlowing(true);
                            eKing.setInvulnerable(true);
                            eKing.setCustomName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Eternal King");
                            loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.9f);
                            break;
                        case 7:
                            player.sendMessage(Lang.DARKEND7.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
                            break;
                        case 8:
                            player.sendMessage(Lang.DARKEND8.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
                            break;
                        case 9:
                            player.sendMessage(Lang.DARKEND9.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.9f);
                            e.getWorld().strikeLightningEffect(loc);
                            endExplodeAnim(loc);
                            break;
                        case 10:
                            player.sendMessage(Lang.DARKEND10.toString());
                            break;
                        case 11:
                            loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_DEATH, 2.0f, 1.5f);
                            break;
                        case 12:
                            endExplodeActive = false;
                            e.setHealth(0);
                            break;
                        case 13:
                            player.sendMessage(Lang.DARKEND11.toString());
                            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
                            break;
                        case 14:
                            eKing.getWorld().spawnParticle(Particle.SPELL_WITCH, eKing.getLocation(), 10);
                            eKing.getWorld().playSound(eKing.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f,1.0f);
                            eKing.remove();
                            break;
                        case 15:
                            victory(player);
                            plugin.fighting.clear();
                            plugin.venSpawned = false;
                            break;
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 80);
    }

    private void endExplodeAnim(Location loc){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!endExplodeActive){
                    cancel();
                }

                loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc.clone().add(0,1,0), 4);
                loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    public void victory(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 0.7f);
        player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY", ChatColor.BLUE + "The eternal guardian is no more.", 10, 160, 20);
        plugin.pInv.clear();

        for (Entity e : player.getNearbyEntities(100, 100, 100)) {
            if (e.hasMetadata("Wraith")) {
                e.remove();
            }
        }

        for (Bar bar : plugin.bars) {
            bar.remove();
        }
        plugin.bars.clear();

        if (plugin.getConfig().get("WinMessage") == null || Objects.requireNonNull(plugin.getConfig().getString("WinMessage")).equalsIgnoreCase("")) {
            if (plugin.getConfig().getBoolean("BroadcastMessage") || plugin.getConfig().get("BroadcastMessage") == null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + ChatColor.ITALIC + player.getDisplayName() + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + " has defeated Dark Vendrick!");
                }
            } else {
                player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + ChatColor.ITALIC + player.getDisplayName() + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + " has defeated Dark Vendrick!");
            }
        } else {
            String message = plugin.getConfig().getString("WinMessage");
            assert message != null;
            message = message.replace("[player]", player.getDisplayName());
            if (plugin.getConfig().getBoolean("BroadcastMessage") || plugin.getConfig().get("BroadcastMessage") == null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                if (plugin.getConfig().getBoolean("do-drops")){
                    player.sendMessage(ChatColor.GRAY + "As the guardian falls, some loot was left behind");
                    plugin.lootHandler.lootRoll(player, plugin.vendrick.getDifficulty());
                }
            }
        }.runTaskLater(plugin, 20);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void talismanEffect(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Vindicator)) {return;}
        if (!(e.getEntity() instanceof Player)) {return;}
        if (!e.getDamager().hasMetadata("Vendrick")) {return;}

        Player player = (Player) e.getEntity();

        if (ArrayUtils.contains(player.getInventory().getContents(), ItemManager.vendrickTalisman)){
            e.setDamage(e.getDamage() * 0.90);
        }
    }

    @EventHandler
    public void checkVendrickHealth(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Vindicator)) {return;}
        if (!(e.getDamager() instanceof Player)) {return;}
        if (!e.getEntity().hasMetadata("Vendrick")) {return;}
        if (!e.getEntity().getScoreboardTags().contains("venDark")) {return;}
        if (e.getDamager() instanceof Arrow){
            if (((Arrow) e.getDamager()).getShooter() instanceof Player){
                Player player = (Player) ((Arrow) e.getDamager()).getShooter();
                UUID pId = player.getUniqueId();
                if (!VendrickBossFight.plugin.fighting.contains(pId) && ! e.getDamager().isOp()){
                    player.sendMessage(Lang.PURE.toString());
                    VendrickBossFight.plugin.vendrick.getVendrick().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, VendrickBossFight.plugin.vendrick.getVendrick().getLocation(), 3);
                    e.setCancelled(true);
                    return;
                }
            }
        }

        double rawPercent = ((Vindicator) e.getEntity()).getHealth() / VendrickBossFight.plugin.getConfig().getInt("vendrick-health");
        double percent = Math.round(rawPercent * 100.0) / 100.0;

        if (percent <= 0.75){
            if (!spawned.contains("flame")){
                spawnSpirit(e.getEntity().getLocation().clone().add(0,4,0), "flame", Color.ORANGE);
                spawned.add("flame");
                ((Vindicator) e.getEntity()).setHealth(plugin.getConfig().getInt("vendrick-health") * 0.75);
                for (Bar bar : plugin.bars){
                    bar.fill(0.75);
                }
            }
        }
        if (percent <= 0.50){
            if (!spawned.contains("tide")){
                spawnSpirit(e.getEntity().getLocation().clone().add(0,4,0), "tide", Color.BLUE);
                spawned.add("tide");
                ((Vindicator) e.getEntity()).setHealth(plugin.getConfig().getInt("vendrick-health") * 0.5);
                for (Bar bar : plugin.bars){
                    bar.fill(0.5);
                }
            }
        }
        if (percent <= 0.25){
            if (!spawned.contains("storm")){
                spawnSpirit(e.getEntity().getLocation().clone().add(0,4,0), "storm", Color.YELLOW);
                spawned.add("storm");
                ((Vindicator) e.getEntity()).setHealth(plugin.getConfig().getInt("vendrick-health") * 0.25);
                for (Bar bar : plugin.bars){
                    bar.fill(0.25);
                }
            }
        }
    }

    @EventHandler
    public void onSpiritDeath(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("ven_spirit_flame") ||
                e.getEntity().hasMetadata("ven_spirit_tide") ||
                e.getEntity().hasMetadata("ven_spirit_storm")){
            e.getDrops().clear();
            plugin.vendrick.stopAttack();
            plugin.runeHandler.setPaused(false);
        }
    }

    private void spawnSpirit(Location loc, String type, Color color){
        plugin.vendrick.startAttack(6);
        plugin.runeHandler.setPaused(true);
        new BukkitRunnable(){
            @Override
            public void run() {
                SpiritSpawnAnim anim = new SpiritSpawnAnim(loc, color, VendrickBossFight.plugin);
            }
        }.runTaskLater(VendrickBossFight.plugin, 20);
        new BukkitRunnable(){
            @Override
            public void run() {
                DistSpirit spirit = new DistSpirit(loc, type);
                spirit.spawnMob();
                Bukkit.getPluginManager().callEvent(new VendrickSpiritSpawnEvent(type, plugin.fighting, spirit.getSpirit()));
            }
        }.runTaskLater(VendrickBossFight.plugin, 20 * 7);
    }

    // Dark rune related Events

    @EventHandler
    public void vendrickLifeSteal(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)) {return;}
        if (!(e.getDamager() instanceof Vindicator)) {return;}
        if (!e.getDamager().hasMetadata("Vendrick")) {return;}
        if (!e.getDamager().getScoreboardTags().contains("venDark")) {return;}
        if (!plugin.runeHandler.getActiveRune().equalsIgnoreCase("Life Steal")) {return;}

        LivingEntity vendrick = (LivingEntity) e.getDamager();
        double damage = e.getDamage();

        try {
            vendrick.setHealth(vendrick.getHealth() + damage * 2);
        } catch (Exception ignored){}

        double rawPercent = ((Vindicator) e.getDamager()).getHealth() / VendrickBossFight.plugin.getConfig().getInt("vendrick-health");
        double percent = Math.round(rawPercent * 100.0) / 100.0;

        if (percent > 1){
            percent = 1;
        }

        for (Bar bar : plugin.bars){
            bar.fill(percent);
        }
    }

    @EventHandler
    public void vendrickWitherSoul(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Vindicator)) {return;}
        if (!(e.getDamager() instanceof Player)) {return;}
        if (!e.getEntity().hasMetadata("Vendrick")) {return;}
        if (!e.getEntity().getScoreboardTags().contains("venDark")) {return;}
        if (!plugin.runeHandler.getActiveRune().equalsIgnoreCase("wither soul")) {return;}

        double chance = Math.random();

        if (chance >= 0.5){
            Location location = e.getEntity().getLocation();
            for (int i = 0 ; i < 2; i++){
                Entity wither = location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
                if (i == 0){
                    wither.setVelocity(new Vector(0.3, 0.6,0));
                } else {
                    wither.setVelocity(new Vector(0, 0.6,0.3));
                }
                wither.setMetadata("venWither", new FixedMetadataValue(plugin, "venwither"));
            }
            location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onVenWitherDeath(EntityDeathEvent e){
        if (e.getEntity().hasMetadata("venWither")){
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void vendrickWitherSoulHitPlayer(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)) {return;}
        if (!(e.getDamager() instanceof Vindicator)) {return;}
        if (!e.getDamager().hasMetadata("Vendrick")) {return;}
        if (!e.getDamager().getScoreboardTags().contains("venDark")) {return;}
        if (!plugin.runeHandler.getActiveRune().equalsIgnoreCase("wither soul")) {return;}

        Player player = (Player) e.getEntity();

        player.addPotionEffect(PotionEffectType.WITHER.createEffect(40, 1));
        player.addPotionEffect(PotionEffectType.SLOW.createEffect(40, 0));
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void vendrickBallisticHitPlayer(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)) {return;}
        if (!(e.getDamager() instanceof Vindicator)) {return;}
        if (!e.getDamager().hasMetadata("Vendrick")) {return;}
        if (!e.getDamager().getScoreboardTags().contains("venDark")) {return;}
        if (!plugin.runeHandler.getActiveRune().equalsIgnoreCase("ballistic")) {return;}

        e.setDamage(e.getDamage() * 1.5);
        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1.0f, 0.9f);
    }

}
