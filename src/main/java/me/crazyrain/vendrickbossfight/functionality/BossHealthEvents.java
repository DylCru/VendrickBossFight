package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class BossHealthEvents implements Listener {

    VendrickBossFight plugin;

    public BossHealthEvents(VendrickBossFight plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerAttackVendrick(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)){
            return;
        }
        if (!e.getEntity().getScoreboardTags().contains("DummyVendrick")) {
            return;
        }

        if (e.getDamager() instanceof Arrow){
            if (((Arrow) e.getDamager()).getShooter() instanceof Player){
                Player player = (Player) ((Arrow) e.getDamager()).getShooter();
                if (playerNotInFight(player)) return;
            }
        }

        if (e.getDamager() instanceof Player){
            if (!plugin.getFightManager().getFighting().contains(e.getDamager().getUniqueId()) && ! e.getDamager().isOp()){
                Player player = (Player) e.getDamager();
                if (playerNotInFight(player)) return;
            }
        }

        double damage = e.getDamage();
        Player player = (Player) e.getDamager();
        player.sendMessage("Damage Dealt: " + damage);

        plugin.getFightManager().damageBoss(damage);

        e.setDamage(0);

    }

    private boolean playerNotInFight(Player player) {
        UUID pId = player.getUniqueId();
        if (!plugin.getFightManager().getFighting().contains(pId) && !player.isOp()){
            player.sendMessage(Lang.PURE.toString());
            plugin.getFightManager().getVendrick().getEntity().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, plugin.getFightManager().getVendrick().getEntity().getLocation(), 3);
            return true;
        }
        return false;
    }
}
