package me.crazyrain.vendrickbossfight.distortions.dark.spirits;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class DistSpirit {

    Location spawnLoc;
    String distortionType;
    Material head;
    String name;
    String metadata;
    LivingEntity spirit;

    public DistSpirit(Location spawnLoc, String distortionType) {
        this.spawnLoc = spawnLoc;
        this.distortionType = distortionType;
        switch (distortionType) {
            case "flame":
                head = Material.MAGMA_BLOCK;
                name = ChatColor.RED + "" + ChatColor.BOLD + "Flaming Spirit";
                metadata = "ven_spirit_flame";
                break;
            case "tide":
                head = Material.WET_SPONGE;
                name = ChatColor.BLUE + "" + ChatColor.BOLD + "Tidal Spirit";
                metadata = "ven_spirit_tide";
                break;
            case "storm":
                head = Material.CRYING_OBSIDIAN;
                name = ChatColor.YELLOW + "" + ChatColor.BOLD + "Stormy Spirit";
                metadata = "ven_spirit_storm";
                break;
        }

    }

    public LivingEntity getSpirit(){
        return spirit;
    }

    public void spawnMob(){
        spirit = (LivingEntity) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
        spirit.getEquipment().setHelmet(new ItemStack(head));
        spirit.setCustomName(name);
        spirit.setCustomNameVisible(true);

        int health;
        if (VendrickBossFight.plugin.getConfig().getInt("spirit-health") <= 2048){
            health = VendrickBossFight.plugin.getConfig().getInt("spirit-health");
        } else {
            health = 2048;
        }

        AttributeModifier incHealth = new AttributeModifier(UUID.randomUUID(), name, health, AttributeModifier.Operation.ADD_NUMBER); // Changeable in config
        spirit.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(incHealth);
        spirit.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100000, 3));
        spirit.setHealth(health);
        spirit.setMetadata(metadata, new FixedMetadataValue(VendrickBossFight.plugin, metadata));
        spirit.addScoreboardTag("venSpirit");

        ItemStack soulBlade = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = soulBlade.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Soul Blade");
        meta.addEnchant(Enchantment.DAMAGE_ALL, 9, true);
        soulBlade.setItemMeta(meta);
        spirit.getEquipment().setItemInMainHand(soulBlade);

        spawnLoc.getWorld().playSound(spawnLoc, Sound.ENTITY_WITHER_DEATH, 1.0f, 1.5f);
    }
}
