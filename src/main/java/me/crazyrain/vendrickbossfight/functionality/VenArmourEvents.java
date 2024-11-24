package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class VenArmourEvents implements Listener {
    List<UUID> healCooldown = new ArrayList<>();
    VendrickBossFight plugin;

    public VenArmourEvents(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void chestplateFunc(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) e.getDamager();

        if (player.getEquipment().getChestplate() == null){
            return;
        }
        if (!player.getEquipment().getChestplate().hasItemMeta()){
            return;
        }
        if (!player.getEquipment().getChestplate().getItemMeta().hasDisplayName()){
            return;
        }
        if (healCooldown.contains(player.getUniqueId())){
            return;
        }
        if (player.getEquipment().getChestplate().getItemMeta().getDisplayName().equals(ItemManager.venChest.getItemMeta().getDisplayName())){
            if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()){
                return;
            }
            try{
                int heal = plugin.getConfig().getInt("chestplate-heal");
                player.setHealth(player.getHealth() + heal);
                player.spawnParticle(Particle.HEART, player.getLocation(), 6);
                healCooldown.add(player.getUniqueId());
                int timer = plugin.getConfig().getInt("chestplate-cooldown");
                cooldown(healCooldown, player.getUniqueId(), timer);
            } catch (IllegalArgumentException exception){
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                player.spawnParticle(Particle.HEART, player.getLocation(), 6);
            }
        }
    }

    public void cooldown(List<UUID> list,UUID pId, long timer){
        new BukkitRunnable(){
            @Override
            public void run() {
                list.remove(pId);
            }
        }.runTaskLater(plugin, timer);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void helmetFuncTakeLess(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player){ //Take 35% less damage
            Player player = (Player) e.getEntity();

            if (player.getEquipment().getHelmet() == null){
                return;
            }
            if (!player.getEquipment().getHelmet().hasItemMeta()){
                return;
            }
            if (!player.getEquipment().getHelmet().getItemMeta().hasDisplayName()){
                return;
            }
            if (player.getEquipment().getHelmet().getItemMeta().getDisplayName().equals(ItemManager.venHead.getItemMeta().getDisplayName())){
                if (e.getEntity() instanceof Player){
                    double reduct = plugin.getConfig().getDouble("helm-damage-reduction") / 100;
                    double damageTaken = e.getDamage() * (1 - reduct);
                    e.setDamage(damageTaken);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void helmetFuncDealMore(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){ // Deal 25% more damage
            Player player = (Player) e.getDamager();

            if (player.getEquipment().getHelmet() == null){
                return;
            }
            if (!player.getEquipment().getHelmet().hasItemMeta()){
                return;
            }
            if (!player.getEquipment().getHelmet().getItemMeta().hasDisplayName()){
                return;
            }
            if (player.getEquipment().getHelmet().getItemMeta().getDisplayName().equals(ItemManager.venHead.getItemMeta().getDisplayName())){
                if (e.getDamager() instanceof Player){
                    double bonus = plugin.getConfig().getDouble("helm-damage-bonus") / 100;
                    double damageDealt = e.getDamage() * (1 + bonus);
                    e.setDamage(damageDealt);
                }
            }
        }
    }

    List<UUID> effectCooldown = new ArrayList<>();

    @EventHandler
    public void leggingsFunc(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();

            if (player.getEquipment().getLeggings() == null){
                return;
            }
            if (!player.getEquipment().getLeggings().hasItemMeta()){
                return;
            }
            if (!player.getEquipment().getLeggings().getItemMeta().hasDisplayName()){
                return;
            }
            if (player.getEquipment().getLeggings().getItemMeta().getDisplayName().equals(ItemManager.venLegs.getItemMeta().getDisplayName())){
                double chance = Math.random();
                double req = plugin.getConfig().getDouble("legs-chance") / 100;
                if (chance <= req){
                    if (!effectCooldown.contains(player.getUniqueId())){
                        int length = plugin.getConfig().getInt("legs-potion-length");
                        int cooldown = plugin.getConfig().getInt("legs-cooldown");

                        List<PotionEffectType> effects = Arrays.asList(PotionEffectType.values());
                        int effect = (int) (Math.random() * effects.size());
                        player.addPotionEffect(effects.get(effect).createEffect(length * 20, 0));
                        player.spawnParticle(Particle.SPELL_MOB, player.getLocation().clone().add(0,1.5,0), 10);
                        effectCooldown.add(player.getUniqueId());
                        cooldown(effectCooldown, player.getUniqueId(), cooldown * 20L);
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void bootsFunc(EntityDeathEvent e){
        if (e.getEntity().getKiller() == null){
            return;
        }
        Player player = e.getEntity().getKiller();

        if (player.getEquipment().getBoots() == null){
            return;
        }
        if (!player.getEquipment().getBoots().hasItemMeta()){
            return;
        }
        if (!player.getEquipment().getBoots().getItemMeta().hasDisplayName()){
            return;
        }
        if (player.getEquipment().getBoots().getItemMeta().getDisplayName().equals(ItemManager.venBoots.getItemMeta().getDisplayName())){
            double chance = Math.random();
            double req = plugin.getConfig().getDouble("boots-double-chance") / 100;
            if (chance <= req){
                List<ItemStack> drops = e.getDrops();
                for (ItemStack drop : drops) { e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), drop); }
                e.getEntity().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, e.getEntity().getLocation().add(0,.5,0), 10);
            }
        }
    }

    @EventHandler
    public void preventBootsInCauldron(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getEquipment().getItemInMainHand().equals(ItemManager.venBoots)) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType().equals(Material.WATER_CAULDRON)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
