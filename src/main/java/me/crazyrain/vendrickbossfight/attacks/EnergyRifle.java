package me.crazyrain.vendrickbossfight.attacks;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import me.crazyrain.vendrickbossfight.items.ItemManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EnergyRifle implements Listener {

    VendrickBossFight plugin;
    public EnergyRifle(VendrickBossFight plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onRifleRightClick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();
        if (!item.hasItemMeta()){
            return;
        }
        if (!item.getItemMeta().hasDisplayName()){
            return;
        }

        if (e.getPlayer().getEquipment().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ItemManager.energyRifle.getItemMeta().getDisplayName())) {

            Player player = e.getPlayer();

            if (VendrickBossFight.plugin.getConfig().getStringList("disabled-items").contains(item.getItemMeta().getDisplayName())){
                player.sendMessage(ChatColor.RED + "This item is currently disabled!");
                return;
            }

            List<EntityType> safeMobs = new ArrayList<>();
            for (String s : plugin.getConfig().getStringList("rifle-safe")){
                safeMobs.add(EntityType.valueOf(s));
            }

            Vector direction = player.getLocation().getDirection().normalize();

            if (player.getFoodLevel() - plugin.getConfig().getInt("rifle-cost") >= 0) {
                if (!player.getGameMode().equals(GameMode.CREATIVE)){
                    player.setFoodLevel(player.getFoodLevel() - plugin.getConfig().getInt("rifle-cost"));
                }
                String message = ChatColor.GREEN + "Used " + ChatColor.GOLD + "" + ChatColor.BOLD + "Pulse Shot";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            } else {
                String message = ChatColor.RED + "" + ChatColor.BOLD + "NOT ENOUGH HUNGER";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                return;
            }


            ArmorStand pulse = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation().add(direction), EntityType.ARMOR_STAND);
            pulse.setVisible(false);
            pulse.setSmall(true);
            pulse.getEquipment().setHelmet(new ItemStack(Material.BEACON));
            pulse.setMetadata("venPulse", new FixedMetadataValue(plugin, "venpulse"));
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

                    for (Entity e : pulse.getNearbyEntities(0.2, 0.2, 0.2)) {
                        if (e instanceof Player || e instanceof ArmorStand || e instanceof ExperienceOrb) {
                            continue;
                        }

                        try {
                            if (safeMobs.contains(e.getType())){
                                continue;
                            }

                            LivingEntity en = (LivingEntity) e;
                            int damage = plugin.getConfig().getInt("rifle-damage");
                            en.damage(damage, player);
                            en.getWorld().strikeLightningEffect(en.getLocation());
                            pulse.remove();
                            cancel();
                        } catch (ClassCastException ignored){}
                    }

                    count++;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    @EventHandler
    public void onGrassTill(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if (player.getEquipment().getItemInMainHand().equals(ItemManager.energyRifle)
                || player.getEquipment().getItemInMainHand().equals(ItemManager.energyRifle)){
            e.setCancelled(true);
        }
    }
}
