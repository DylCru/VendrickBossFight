package me.crazyrain.vendrickbossfight.functionality;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStartEvent;
import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStopEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.attacks.*;
import me.crazyrain.vendrickbossfight.distortions.dark.DarkVendrick;
import me.crazyrain.vendrickbossfight.distortions.flaming.FlamingVendrick;
import me.crazyrain.vendrickbossfight.distortions.tidal.BubbleBomb;
import me.crazyrain.vendrickbossfight.distortions.tidal.TidalVendrick;
import me.crazyrain.vendrickbossfight.distortions.stormy.Hurricane;
import me.crazyrain.vendrickbossfight.distortions.stormy.StormyVendrick;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.List;

public class Events implements Listener {

    VendrickBossFight plugin;

    public Events(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    public boolean bossDead = false;
    public boolean spawnedHoard = false;
    public boolean pigsSpawned = false;
    public boolean enraged = false;
    public boolean lost = false;
    public boolean starDropped = false;

    double percent;

    public boolean attacking = false;


    public static List<UUID> plost = new ArrayList<>();


    @EventHandler
    public void spawnBoss(ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getItemMeta() != null) {
            if (e.getEntity().getThrower() == null) {
                return;
            }
            if (e.getEntity().getItemStack().getItemMeta().getEnchants().containsKey(Enchantment.ARROW_INFINITE) && e.getEntity().getItemStack().getType().equals(Material.NETHER_STAR)) {
                Player player = Bukkit.getPlayer(e.getEntity().getThrower());
                starDropped = true;
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (starDropped){
                            if (plugin.venSpawned){
                                player.sendMessage(Lang.FORCE.toString());
                                player.sendMessage(Lang.ENOUGH.toString());
                                return;
                            }

                            if (plugin.getConfig().getBoolean("disable-boss")){
                                return;
                            }

                            Location spawnLoc = e.getEntity().getLocation();

                            if (plugin.getConfig().getBoolean("use-locations")){
                                int foundCount = 0;
                                int x = spawnLoc.getBlockX();
                                int y = spawnLoc.getBlockY();
                                int z = spawnLoc.getBlockZ();
                                for (Location loc : plugin.configSpawnLocs){
                                    foundCount = 0;
                                    if (x == loc.getBlockX()){
                                        foundCount++;
                                    }
                                    if (y == loc.getBlockY()){
                                        foundCount++;
                                    }
                                    if (z == loc.getBlockZ()){
                                        foundCount++;
                                    }
                                }
                                if (foundCount != 3) {
                                    return;
                                }
                            }

                            player.sendMessage(Lang.AWAKE.toString());
                            player.sendMessage(Lang.CIRCLE.toString());
                            player.sendMessage(Lang.STAR.toString());
                            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.2f);

                            for (Entity e : spawnLoc.getWorld().getNearbyEntities(spawnLoc, 45, 45 ,45)){
                                if (e instanceof  Player){
                                    Player p = ((Player) e).getPlayer();
                                    if (p == player){
                                        continue;
                                    }
                                    p.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.2f);
                                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Vendrick is spawning nearby!");
                                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Head to X:" + spawnLoc.getBlockX()
                                            + ", Y:" + spawnLoc.getBlockY() + ", X:" + spawnLoc.getBlockZ() + "!");

                                }
                            }


                            if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(FLAMING)")){
                                makeCircle(e.getEntity().getLocation(), 6f, Color.ORANGE);
                            } else if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(TIDAL)")) {
                                makeCircle(e.getEntity().getLocation(), 6f, Color.BLUE);
                            } else if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(STORMY)")){
                                makeCircle(e.getEntity().getLocation(), 6f, Color.YELLOW);
                            } else if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(DARK)")){
                                makeCircle(e.getEntity().getLocation(), 6f, Color.BLACK);
                            } else {
                                makeCircle(e.getEntity().getLocation(), 6f, Color.RED);
                            }

                            new BukkitRunnable(){
                                int playerCount = 0;
                                @Override
                                public void run() {
                                    for (Entity e : spawnLoc.getWorld().getNearbyEntities(spawnLoc, 5.5,6,5.5)){
                                        if (e instanceof Player){
                                            playerCount++;
                                            plugin.fighting.add(e.getUniqueId());
                                        }
                                    }

                                    if (playerCount > 0){
                                        bossDead = false;
                                        lost = false;
                                        for (UUID player : plugin.fighting){
                                            assert Bukkit.getPlayer(player) != null;
                                            Bukkit.getPlayer(player).sendTitle(ChatColor.DARK_RED + "Vendrick", ChatColor.RED + "The eternal guardian", 10, 70, 20);
                                            Bukkit.getPlayer(player).sendMessage(Lang.CURSE.toString());
                                            Bukkit.getPlayer(player).playSound(Bukkit.getPlayer(player).getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 0.8f);
                                            Bukkit.getPlayer(player).playSound(Bukkit.getPlayer(player).getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 0.8f);

                                            Bar bar = new Bar(Bukkit.getPlayer(player),ChatColor.DARK_RED + ""  + ChatColor.BOLD + "Vendrick", BarColor.RED);
                                            bar.add();
                                            plugin.bars.add(bar);
                                        }

                                        if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(FLAMING)")){
                                            plugin.vendrick = new FlamingVendrick(plugin.fighting, spawnLoc, plugin);
                                        } else if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(TIDAL)")){
                                            plugin.vendrick = new TidalVendrick(plugin.fighting, spawnLoc, plugin);
                                            plugin.squids = 4;
                                        } else if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(STORMY)")){
                                            plugin.vendrick = new StormyVendrick(plugin.fighting, spawnLoc, plugin);
                                        } else if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(DARK)")){
                                            plugin.vendrick = new DarkVendrick(plugin.fighting, spawnLoc, plugin);
                                        } else {
                                            plugin.vendrick = new Vendrick(plugin.fighting, spawnLoc, plugin);
                                        }

                                        if (e.getEntity().getItemStack().getItemMeta().getDisplayName().contains("(STORMY)")){
                                            Hurricane hurricane = new Hurricane(plugin.vendrick);
                                            plugin.hurricane = hurricane;
                                        }

                                        plugin.vendrick.spawnBoss();
                                        Bukkit.getServer().getPluginManager().callEvent(new VendrickFightStartEvent(plugin.fighting,
                                                plugin.vendrick.getDistortion(), plugin.vendrick.getDifficulty()));

                                    } else {
                                        player.sendMessage(Lang.NOSTART.toString());
                                        plugin.venSpawned = false;
                                        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 0.3f, 0.1f);
                                    }
                                }
                            }.runTaskLater(plugin, 20 * 5);

                            e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, e.getLocation(), 1);
                            e.getEntity().remove();

                            spawnedHoard = false;
                            pigsSpawned = false;
                            enraged = false;

                            plugin.venSpawned = true;
                        }
                    }
                }.runTaskLater(plugin, 20 * 5);
            }
        }
    }

    public static void makeCircle(Location loc, Float radius, Color color){
        new BukkitRunnable(){
            Integer t = 0;
            @Override
            public void run() {
                if (t >= 100){
                    cancel();
                }


                for (int d = 0; d <= 90; d += 1) {
                    Location particleLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                    particleLoc.setX(loc.getX() + Math.cos(d) * radius);
                    particleLoc.setZ(loc.getZ() + Math.sin(d) * radius);
                    particleLoc.add(0, (d / 90.0), 0);
                    loc.getWorld().spawnParticle(Particle.REDSTONE,  particleLoc, 1, new Particle.DustOptions(color, 1));
                }
                t += 3;
            }
        }.runTaskTimer(VendrickBossFight.plugin, 0, 3);
    }

    @EventHandler
    public void stopSpawning(EntityPickupItemEvent e){
        if (e.getEntity() instanceof Player){
            if (e.getItem().getItemStack().hasItemMeta() && Objects.requireNonNull(e.getItem().getItemStack().getItemMeta()).hasDisplayName()){
                if (e.getItem().getItemStack().getItemMeta().getDisplayName().contains("Eternal Star")){
                    starDropped = false;
                }
            }

        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void showHealth(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Vindicator){
            if (e.getEntity().hasMetadata("Vendrick")){
                if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)){
                    return;
                }

                if (e.getDamager() instanceof Arrow){
                    if (((Arrow) e.getDamager()).getShooter() instanceof Player){
                        Player player = (Player) ((Arrow) e.getDamager()).getShooter();
                        UUID pId = player.getUniqueId();
                        if (!plugin.fighting.contains(pId) && ! e.getDamager().isOp()){
                            player.sendMessage(Lang.PURE.toString());
                            plugin.vendrick.getVendrick().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, plugin.vendrick.getVendrick().getLocation(), 3);
                            e.setCancelled(true);
                            return;
                        }
                    }
                }

                if (e.getDamager() instanceof Player){
                    if (!plugin.fighting.contains(e.getDamager().getUniqueId()) && ! e.getDamager().isOp()){
                        Player player = (Player) e.getDamager();
                        player.sendMessage(Lang.PURE.toString());
                        e.setCancelled(true);
                        return;
                    }
                }

                if (e.getEntity().getScoreboardTags().contains("venTide")){
                    if (!(plugin.squids == 4)){
                        double damage = e.getDamage() * (plugin.squids / 4.0);
                        e.setDamage(damage);
                    }
                }

                double rawPercent = ((Vindicator) e.getEntity()).getHealth() / plugin.getConfig().getInt("vendrick-health");
                percent = Math.round(rawPercent * 100.0) / 100.0;

                for (Bar bar : plugin.bars){
                    bar.fill(percent);
                }

                attacking = false;

                if (e.getEntity().getScoreboardTags().contains("venDark")){
                    return;
                }

                if (percent <= 0.75){
                    if (!(spawnedHoard)){
                        attacking = true;
                        spawnedHoard = true;
                        ((Vindicator) e.getEntity()).setHealth(plugin.getConfig().getInt("vendrick-health") * 0.75);
                        for (Bar bar : plugin.bars){
                            bar.fill(0.75);
                        }

                        PortalWraiths wraiths = new PortalWraiths(plugin);
                        wraiths.init(plugin.vendrick, plugin.fighting, true);
                        plugin.vendrick.startAttack(1);
                        for (UUID id : plugin.fighting){
                            Bukkit.getPlayer(id).sendMessage(Lang.PORTAL.toString());
                            AttackCharge charge = new AttackCharge(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Eternal Wraiths", Bukkit.getPlayer(id));
                        }
                    }
                }

                if (percent <= 0.50){
                    if (!(pigsSpawned)){
                        attacking = true;
                        pigsSpawned = true;
                        ((Vindicator) e.getEntity()).setHealth(plugin.getConfig().getInt("vendrick-health") * 0.50);
                        for (Bar bar : plugin.bars){
                            bar.fill(0.50);
                        }
                        if (e.getEntity().getScoreboardTags().contains("venTide")) {
                            plugin.vendrick.startAttack(5);
                            BubbleBomb bomb = new BubbleBomb(e.getEntity().getLocation(), plugin, plugin.fighting);
                            ((TidalVendrick) plugin.vendrick).setBubbleBomb(bomb);
                            bomb.startAttack();
                            for (UUID id : plugin.fighting){
                                AttackCharge charge = new AttackCharge(ChatColor.BLUE + "" + ChatColor.BOLD + "Bubble Bomb", Bukkit.getPlayer(id));
                                Bukkit.getPlayer(id).sendMessage(Lang.BUBBLE.toString());
                            }
                        } else {
                            PigBombs pigBombs = new PigBombs(plugin);
                            pigBombs.init(plugin.vendrick, plugin.fighting);
                            plugin.vendrick.startAttack(2);
                            for (UUID id : plugin.fighting){
                                AttackCharge charge = new AttackCharge(ChatColor.GOLD + "" + ChatColor.BOLD + "Pig Bombs", Bukkit.getPlayer(id));
                                Bukkit.getPlayer(id).sendMessage(Lang.BOMBS.toString());
                            }
                        }
                    }
                }

                if (percent <= 0.25){
                    if (!(enraged)){
                        attacking = true;
                        enraged = true;
                        ((Vindicator) e.getEntity()).setHealth(plugin.getConfig().getInt("vendrick-health") * 0.25);
                        for (Bar bar : plugin.bars){
                            bar.fill(0.25);
                        }
                        Enrage enrage = new Enrage(plugin);
                        enrage.init(plugin.vendrick);
                        plugin.vendrick.startAttack(0);

                        for (UUID id : plugin.fighting){
                            AttackCharge charge = new AttackCharge(ChatColor.BLACK + "" + ChatColor.BOLD + "???", Bukkit.getPlayer(id));
                        }

                        new BukkitRunnable(){

                            @Override
                            public void run() {
                                   Shatter shatter = new Shatter(plugin);
                                    new BukkitRunnable(){

                                        @Override
                                        public void run() {
                                            if (!bossDead){
                                               for (UUID id : plugin.fighting){
                                                   shatter.startShatter(Bukkit.getPlayer(id));
                                               }
                                            } else {
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0, 20 * 3);
                            }
                        }.runTaskLater(plugin, 20 * 11);
                    }
                }

            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST) // Runs event after showHealth to ensure health is set for interval attacks before this one is rolled
    public void rollForAttack(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Vindicator){
            if (e.getEntity().hasMetadata("Vendrick")){
                if (percent < 0.75 && percent > 0.15){
                    int rand = (int) (Math.random() * 8);
                    if (rand == 7){
                        int attack = (int) (Math.random() * 3);
                        switch (attack) {
                            case 0:
                                if (!(attacking) && !(percent == 0.75)) {
                                    PortalWraiths wraiths = new PortalWraiths(plugin);
                                    wraiths.init(plugin.vendrick, plugin.fighting, true);
                                    plugin.vendrick.startAttack(1);
                                    attacking = true;
                                    for (UUID id : plugin.fighting) {
                                        AttackCharge charge = new AttackCharge(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Eternal Wraiths", Bukkit.getPlayer(id));
                                        Bukkit.getPlayer(id).sendMessage(Lang.PORTAL.toString());
                                    }
                                    try{
                                        plugin.runeHandler.setPaused(true);
                                    } catch (Exception ignored){}
                                }
                                break;
                            case 1:
                                if (!(attacking) && !(percent == 0.50)) {
                                    if (e.getEntity().getScoreboardTags().contains("venTide")) {
                                        plugin.vendrick.startAttack(2);
                                        BubbleBomb bomb = new BubbleBomb(e.getEntity().getLocation(), plugin, plugin.fighting);
                                        bomb.startAttack();
                                        for (UUID id : plugin.fighting){
                                            AttackCharge charge = new AttackCharge(ChatColor.BLUE + "" + ChatColor.BOLD + "Bubble Bomb", Bukkit.getPlayer(id));
                                            Bukkit.getPlayer(id).sendMessage(Lang.BOMBS.toString());
                                        }
                                    } else {
                                        PigBombs pigBombs = new PigBombs(plugin);
                                        pigBombs.init(plugin.vendrick, plugin.fighting);
                                        plugin.vendrick.startAttack(2);
                                        for (UUID id : plugin.fighting){
                                            AttackCharge charge = new AttackCharge(ChatColor.GOLD + "" + ChatColor.BOLD + "Pig Bombs", Bukkit.getPlayer(id));
                                            Bukkit.getPlayer(id).sendMessage(Lang.BOMBS.toString());
                                        }
                                    }
                                    attacking = true;
                                    try{
                                        plugin.runeHandler.setPaused(true);
                                    } catch (Exception ignored){}
                                }
                                break;
                            case 2:
                                if (plugin.getConfig().getBoolean("DoGrowths") && percent != 0.75 && percent != 0.50) {
                                    ZombieHoard hoard = new ZombieHoard(plugin);
                                    hoard.init(plugin.vendrick);
                                    plugin.vendrick.startAttack(3);
                                    attacking = true;
                                    for (UUID id : plugin.fighting){
                                        AttackCharge charge = new AttackCharge(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "The Horde", Bukkit.getPlayer(id));
                                        Bukkit.getPlayer(id).sendMessage(Lang.GROWTHS.toString());
                                    }
                                    try{
                                        plugin.runeHandler.setPaused(true);
                                    } catch (Exception ignored){};
                                }
                            }
                         }
                    }
                 }
            }
    }

    public boolean eventCalled;
    @EventHandler
    public void onBossDeath (EntityDeathEvent e){
        if (e.getEntity().getScoreboardTags().contains("venDark")) {return;}

            if (e.getEntity() instanceof Vindicator){
                if (e.getEntity().hasMetadata("Vendrick")){
                    if (e.getEntity().getKiller() != null){

                        for (Entity en : e.getEntity().getNearbyEntities(50,50,50)){
                            if (en.hasMetadata("Wraith") || en.hasMetadata("PigBomb") || en.hasMetadata("SquidShield") || en.hasMetadata("Growth")){
                                en.remove();
                            }
                        }

                        plugin.bars.forEach(bar -> {
                            bar.fill(0.0);
                        });

                        e.getDrops().clear();

                        eventCalled = false;
                        plugin.venSpawned = false;

                        if (plugin.getConfig().getBoolean("skip-cutscene")){
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    bossDead = true;
                                    for (UUID id : plugin.fighting){
                                        victory(Bukkit.getPlayer(id));
                                    }
                                    plugin.getServer().getPluginManager().callEvent(new VendrickFightStopEvent(plugin.fighting, plugin.fighting, plost
                                            ,plugin.vendrick.getDistortion(), plugin.vendrick.getDifficulty()));
                                    lost = false;
                                    plugin.fighting.clear();
                                }
                            }.runTaskLater(plugin, 40);
                            return;
                        }

                        for (UUID id : plugin.fighting){
                            if (!plugin.getConfig().getBoolean("disable-effects")){
                                Bukkit.getPlayer(id).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(10000, 2));
                                Bukkit.getPlayer(id).addPotionEffect(PotionEffectType.SLOW.createEffect(10000, 5));
                            }
                            bossDead = true;

                            Bukkit.getPlayer(id).sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "A loud, echoing voice fills the land");

                            new BukkitRunnable(){
                                int text = 0;

                                @Override
                                public void run() {
                                    text += 1;
                                    switch (text){
                                        case 2:
                                            Bukkit.getPlayer(id).sendMessage(Lang.END1.toString());
                                            Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 20f, 0.6f);
                                            break;
                                        case 3:
                                            Bukkit.getPlayer(id).sendMessage(Lang.END2.toString());
                                            Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 20f, 0.6f);
                                            break;
                                        case 4:
                                            Bukkit.getPlayer(id).sendMessage(Lang.END3.toString());
                                            Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 20f, 0.6f);
                                            break;
                                        case 5:
                                            Bukkit.getPlayer(id).sendMessage(Lang.END4.toString());
                                        case 6:
                                            if (!eventCalled){
                                                plugin.getServer().getPluginManager().callEvent(new VendrickFightStopEvent(plugin.fighting, plugin.fighting, plost
                                                        ,plugin.vendrick.getDistortion(), plugin.vendrick.getDifficulty()));
                                                eventCalled = true;
                                            }
                                            victory(Bukkit.getPlayer(id));
                                            lost = false;
                                            plugin.fighting.clear();
                                            cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0, 20 * 4);
                        }
                    }
                }
            }
    }

    public void victory(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 0.7f);
        player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY", ChatColor.BLUE + "The eternal guardian has fallen", 10, 160, 20);
        plugin.pInv.clear();

        for (Entity e : player.getNearbyEntities(100, 100, 100)) {
            if (e.hasMetadata("Wraith") || e.hasMetadata("SquidShield")) {
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
                    if (!plugin.vendrick.getDistortion().equalsIgnoreCase("normal")){
                        p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + ChatColor.ITALIC + player.getDisplayName() + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + " has defeated " + plugin.vendrick.getDistortion() + " Vendrick!");
                    } else {
                        p.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + ChatColor.ITALIC + player.getDisplayName() + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + " has defeated Vendrick!");
                    }

                }
            } else {
                if (!plugin.vendrick.getDistortion().equalsIgnoreCase("normal")){
                    player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + ChatColor.ITALIC + player.getDisplayName() + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + " has defeated " + plugin.vendrick.getDistortion() + " Vendrick!");
                } else {
                    player.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + ChatColor.ITALIC + player.getDisplayName() + ChatColor.DARK_AQUA + "" + ChatColor.ITALIC + " has defeated Vendrick!");
                }
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


    public void lose(){
        bossDead = true;
        lost = true;

        Location deathLoc = plugin.vendrick.getVendrick().getLocation();
        for (Entity en : deathLoc.getWorld().getNearbyEntities(deathLoc, 50,50,50)){
            if (en.hasMetadata("Wraith") || en.hasMetadata("Portal") || en.getScoreboardTags().contains("venSpirit") || en.hasMetadata("SquidShield")){
                if (en.getScoreboardTags().contains("venSpirit")){
                    ((LivingEntity) en).setHealth(0);
                } else {
                    en.remove();
                }
            }
        }

        for (UUID player : plost){
            try{
                Bukkit.getPlayer(player).sendMessage(Lang.LOSE.toString());
            } catch (NullPointerException ignored){}
        }

        new BukkitRunnable(){

            @Override
            public void run() {
                plost.clear();
            }
        }.runTaskLater(plugin, 20 * 5);

        if (plugin.vendrick.getDistortion().equalsIgnoreCase("dark")) {
            plugin.runeHandler.clearStand();
            plugin.runeHandler.setActive(false);
        }

        PortalWraiths wraiths = new PortalWraiths(plugin);
        wraiths.init(plugin.vendrick, plugin.fighting, false);
        wraiths.stopSpawning();
        wraiths.stopParticles();
        plugin.vendrick.getVendrick().remove();

        plugin.venSpawned = false;
        plugin.pInv.clear();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
       if (plugin.fighting.contains(e.getPlayer().getUniqueId())){
           e.getPlayer().sendMessage(Lang.PLAYERDEATH.toString().replace("{player}", e.getPlayer().getDisplayName()));
           plugin.fighting.remove(e.getPlayer().getUniqueId());

           for (Bar bar : plugin.bars){
               if (bar.getPlayer().equals(e.getPlayer().getUniqueId())){
                   bar.remove();
               }
           }

           plost.add(e.getPlayer().getUniqueId());

           if (plugin.getConfig().getBoolean("keep-inventory")){
               e.getPlayer().getInventory().setContents(plugin.pInv.get(e.getPlayer().getUniqueId()));
               plugin.pInv.remove(e.getPlayer().getUniqueId());
           }

           if (plugin.fighting.isEmpty()){
               lose();
           }
       }

    }

    @EventHandler
    public void onPlayerLeaveMidFight(PlayerQuitEvent e){
        if (plugin.fighting.contains(e.getPlayer().getUniqueId())){
            plost.add(e.getPlayer().getUniqueId());
            plugin.fighting.remove(e.getPlayer().getUniqueId());

            if (plugin.fighting.isEmpty()){
                lose();
            }
        }
    }

    @EventHandler
    public void stopPlayerDrops(PlayerDeathEvent e){
        if (plugin.getConfig().getBoolean("keep-inventory")) {
            if (plugin.fighting.contains(e.getEntity().getUniqueId())) {
                plugin.pInv.put(e.getEntity().getUniqueId(), e.getEntity().getInventory().getContents());
                e.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void stopFallDmg(EntityDamageEvent e){
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
            if (e.getEntity().hasMetadata("Vendrick")){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVendrickTarget(EntityTargetLivingEntityEvent e){
        if (e.getEntity().hasMetadata("Vendrick")){
            if (!(e.getTarget() instanceof Player)){
                return;
            }
            if (!plugin.fighting.contains(e.getTarget().getUniqueId())){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void stopBlockPlace(BlockPlaceEvent e){
        ItemStack block = e.getPlayer().getInventory().getItemInMainHand();
        if (block.getItemMeta() == null) {
            return;
        }
        if (!NBTEditor.contains(block, NBTEditor.CUSTOM_DATA, "VEN_BLOCK")) {
            return;
        }
        if (!NBTEditor.getBoolean(block, NBTEditor.CUSTOM_DATA, "VEN_BLOCK")) {
            return;
        }
        e.setCancelled(true);

//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.eternalFragment.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.shatterSpine.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.lusciousApple.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.theCatalyst.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ItemManager.oven.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.fusionChamber.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.catalystPartA.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.catalystPartB.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }
//        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.plasmaTorch.getItemMeta().getDisplayName())){
//            e.setCancelled(true);
//        }


    }

    @EventHandler
    public void coreDrops(EntityDeathEvent e){
        if (e.getEntity().getKiller() == null){
            return;
        }

        int dropChance = (int) (Math.random() * 20);

        if (dropChance > 18){
            if (e.getEntity().getType() == EntityType.valueOf(plugin.getConfig().getString("flame-core-mob"))){
                e.getDrops().add(ItemManager.flameCore);
            }
            if (e.getEntity().getType() == EntityType.valueOf(plugin.getConfig().getString("wave-core-mob"))){
                e.getDrops().add(ItemManager.waveCore);
            }
            if (e.getEntity().getType() == EntityType.valueOf(plugin.getConfig().getString("voltaic-core-mob"))){
                e.getDrops().add(ItemManager.voltaicCore);
            }
        }
    }

    @EventHandler
    public void stopStarPlace(PlayerInteractEvent e){
        if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()){
            return;
        }

        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.volatileStar.getItemMeta().getDisplayName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventSheepDyeing(SheepDyeWoolEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = e.getPlayer().getEquipment().getItemInMainHand().clone();
        stack.setAmount(1);
        if (ArrayUtils.contains(ItemManager.allItems, stack)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventCatalystOnObsidian(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getEquipment().getItemInMainHand().equals(ItemManager.theCatalyst)) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType().equals(Material.OBSIDIAN) || e.getClickedBlock().getType().equals(Material.BEDROCK)) {
                    e.setCancelled(true);
                }
            }
        }
    }

//    @EventHandler
//    public void lootRollTest(PlayerInteractEvent e) {
//        Bukkit.broadcastMessage("\n");
//        String id = NBTEditor.getString(e.getItem(), NBTEditor.CUSTOM_DATA, "VEN_ITEM_ID");
//        Bukkit.broadcastMessage(id);
//        String type = NBTEditor.getString(e.getItem(), NBTEditor.CUSTOM_DATA, "VEN_ITEM_TYPE");
//        Bukkit.broadcastMessage(type);
//    }
}
