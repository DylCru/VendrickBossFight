package me.crazyrain.vendrickbossfight.Commands;

import me.crazyrain.vendrickbossfight.CustomEvents.VendrickSpiritSpawnEvent;
import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.attacks.PortalWraiths;
import me.crazyrain.vendrickbossfight.distortions.dark.spirits.DistSpirit;
import me.crazyrain.vendrickbossfight.distortions.dark.spirits.TsunamiCountdown;
import me.crazyrain.vendrickbossfight.distortions.tidal.BubbleBomb;
import me.crazyrain.vendrickbossfight.functionality.ItemManager;
import me.crazyrain.vendrickbossfight.functionality.Lang;
import me.crazyrain.vendrickbossfight.functionality.LootHandler;
import me.crazyrain.vendrickbossfight.inventories.ClickEvents;
import me.crazyrain.vendrickbossfight.inventories.VenInventory;
import me.crazyrain.vendrickbossfight.mobs.Growth;
import me.crazyrain.vendrickbossfight.mobs.PigBomb;
import me.crazyrain.vendrickbossfight.mobs.Wraith;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Commands implements CommandExecutor {
    VendrickBossFight plugin;

    public Commands(VendrickBossFight plugin){
        this.plugin = plugin;
    }
    String venPrefix = ChatColor.AQUA + "[VEN]";

    boolean endExplodeActive = true;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            plugin.getLogger().log(Level.WARNING, "Only players can use commands!");
            return true;
        }
        Player player = (Player) sender;

        if (cmd.getLabel().equalsIgnoreCase("ven")){
            if (player.isOp()){
                if (args.length > 0){
                    if (args[0].equalsIgnoreCase("help")){
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Vendrick commands");
                        player.sendMessage(ChatColor.AQUA + "/ven help - Shows this message");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven items / i - Shows the item menu");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven reload / rl - Reloads the plugin's config");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven merchant [e/d/m] - Allows you to spawn an Eternal Merchant");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven mremove - Removes all merchants in a 5 block radius");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven clear - Removes any vendrick related entities in a 10 block radius. Can only be used while not in a fight. Does not remove merchants.");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven summon [wraith/pig/growth] - Summon any plugin mob. Can only be used while not in a fight.");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.AQUA + "/ven refresh - Hold a Vendrick item to update it to it's latest version");

                    } else if (args[0].equalsIgnoreCase("items") || args[0].equalsIgnoreCase("i")){
                        VenInventory inv = new VenInventory("Vendrick Items: All", ItemManager.allItems, 1, false);
                        player.openInventory(inv.getInventory());

                    } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
                        player.sendMessage(venPrefix + ChatColor.GREEN + " The config has been reloaded!");
                        plugin.reloadConfig();
                        plugin.initLocations();
                        player.sendMessage(venPrefix + ChatColor.GRAY + " " + plugin.configSpawnLocs.size() + " Custom spawning locations successfully initialised");
                        plugin.lootHandler.refreshChances();

                    } else if(args[0].equalsIgnoreCase("merchant")){
                        if (args.length < 2) {
                            player.sendMessage(venPrefix + ChatColor.RED + " /ven merchant [e/d/m]");
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("e")){
                            player.getInventory().addItem(ItemManager.tradeLoc);
                            player.sendMessage(ChatColor.GREEN + "You have been given the Merchant Placer. Right click any block to place down an Eternal Merchant");
                        } else if (args[1].equalsIgnoreCase("d")){
                            player.getInventory().addItem(ItemManager.DtradeLoc);
                            player.sendMessage(ChatColor.GREEN + "You have been given the Merchant Placer. Right click any block to place down a Distorted Merchant");
                        } else if (args[1].equalsIgnoreCase("m")){
                            player.getInventory().addItem(ItemManager.MtradeLoc);
                            player.sendMessage(ChatColor.GREEN + "You have been given the Merchant Placer. Right click any block to place down a Material Merchant");
                        }
                        else {
                            player.sendMessage(venPrefix + ChatColor.RED + " /ven merchant [e/d/m]");
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("mremove")){
                        int removeCount = 0;
                        for (Entity entity : player.getNearbyEntities(5,5,5)){
                            if (entity.getScoreboardTags().contains("VenMerchant")){
                                entity.remove();
                                if (!entity.getType().equals(EntityType.ARMOR_STAND)){
                                    removeCount++;
                                }
                            }
                        }
                        if (removeCount == 0){
                            player.sendMessage(ChatColor.RED + "No Merchants were removed. Stand closer to them and try again.");
                        } else if (removeCount > 1){
                            player.sendMessage(ChatColor.GREEN + "Removed " + removeCount + " merchants!");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "Removed " + removeCount + " merchant!");
                        }
                    } else if (args[0].equalsIgnoreCase("clear")){
                        if (!plugin.venSpawned){
                            int enCount = 0;
                            for (Entity e : player.getNearbyEntities(10,10,10)){
                                if (e.hasMetadata("PigBomb")
                                        || e.hasMetadata("Portal")
                                        || e.hasMetadata("Wraith")
                                        || e.hasMetadata("Growth")
                                        || e.hasMetadata("SquidShield")
                                        || e.hasMetadata("Vendrick")
                                        || e.hasMetadata("venPulse")
                                        || e.hasMetadata("venCollect")
                                        || e.hasMetadata("venBall")
                                        || e.hasMetadata("Ven_Rune_Stand")
                                        || e.hasMetadata("spirit_flame_bomb")
                                        || e.getScoreboardTags().contains("venSpirit")
                                        || e.getCustomName() != null && e.getCustomName().contains("Portal Health")
                                        || e.getType().equals(EntityType.GUARDIAN)
                                        || e.getType().equals(EntityType.ELDER_GUARDIAN)
                                        || e.getType().equals(EntityType.DROWNED)
                                        || e.hasMetadata("VenBubble")
                                        || e.hasMetadata("ven-e-king"))
                                {
                                    e.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation(), 10);
                                    e.remove();
                                    enCount++;
                                }
                            }
                            player.sendMessage(venPrefix + ChatColor.GREEN + " " + enCount + " Entities removed!");
                        } else {
                            player.sendMessage(venPrefix + ChatColor.RED + " Entities cannot be cleared while the fight is still in progress.");
                        }

                    } else if (args[0].equalsIgnoreCase("add")){
                        if (args.length == 2){
                            int x = player.getLocation().getBlockX();
                            int y = player.getLocation().getBlockY();
                            int z = player.getLocation().getBlockZ();
                            int[] coords = {x,y,z};
                            String key = args[1];
                            plugin.getConfig().set("spawn-locations." + key, coords);
                            plugin.saveConfig();
                            player.sendMessage(venPrefix + ChatColor.GREEN + " Added new location: " + key + " to the config!");
                        }
                    } else if (args[0].equalsIgnoreCase("summon")) {
                        if (!plugin.venSpawned) {
                        if (args.length >= 2) {
                            String mob = args[1].toUpperCase();
                            switch (mob) {
                                case "PIG":
                                    PigBomb bomb = new PigBomb(player.getLocation(), 0, 0, plugin);
                                    break;
                                case "WRAITH":
                                    Wraith wraith = new Wraith(player.getLocation(), plugin);
                                    break;
                                case "GROWTH":
                                    Growth growth = new Growth(player.getLocation(), plugin);
                                    break;
                                default:
                                    player.sendMessage(venPrefix + ChatColor.RED + " Invalid entity: " + mob);
                                    break;
                            }
                        } else {
                            player.sendMessage(venPrefix + ChatColor.RED + " /ven summon [pig/wraith/growth]");
                        }
                    } else {
                            player.sendMessage(venPrefix + ChatColor.RED + " This command cannot be used while a fight is in progress!");
                        }
                    } else if (args[0].equalsIgnoreCase("refresh")){
                        if (player.getEquipment().getItemInMainHand().getType().equals(Material.AIR) || !player.getEquipment().getItemInMainHand().hasItemMeta()){
                            player.sendMessage(venPrefix + ChatColor.RED + " You need to hold an item!");
                        } else {
                            ItemStack[] item = ClickEvents.findItems(player.getEquipment().getItemInMainHand().getItemMeta().getDisplayName());
                            if (item == null){
                                player.sendMessage(venPrefix + ChatColor.RED + " You need to hold a Vendrick Item");
                            } else {
                                int amount = player.getEquipment().getItemInMainHand().getAmount();
                                player.getEquipment().setItemInMainHand(item[0]);
                                player.getEquipment().getItemInMainHand().setAmount(amount);
                                player.updateInventory();
                                player.sendMessage(venPrefix + ChatColor.GREEN + " Refreshed your " + item[0].getItemMeta().getDisplayName());
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("test")) {
                        ArrayList<UUID> players = new ArrayList<>();
                        players.add(player.getUniqueId());
                        BubbleBomb bubbleBomb = new BubbleBomb(player.getLocation(), plugin, players);
                        bubbleBomb.startAttack();
                    } else {
                        player.sendMessage(venPrefix + ChatColor.RED + " /ven [help] [items] [reload] [merchant] [mremove] [refresh]");
                    }
                } else {
                    player.sendMessage(venPrefix + ChatColor.RED + " /ven [help] [items] [reload] [merchant] [mremove] [refresh]");
                }
            } else {
                player.sendMessage(venPrefix + " " + Lang.NOPERMS.toString());
            }

        }

        return true;
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
}
