package me.crazyrain.vendrickbossfight.distortions.stormy;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.functionality.AttackCharge;
import me.crazyrain.vendrickbossfight.functionality.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StormyEvents implements Listener {

    VendrickBossFight plugin;

    public StormyEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onVendrickAttack(EntityDamageByEntityEvent e){
        if (e.getEntity().getScoreboardTags().contains("venStorm")){
            int chance = (int) (Math.random() * 100);

            if (chance >= 95){
                plugin.vendrick.startAttack(0);
                LightningStorm storm = new LightningStorm(plugin);
                for (UUID p : plugin.fighting){
                    AttackCharge charge = new AttackCharge(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Lightning Storm", Bukkit.getPlayer(p));
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        storm.movement();
                        storm.shootBalls(plugin.vendrick.getVendrick().getLocation().getDirection().normalize(), plugin.vendrick.getVendrick().getLocation());
                    }
                }.runTaskLater(plugin, 20 * 3);
            }
        }
    }

    @EventHandler
    public void shootBallLightning(PlayerInteractEvent e){
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            return;
        }
        Player player = e.getPlayer();
        if (!player.getEquipment().getItemInMainHand().hasItemMeta()){
            return;
        }
        if (!player.getEquipment().getItemInMainHand().getItemMeta().hasLore()){
            return;
        }
        if (e.getPlayer().getEquipment().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.ballLightning.getItemMeta().getDisplayName())){
            Vector direction = player.getLocation().getDirection().normalize();
            int amount = e.getPlayer().getEquipment().getItemInMainHand().getAmount();
            e.getPlayer().getEquipment().getItemInMainHand().setAmount(amount - 1);
            e.getPlayer().updateInventory();

            ArmorStand pulse = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation().add(direction), EntityType.ARMOR_STAND);
            pulse.setVisible(false);
            pulse.setSmall(true);
            pulse.getEquipment().setHelmet(new ItemStack(Material.BLUE_ICE));
            pulse.setMetadata("venBall", new FixedMetadataValue(plugin, "venball"));
            pulse.setVelocity(direction);


            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    if (count == 10) {
                        pulse.remove();
                        cancel();
                    }
                    pulse.setVelocity(direction);

                    for (Entity e : pulse.getNearbyEntities(0.4, 0.4, 0.4)) {
                        if (e instanceof Player || e instanceof ArmorStand) {
                            continue;
                        }
                        try {
                            LivingEntity en = (LivingEntity) e;
                            if (en.hasMetadata("Vendrick")){;
                                en.getWorld().strikeLightningEffect(en.getLocation());
                                player.sendMessage(ChatColor.DARK_GRAY + "You hit Vendrick!");
                                pulse.remove();

                                plugin.hurricane.setDamage(plugin.hurricane.getDamage() - 1);
                                plugin.hurricane.setRadius(plugin.hurricane.getRadius() - 0.5f);
                                for (UUID id : plugin.fighting) {
                                    Bukkit.getPlayer(id).sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "BOOM!" + ChatColor.GREEN +
                                            " Vendrick was hit by ball lightning! The power of his storm has been reduced.");
                                }

                                cancel();
                            }
                        } catch (ClassCastException ignored){}
                    }

                    count++;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    @EventHandler
    public void onPlayerDeathNoFight(PlayerDeathEvent e){ //Called when the player dies when the fight is not on
        if (!plugin.venSpawned){
            List<ItemStack> drops = e.getDrops();
            List<ItemStack> newDrops = List.of(removeBLFromInv(e.getEntity(), drops.toArray(new ItemStack[0])));
            e.getDrops().clear();
            e.getDrops().addAll(newDrops);
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) { //Called if players DO NOT keep their inventory when they die in the fight
        if (plugin.venSpawned){ //If the player dies during the fight
            if (plugin.fighting.contains(e.getEntity().getUniqueId())){
                if (!plugin.getConfig().getBoolean("keep-inventory")) {
                    List<ItemStack> drops = e.getDrops();
                    List<ItemStack> newDrops = List.of(removeBLFromInv(e.getEntity(), drops.toArray(new ItemStack[0])));
                    e.getDrops().clear();
                    e.getDrops().addAll(newDrops);
                } else {
                    pInv.put(e.getEntity().getUniqueId(),e.getEntity().getInventory().getContents());
                }
            }
        }
    }

    HashMap<UUID, ItemStack[]> pInv = new HashMap<>();

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerRespawn(PlayerRespawnEvent e){
        if (plugin.venSpawned){
            if (plugin.fighting.contains(e.getPlayer().getUniqueId())){
                if (plugin.getConfig().getBoolean("keep-inventory")){
                    ItemStack[] newInv = removeBLFromInv(e.getPlayer(), pInv.get(e.getPlayer().getUniqueId()));
                    e.getPlayer().getInventory().setContents(newInv);
                    e.getPlayer().updateInventory();
                    pInv.remove(e.getPlayer().getUniqueId());
                }
            }
        }
    }

    public ItemStack[] removeBLFromInv(Player player, ItemStack[] inv){
        for (int i = 0; i < inv.length; i++){
            if (inv[i] == null || !inv[i].hasItemMeta() || !inv[i].getItemMeta().hasDisplayName()){
                continue;
            }
            if (inv[i].getItemMeta().getDisplayName().contains("Ball Lightning")){
                inv[i].setAmount(0);
            }
        }
        return inv;
    }
}
