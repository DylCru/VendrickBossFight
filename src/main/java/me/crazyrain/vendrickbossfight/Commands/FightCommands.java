package me.crazyrain.vendrickbossfight.Commands;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickFightStopEvent;
import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSkipSpiritEvent;
import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSpiritSpawnEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.attacks.PigBombs;
import me.crazyrain.vendrickbossfight.attacks.PortalWraiths;
import me.crazyrain.vendrickbossfight.distortions.dark.DarkVendrick;
import me.crazyrain.vendrickbossfight.distortions.flaming.FlamingVendrick;
import me.crazyrain.vendrickbossfight.distortions.stormy.Hurricane;
import me.crazyrain.vendrickbossfight.distortions.tidal.TidalVendrick;
import me.crazyrain.vendrickbossfight.distortions.stormy.StormyVendrick;
import me.crazyrain.vendrickbossfight.functionality.Bar;
import me.crazyrain.vendrickbossfight.functionality.Events;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import me.crazyrain.vendrickbossfight.npcs.Vendrick;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class FightCommands implements CommandExecutor {
    VendrickBossFight plugin;

    public FightCommands(VendrickBossFight plugin){
        this.plugin = plugin;
    }
    String venPrefix = ChatColor.AQUA + "[VEN]";

    String[] distortions = {"n","f", "t", "s", "d"};
    HashMap<String, Color> circleColours = new HashMap<>(){{
        put("n", Color.RED);
        put("f", Color.ORANGE);
        put("t", Color.BLUE);
        put("s", Color.YELLOW);
        put("d", Color.BLACK);
    }};



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            plugin.getLogger().log(Level.WARNING, "Only players can use commands!");
            return true;
        }

        Player player = (Player) sender;

        //phases:
        //1: wraiths
        //2: pig bombs
        //3: growths

        if (cmd.getLabel().equalsIgnoreCase("venfight")){
            if (!player.isOp()){
                player.sendMessage(venPrefix + ChatColor.RED + " Only OP's can use this command.");
                return true;
            }
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("help")){
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Vendrick " + ChatColor.BOLD + "FIGHT" + ChatColor.AQUA + " commands");
                    player.sendMessage(ChatColor.AQUA + "/venfight help - Shows this message");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + "/venfight skip / sk - Skips Vendrick's current attack. Attack must be fully ready before skipping");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + "/venfight stop / st - Stops the current fight. A fight can only be stopped when Vendrick is not attacking.");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + "/venfight spawn / sp [Custom Location] [Distortion] - Allows you to start a Vendrick fight without use of a star");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + "Custom Location - A custom location in the config, put the name of the location here");
                    player.sendMessage(ChatColor.AQUA + "Distortion - n: No distortion, f: Flaming, t: Tidal, s: Stormy, d: Dark");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + "/venfight storm [damage/size] [amount] - Allows you to set the size or damage of Stormy Vendrick's storm.");
                    player.sendMessage("");

                }
                else if (args[0].equalsIgnoreCase("skip") || args[0].equalsIgnoreCase("sk")){
                    if (plugin.venSpawned){
                        if (!plugin.vendrick.getSkipable()){
                            player.sendMessage(venPrefix + ChatColor.RED + " Wait for the attack to be ready before skipping!");
                            return true;
                        }
                        switch (plugin.vendrick.getPhase()){
                            case 0:
                                player.sendMessage(venPrefix + ChatColor.RED + " Vendrick isn't attacking currently.");
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                                return true;
                            case 1:
                                PortalWraiths wraiths = new PortalWraiths(plugin);
                                wraiths.skipAttack();
                                break;
                            case 2:
                                PigBombs bombs = new PigBombs(plugin);
                                bombs.skipAttack();
                                break;
                            case 3:
                                for (Entity e : plugin.vendrick.getVendrick().getNearbyEntities(30,30,30)){
                                    if (e.hasMetadata("Growth")){
                                        e.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation(), 3);
                                        e.remove();
                                    }
                                }
                                plugin.vendrick.stopAttack();
                                break;
                            case 5:
                                ((TidalVendrick) plugin.vendrick).getBubbleBomb().stopAttack();
                                plugin.vendrick.stopAttack();
                                break;
                            case 6:
                                DarkVendrick vendrick = (DarkVendrick) plugin.vendrick;
                                Bukkit.getPluginManager().callEvent(new VendrickSkipSpiritEvent(vendrick.getSpirit().getMetadata()));
                                plugin.vendrick.stopAttack();
                                plugin.runeHandler.setPaused(false);
                                break;
                            }
                        for (UUID id : plugin.fighting){
                            Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.GREEN + " Skipped Vendrick's attack!");
                        }
                        player.sendMessage(ChatColor.GREEN + "Attack skipped!");
                        } else {
                            player.sendMessage(venPrefix + ChatColor.RED + " Vendrick hasn't spawned yet.");
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    }
                }
                else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("st")){
                    if (plugin.venSpawned){
                        if (plugin.vendrick.getPhase() == 0){
                            for (Bar bar : plugin.bars){
                                bar.remove();
                            }
                            for (UUID id : plugin.fighting){
                                Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.RED + " Stopped the fight!");
                            }
                            plugin.vendrick.getVendrick().getWorld().spawnParticle(Particle.SPELL_WITCH, plugin.vendrick.getVendrick().getLocation(), 10);
                            plugin.vendrick.getVendrick().remove();
                            plugin.fighting.clear();
                            plugin.pInv.clear();
                            Bukkit.getPluginManager().callEvent(new VendrickFightStopEvent(null, null, null, plugin.vendrick.getDistortion(), plugin.vendrick.getDifficulty()));
                            plugin.venSpawned = false;

                            try {
                                plugin.runeHandler.clearStand();
                                plugin.runeHandler.setActive(false);
                                plugin.countdown.removeBars();
                            } catch (Exception ignored) {}

                        } else {
                            player.sendMessage(venPrefix + ChatColor.RED + "Wait for the current attack to be over before ending the fight");
                        }
                    } else {
                        player.sendMessage(venPrefix + ChatColor.RED + " Vendrick hasn't spawned yet.");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    }
                }
                else if (args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("sp")){
                    if (args.length == 3){ //Using custom location from config
                        if (plugin.venSpawned){
                            player.sendMessage(venPrefix + ChatColor.RED + " Vendrick is already spawned!");
                            return true;
                        }

                        String key = args[1];
                        List<Integer> coords;
                        Location spawnLoc;
                        coords = plugin.getConfig().getIntegerList("spawn-locations." + key);

                        try {
                            spawnLoc = new Location(player.getWorld(), coords.get(0), coords.get(1), coords.get(2));
                        } catch (Exception e){
                            player.sendMessage(venPrefix + ChatColor.RED + " Couldn't get the coordinates entered in " + key + "!");
                            return true;
                        }

                        if (!checkArg(args[2])){
                            player.sendMessage(venPrefix + ChatColor.RED + " " + args[2] + " is not a valid distortion. Refer to /venfight help for help with arguments.");
                            return true;
                        }

                        player.sendMessage(venPrefix + ChatColor.GREEN + " Your vendrick fight will begin in 5 seconds. Players within a 5 block radius of the spawn location will be entered into the fight.");
                        Events.makeCircle(spawnLoc, 6f, circleColours.get(args[2]));

                        new BukkitRunnable(){
                            int pCount = 0;
                            @Override
                            public void run() {
                                for (Entity e : spawnLoc.getWorld().getNearbyEntities(spawnLoc,5.5,6,5.5)){
                                    if (e instanceof Player){
                                        pCount++;
                                        plugin.fighting.add(e.getUniqueId());
                                    }
                                }
                                if (pCount > 0){
                                    switch (args[2]){
                                        case "f":
                                            plugin.vendrick = new FlamingVendrick(plugin.fighting, spawnLoc, plugin);
                                            break;
                                        case "t":
                                            plugin.vendrick = new TidalVendrick(plugin.fighting, spawnLoc, plugin);
                                            plugin.squids = 4;
                                            break;
                                        case "s":
                                            plugin.vendrick = new StormyVendrick(plugin.fighting, spawnLoc, plugin);
                                            break;
                                        case "d":
                                            plugin.vendrick = new DarkVendrick(plugin.fighting, spawnLoc, plugin);
                                            break;
                                        case "n":
                                            plugin.vendrick = new Vendrick(plugin.fighting, spawnLoc, plugin);
                                    }

                                    for (UUID player : plugin.fighting){
                                        assert Bukkit.getPlayer(player) != null;
                                        Bukkit.getPlayer(player).sendTitle(ChatColor.DARK_RED + "Vendrick", ChatColor.RED + "The eternal guardian", 10, 70, 20);
                                        Bukkit.getPlayer(player).sendMessage(Lang.CURSE.toString());
                                        Bukkit.getPlayer(player).playSound(Bukkit.getPlayer(player).getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 0.8f);
                                        Bukkit.getPlayer(player).playSound(Bukkit.getPlayer(player).getLocation(), Sound.ENTITY_WITHER_SPAWN, 10, 0.8f);

                                        Bar bar = new Bar(Bukkit.getPlayer(player), ChatColor.DARK_RED + ""  + ChatColor.BOLD + "Vendrick", BarColor.RED);
                                        bar.add();
                                        plugin.bars.add(bar);

                                    }

                                    plugin.vendrick.spawnBoss();

                                    if (args[2].equalsIgnoreCase("s")){
                                        Hurricane hurricane = new Hurricane(plugin.vendrick);
                                        plugin.hurricane = hurricane;
                                    }

                                    plugin.venSpawned = true;
                                } else {
                                    player.sendMessage(venPrefix + ChatColor.RED + " No players could be found! Fight aborted.");
                                    cancel();
                                }

                            }
                        }.runTaskLater(plugin, 20 * 5);
                    } else {
                        player.sendMessage(venPrefix + ChatColor.RED + " /venfight spawn [location] [distortion]");
                    }
                }
                else if (args[0].equalsIgnoreCase("storm")){
                    if (args.length == 3){
                        if (plugin.venSpawned){
                            if (plugin.vendrick.getDifficulty() == 4){
                                int input;
                                try {
                                    input = Integer.parseInt(args[2]);
                                } catch (Exception e){
                                    player.sendMessage(venPrefix + ChatColor.RED + " " + args[2] + " is not a valid input. Enter a number!");
                                    return true;
                                }
                                switch (args[1]){
                                    case "damage":
                                        plugin.hurricane.setDamage(input);
                                        player.sendMessage(venPrefix + ChatColor.GREEN + " Done! Storm damage set to " + plugin.hurricane.getDamage());
                                        break;
                                    case "size":
                                        plugin.hurricane.setRadius(input);
                                        player.sendMessage(venPrefix + ChatColor.GREEN + " Done! Storm size set to " + plugin.hurricane.getRadius());
                                        break;
                                    default:
                                        player.sendMessage(venPrefix + ChatColor.RED + " /venfight storm [damage/size] [amount]");
                                        return true;
                                }
                            } else {
                                player.sendMessage(venPrefix + ChatColor.RED + " Stormy Vendrick must be spawned for you to use this command!");
                            }
                        } else {
                            player.sendMessage(venPrefix + ChatColor.RED + " Vendrick hasn't spawned yet.");
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        }
                    } else {
                        player.sendMessage(venPrefix + ChatColor.RED + " /venfight storm [damage/size] [amount]");
                        return true;
                    }
                }
                else {
                    player.sendMessage(venPrefix + ChatColor.RED + " /venfight [skip][stop][spawn][storm]");
                }
            } else {
                player.sendMessage(venPrefix + ChatColor.RED + " /venfight [skip][stop][spawn][storm]");
            }
        }


        return true;
    }

    public boolean checkArg(String arg){
        for (String d : distortions){
            if (Objects.equals(d, arg)) {
                return true;
            }
        }
        return false;
    }
}
