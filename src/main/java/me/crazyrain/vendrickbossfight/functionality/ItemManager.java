package me.crazyrain.vendrickbossfight.functionality;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;
import java.util.logging.Level;


public class ItemManager {

    public static VendrickBossFight plugin = VendrickBossFight.plugin;

    public static ItemStack eternalStar;
    public static ItemStack vendrickHatchet;
    public static ItemStack growthSword;
    public static ItemStack eternalFragment;
    public static ItemStack essenceOfEternity;
    public static ItemStack trueEternalHatchet;
    public static ItemStack shatterSpine;
    public static ItemStack nutrimentOfTheInfinite;
    public static ItemStack shatterStick;
    public static ItemStack tradeLoc;
    public static ItemStack DtradeLoc;
    public static ItemStack MtradeLoc;
    public static ItemStack pieCrust;
    public static ItemStack flamingStar;
    public static ItemStack tidalStar;
    public static ItemStack lusciousApple;
    public static ItemStack oven;
    public static ItemStack nutrimentU;
    public static ItemStack theCatalyst;
    public static ItemStack flameCore;
    public static ItemStack waveCore;
    public static ItemStack voltaicCore;
    public static ItemStack voidCore;
    public static ItemStack voidCoreFragment;
    public static ItemStack volatileStar;
    public static ItemStack stormStar;
    public static ItemStack ballLightning;
    public static ItemStack energyRifle;
    public static ItemStack fusionChamber;
    public static ItemStack catalystPartA;
    public static ItemStack catalystPartB;
    public static ItemStack unchargedRifle;
    public static ItemStack infinium;
    public static ItemStack enchantedInfinium;
    public static ItemStack plasmaTorch;
    public static ItemStack darkStar;
    public static ItemStack venChest;
    public static ItemStack venLegs;
    public static ItemStack venHead;
    public static ItemStack venBoots;
    public static ItemStack vendrickTalisman;


    public static ItemStack[] allItems;
    public static ItemStack[] items;
    public static ItemStack[] materials;
    public static ItemStack[] vendrick;

    public static void Init(){
        createStar();
        createHatchet();
        createGrowthSword();
        createFrag();
        createEssence();
        createTrueHatchet();
        createSpine();
        makeShatterStick();
        createNutriment();
        createTradeLoc();
        createDLoc();
        createMLoc();
        createCrust();
        createFlamingStar();
        createTidalStar();
        createApple();
        createOven();
        createNutrimentU();
        createCatalyst();
        flameCore = createCore(Lang.FLAMECORE.toString(), Lang.FLAMECORETAG.toString(), Material.ORANGE_DYE, Rarity.RARE.toString());
        waveCore = createCore(Lang.WAVECORE.toString(), Lang.WAVECORETAG.toString(), Material.LIGHT_BLUE_DYE, Rarity.RARE.toString());
        voltaicCore = createCore(Lang.VOLTAICCORE.toString(), Lang.VOLTAICCORETAG.toString(), Material.YELLOW_DYE, Rarity.EPIC.toString());
        voidCore = createCore(Lang.VOIDCORE.toString(), ChatColor.DARK_GRAY + "It's " + ChatColor.MAGIC + "the end", Material.BLACK_DYE, Rarity.EPIC.toString());
        createVolatile();
        createEarthStar();
        createBall();
        createRifle();
        createChamber();
        catalystPartA = createCatalystPart("A", Material.CHORUS_PLANT);
        catalystPartB = createCatalystPart("B", Material.CHORUS_FLOWER);
        createUncharged();
        createInfinium();
        createDarkStar();
        createEnchantedInfinium();
        createPlasmaTorch();
        createVenChest();
        createVenBoots();
        createVenHead();
        createVenLegs();
        createTalisman();
        createDarkFrag();

        allItems = new ItemStack[]{eternalStar, flamingStar, tidalStar, stormStar, darkStar, vendrickHatchet, eternalFragment, essenceOfEternity, trueEternalHatchet, shatterSpine, shatterStick, nutrimentOfTheInfinite, pieCrust,
                                    lusciousApple, oven, nutrimentU, theCatalyst, catalystPartA, catalystPartB, flameCore, waveCore, voltaicCore, voidCore, voidCoreFragment, volatileStar,
                                     unchargedRifle, fusionChamber, energyRifle, infinium, enchantedInfinium, plasmaTorch, venHead, venChest, venLegs, venBoots, vendrickTalisman};
        items = new ItemStack[]{vendrickHatchet, trueEternalHatchet, shatterStick, energyRifle, venHead, venChest, venLegs, venBoots, vendrickTalisman};
        materials = new ItemStack[]{eternalFragment, essenceOfEternity, shatterSpine, infinium, pieCrust, lusciousApple, oven, fusionChamber, enchantedInfinium, plasmaTorch};
        vendrick = new ItemStack[]{eternalStar, flamingStar, tidalStar, stormStar, darkStar, theCatalyst, catalystPartA, catalystPartB, flameCore, waveCore, voltaicCore, voidCore, voidCoreFragment, volatileStar};
    }

    private static HashMap<Enchantment, Integer> getEnchantsFromConfig(String path){
        HashMap<Enchantment, Integer> enchData = new HashMap<>();

        for (String key: plugin.getConfig().getConfigurationSection(path + ".enchants").getKeys(false)){
            List<String> data = plugin.getConfig().getStringList(path + ".enchants." + key);
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(data.get(0).toLowerCase()));
            if (ench == null){
                plugin.getLogger().log(Level.WARNING, "Couldn't find the enchant in [" + path + "." + key + "]. Did you spell the enchant correctly?");
                continue;
            }
            try {
                enchData.put(ench, Integer.valueOf(data.get(1)));
            } catch (Exception e){
                enchData.put(ench, 1);
            }

        }

        return enchData;
    }

    private static void createStar(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.STARNAME.toString());
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List <String> lore = new ArrayList<>();
        lore.add(Lang.STARTAGLINE.toString());
        lore.add("");
        lore.add(Rarity.RARE.toString());
        lore.add("");
        lore.add(Lang.STAR1.toString());
        lore.add(Lang.STAR2.toString());
        lore.add(Lang.STAR3.toString());
        lore.add(Lang.STAR4.toString());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Difficulty: ★☆☆☆☆");
        meta.setLore(lore);

        item.setItemMeta(meta);
        eternalStar = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("eternalstar"), item);
        sr.shape("eee","ede","eee");
        sr.setIngredient('e', Material.EMERALD);
        sr.setIngredient('d', Material.DIAMOND_BLOCK);
        Bukkit.getServer().addRecipe(sr);
    }

    private static void createVolatile(){
        ItemStack item = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.VSTARNAME.toString());

        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.VSTAR1.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        volatileStar = item;
    }

    private static void createFlamingStar(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.DSTARNAME.toString() + ChatColor.RED + "" + ChatColor.BOLD + " (FLAMING)");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List <String> lore = new ArrayList<>();
        lore.add(Lang.DSTARTAGLINE.toString());
        lore.add("");
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.DSTAR1.toString());
        lore.add(Lang.DSTAR2.toString());
        lore.add(Lang.DSTAR3.toString());
        lore.add(Lang.DSTAR4.toString());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Difficulty: ★★☆☆☆");
        lore.add("");
        lore.add(Lang.FLAMESTARDESC1.toString());
        lore.add(Lang.FLAMESTARDESC2.toString());
        lore.add(Lang.FLAMESTARDESC3.toString());

        meta.setLore(lore);

        item.setItemMeta(meta);
        flamingStar = item;
    }

    private static void createTidalStar(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.DSTARNAME.toString() + ChatColor.BLUE + "" + ChatColor.BOLD + " (TIDAL)");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List <String> lore = new ArrayList<>();
        lore.add(Lang.DSTARTAGLINE.toString());
        lore.add("");
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.DSTAR1.toString());
        lore.add(Lang.DSTAR2.toString());
        lore.add(Lang.DSTAR3.toString());
        lore.add(Lang.DSTAR4.toString());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Difficulty: ★★★☆☆");
        lore.add("");
        lore.add(Lang.TIDESTARDESC1.toString());
        lore.add(Lang.TIDESTARDESC2.toString());
        lore.add(Lang.TIDESTARDESC3.toString());


        meta.setLore(lore);

        item.setItemMeta(meta);
        tidalStar = item;
    }

    private static void createEarthStar(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.DSTARNAME.toString() + ChatColor.YELLOW + "" + ChatColor.BOLD + " (STORMY)");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List <String> lore = new ArrayList<>();
        lore.add(Lang.DSTARTAGLINE.toString());
        lore.add("");
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.DSTAR1.toString());
        lore.add(Lang.DSTAR2.toString());
        lore.add(Lang.DSTAR3.toString());
        lore.add(Lang.DSTAR4.toString());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Difficulty: ★★★★☆");
        lore.add("");
        lore.add(Lang.STORMSTARDESC1.toString());
        lore.add(Lang.STORMSTARDESC2.toString());
        lore.add(Lang.STORMSTARDESC3.toString());


        meta.setLore(lore);

        item.setItemMeta(meta);
        stormStar = item;
    }

    private static void createDarkStar(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.DSTARNAME.toString() + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + " (DARK)");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List <String> lore = new ArrayList<>();
        lore.add(Lang.DSTARTAGLINE.toString());
        lore.add("");
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(ChatColor.MAGIC + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        lore.add(ChatColor.MAGIC + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        lore.add(ChatColor.MAGIC + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        lore.add(ChatColor.MAGIC + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Difficulty: ★★★★★");
        lore.add("");
        lore.add(Lang.DARKSTARDESC1.toString());
        lore.add(Lang.DARKSTARDESC2.toString());
        lore.add(Lang.DARKSTARDESC3.toString());

        meta.setLore(lore);

        item.setItemMeta(meta);
        darkStar = item;
    }

    private static void createHatchet(){
         ItemStack item = new ItemStack(Material.DIAMOND_AXE);
         ItemMeta meta = item.getItemMeta();

         meta.setDisplayName(Lang.HATCHETNAME.toString());

         HashMap<Enchantment, Integer> enchData = getEnchantsFromConfig("EternalHatchet");
         for (Enchantment ench : enchData.keySet()){
             meta.addEnchant(ench, enchData.get(ench), true);
         }

         List <String> lore = new ArrayList<>();
         lore.add("");
         lore.add(Rarity.EPIC.toString());
         lore.add("");
         lore.add(Lang.HATCHET1.toString());
         lore.add(Lang.HATCHET2.toString());
         lore.add("");
         lore.add(Lang.HATCHET3.toString());
         lore.add(Lang.HATCHET4.toString());
         meta.setLore(lore);

         item.setItemMeta(meta);
         vendrickHatchet = item;
    }

    private static void createGrowthSword(){
        ItemStack item = new ItemStack(Material.ROSE_BUSH);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Vendrick's rose");
        meta.addEnchant(Enchantment.DAMAGE_ALL, 25, true);
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        growthSword = item;
    }

    private static void createFrag(){
         ItemStack item = new ItemStack(Material.LAPIS_BLOCK, 1);
         ItemMeta meta = item.getItemMeta();

         meta.setDisplayName(Lang.FRAGNAME.toString());

         List <String> lore = new ArrayList<>();
         lore.add(Rarity.RARE.toString());
         lore.add("");
         lore.add(Lang.FRAG1.toString());
         lore.add(Lang.FRAG2.toString());
         lore.add(Lang.FRAG3.toString());
         meta.setLore(lore);
         item.setItemMeta(meta);
         eternalFragment = item;
    }

    public static void createEssence(){
         ItemStack item = new ItemStack(Material.BLUE_DYE);
         ItemMeta meta = item.getItemMeta();

         meta.setDisplayName(Lang.ESSENCENAME.toString());

         List <String> lore = new ArrayList<>();
         lore.add(Rarity.RARE.toString());
         lore.add("");
         lore.add(Lang.ESSENCE1.toString());
         lore.add(Lang.ESSENCE2.toString());
         lore.add(Lang.ESSENCE3.toString());
         lore.add("");
         lore.add(Lang.ESSENCE4.toString());
         lore.add(Lang.ESSENCE5.toString());

         meta.setLore(lore);
         item.setItemMeta(meta);
         essenceOfEternity = item;
    }

    private static void createTrueHatchet(){
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.THATCHETNAME.toString());

        HashMap<Enchantment, Integer> enchData = getEnchantsFromConfig("TrueEternalHatchet");
        for (Enchantment ench : enchData.keySet()){
            meta.addEnchant(ench, enchData.get(ench), true);
        }

        List <String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Rarity.SPECIAL.toString());
        lore.add("");
        lore.add(Lang.THATCHET1.toString());
        lore.add(Lang.THATCHET2.toString());
        lore.add(Lang.THATCHET3.toString());
        lore.add(Lang.THATCHET4.toString());
        meta.setLore(lore);

        item.setItemMeta(meta);
        trueEternalHatchet = item;
    }

    private static void createSpine(){
         ItemStack item = new ItemStack(Material.END_ROD);
         ItemMeta meta = item.getItemMeta();

         meta.addEnchant(Enchantment.LUCK, 1, true);
         meta.setDisplayName(Lang.SPINENAME.toString());
         meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

         List <String> lore = new ArrayList<>();
         lore.add(Rarity.EPIC.toString());
         lore.add("");
         lore.add(Lang.SPINE1.toString());
         lore.add("");
         lore.add(Lang.SPINE2.toString());
         lore.add(Lang.SPINE3.toString());
         meta.setLore(lore);
         item.setItemMeta(meta);
         shatterSpine = item;

    }

    private static void makeShatterStick(){
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        List <String> lore = new ArrayList<>();

        meta.setDisplayName(Lang.STICKNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        lore.add(Lang.STICKENCH.toString());
        lore.add(ChatColor.DARK_GRAY + "");
        lore.add(Rarity.SPECIAL.toString());
        lore.add("");
        lore.add(Lang.STICK1.toString());
        lore.add(Lang.STICK2.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);

        shatterStick = item;
    }

    private static void createNutriment(){
         ItemStack item = new ItemStack(Material.PUMPKIN_PIE);
         ItemMeta meta = item.getItemMeta();
         List<String> lore = new ArrayList<>();

         meta.setDisplayName(Lang.NUTRIMENTNAME.toString());
         meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
         lore.add(Lang.NUTRIMENTENCH.toString());

         lore.add("");
         lore.add(Rarity.SPECIAL.toString());
         lore.add("");
         lore.add(Lang.NUTRIMENT1.toString());
         lore.add(Lang.NUTRIMENT2.toString());
         meta.setLore(lore);
         item.setItemMeta(meta);

         nutrimentOfTheInfinite = item;
    }

    private static void createTradeLoc(){
        ItemStack item = new ItemStack(Material.BLACK_DYE);
        ItemMeta tradeMeta = item.getItemMeta();
        assert tradeMeta != null;
        tradeMeta.setDisplayName(Lang.MERCHANTPLACER.toString());
        item.setItemMeta(tradeMeta);

        tradeLoc = item;
    }
    private static void createDLoc(){
        ItemStack item = new ItemStack(Material.PURPLE_DYE);
        ItemMeta tradeMeta = item.getItemMeta();
        assert tradeMeta != null;
        tradeMeta.setDisplayName(Lang.DMERCHANTPLACER.toString());
        item.setItemMeta(tradeMeta);

        DtradeLoc = item;
    }
    private static void createMLoc(){
        ItemStack item = new ItemStack(Material.GREEN_DYE);
        ItemMeta tradeMeta = item.getItemMeta();
        assert tradeMeta != null;
        tradeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lMaterial Merchant Placer"));
        item.setItemMeta(tradeMeta);

        MtradeLoc = item;
    }

    private static void createCrust(){
        ItemStack item = new ItemStack(Material.ORANGE_DYE);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Lang.CRUSTNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        List<String> lore = new ArrayList<>();
        lore.add(Rarity.RARE.toString());
        lore.add("");
        lore.add(Lang.CRUST1.toString());
        lore.add(ChatColor.DARK_GRAY + "");
        lore.add(Lang.CRUST2.toString());

        meta.setLore(lore);
        item.setItemMeta(meta);
        pieCrust = item;
    }

    private static void createApple(){
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Lang.APPLENAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<>();
        lore.add(Rarity.SPECIAL.toString());
        lore.add("");
        lore.add(Lang.APPLE1.toString());
        lore.add(Lang.APPLE2.toString());
        lore.add(ChatColor.DARK_GRAY + "");
        lore.add(Lang.APPLE3.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);

        lusciousApple = item;
    }

    private static void createOven(){
        ItemStack item = new ItemStack(Material.FURNACE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Lang.OVENNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.OVEN1.toString());
        lore.add(ChatColor.DARK_GRAY + "");
        lore.add(Lang.OVEN2.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        oven = item;
    }

    private static void createNutrimentU(){
        ItemStack item = new ItemStack(Material.PUMPKIN_PIE);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.setDisplayName(Lang.UNUTRIMENTNAME.toString());
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        lore.add(Lang.UNUTRIMENTENCH.toString());

        lore.add("");
        lore.add(Rarity.INSANE.toString());
        lore.add("");
        lore.add(Lang.UNUTRIMENT1.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);

        nutrimentU = item;
    }

    private static void createCatalyst(){
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lT&k&lh&8&le Ca&k&lt&8&la&k&lly&8&lst"));

        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.CATALYST1.toString());
        lore.add(Lang.CATALYST2.toString());

        meta.setLore(lore);
        item.setItemMeta(meta);
        theCatalyst = item;
    }

    private static ItemStack createCore(String name, String tagLine, Material mat, String rarity){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add(rarity);
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + tagLine);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private static void createBall(){
        ItemStack item = new ItemStack(Material.BLUE_ICE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.BALLLIGHTNINGNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.BALLLIGHTNING1.toString());
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Lost on death!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        ballLightning = item;
    }

    private static void createRifle(){
        ItemStack item = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.ENERGYNAME.toString());
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<>();
        lore.add(Rarity.INSANE.toString());
        lore.add("");
        lore.add(Lang.ENERGY1.toString());
        lore.add("");
        lore.add(Lang.ENERGY2.toString());
        int cost = plugin.getConfig().getInt("rifle-cost");
        lore.add(Lang.ENERGY3.toString().replace("!c", String.valueOf(cost / 2)));
        meta.setLore(lore);
        item.setItemMeta(meta);

        energyRifle = item;
    }

    private static void createChamber(){
        ItemStack item = new ItemStack(Material.STRUCTURE_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.CHAMBERNAME.toString());
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.SPECIAL.toString());
        lore.add("");
        lore.add(Lang.CHAMBER1.toString());
        lore.add(Lang.CHAMBER2.toString());
        lore.add("");
        lore.add(Lang.CHAMBER3.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);

        fusionChamber = item;
    }

    private static ItemStack createCatalystPart(String part, Material mat){
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.WEDGENAME + " " + part);
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.WEDGE1.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static void createUncharged(){
        ItemStack item = new ItemStack(Material.STONE_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.UNCHARGEDNAME.toString());
        meta.setLore(Collections.singletonList(Rarity.EPIC.toString()));
        item.setItemMeta(meta);
        unchargedRifle = item;
    }

    private static void createInfinium(){
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.INFINIUMNAME.toString());
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.RARE.toString());
        lore.add("");
        lore.add(Lang.INFINIUM1.toString());
        lore.add(Lang.INFINIUM2.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        infinium = item;
    }

    private static void createEnchantedInfinium(){
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.ENCHINFNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1 ,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.ENCHINFDESC.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        enchantedInfinium = item;
    }

    private static void createPlasmaTorch(){
        ItemStack item = new ItemStack(Material.SOUL_TORCH);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.PLASTORCHNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1 ,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.EPIC.toString());
        lore.add("");
        lore.add(Lang.PLASTORCHDESC.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        plasmaTorch = item;
    }

    private static void createVenHead(){
        ItemStack helm = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta meta = helm.getItemMeta();
        meta.setDisplayName(Lang.HELMNAME.toString());

        HashMap<Enchantment, Integer> enchData = getEnchantsFromConfig("EternalHelmet");
        for (Enchantment ench : enchData.keySet()){
            meta.addEnchant(ench, enchData.get(ench), true);
        }

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),meta.getDisplayName(), -8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier);
        List<String> hLore = new ArrayList<>();
        hLore.add("");
        hLore.add(Rarity.INSANE.toString());
        hLore.add("");

        int bonus = plugin.getConfig().getInt("helm-damage-bonus");
        int reduct = plugin.getConfig().getInt("helm-damage-reduction");

        hLore.add(Lang.HELMABIL1.toString().replace("!d", String.valueOf(bonus)));
        hLore.add(Lang.HELMABIL2.toString().replace("!d", String.valueOf(reduct)));
        hLore.add("");
        hLore.add(Lang.HELMDESC.toString());
        meta.setLore(hLore);
        helm.setItemMeta(meta);
        venHead = helm;
    }
    private static void createVenChest(){
        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta cMeta = chest.getItemMeta();
        cMeta.setDisplayName(Lang.CHESTNAME.toString());

        HashMap<Enchantment, Integer> enchData = getEnchantsFromConfig("EternalChestplate");
        for (Enchantment ench : enchData.keySet()){
            cMeta.addEnchant(ench, enchData.get(ench), true);
        }

        List<String> cLore = new ArrayList<>();
        cLore.add("");
        cLore.add(Rarity.INSANE.toString());
        cLore.add("");

        double heal = plugin.getConfig().getDouble("chestplate-heal") / 2;
        int cooldown = plugin.getConfig().getInt("chestplate-cooldown");

        cLore.add(Lang.CHESTABIL1.toString().replace("!h", String.valueOf(heal)));
        cLore.add(Lang.CHESTABIL2.toString().replace("!c", String.valueOf(cooldown)));
        cLore.add("");
        cLore.add(Lang.CHESTDESC.toString());
        cMeta.setLore(cLore);
        chest.setItemMeta(cMeta);
        venChest = chest;
    }
    private static void createVenLegs(){
        ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta lMeta = legs.getItemMeta();
        lMeta.setDisplayName(Lang.LEGSNAME.toString());

        HashMap<Enchantment, Integer> enchData = getEnchantsFromConfig("EternalLeggings");
        for (Enchantment ench : enchData.keySet()){
            lMeta.addEnchant(ench, enchData.get(ench), true);
        }

        AttributeModifier legsArmour = new AttributeModifier(UUID.randomUUID(),lMeta.getDisplayName(), 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        AttributeModifier legsToughness = new AttributeModifier(UUID.randomUUID(),lMeta.getDisplayName(), 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
        lMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, legsArmour);
        lMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, legsToughness);
        List<String> lLore = new ArrayList<>();
        lLore.add("");
        lLore.add(Rarity.INSANE.toString());
        lLore.add("");

        int chance = plugin.getConfig().getInt("legs-chance");
        int length = plugin.getConfig().getInt("legs-potion-length");
        int cooldown = plugin.getConfig().getInt("legs-cooldown");

        lLore.add(Lang.LEGSABIL1.toString().replace("!c", String.valueOf(chance)).replace("!l", String.valueOf(length)));
        lLore.add(Lang.LEGSABIL2.toString());
        lLore.add(Lang.LEGSABIL3.toString().replace("!c", String.valueOf(cooldown)));
        lLore.add("");
        lLore.add(Lang.LEGSDESC.toString());
        lMeta.setLore(lLore);
        legs.setItemMeta(lMeta);
        venLegs = legs;
    }
    private static void createVenBoots(){
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta bMeta = boots.getItemMeta();
        ((LeatherArmorMeta) bMeta).setColor(Color.BLACK);
        bMeta.setDisplayName(Lang.BOOTSNAME.toString());
        bMeta.setUnbreakable(true);
        AttributeModifier bModifier = new AttributeModifier(UUID.randomUUID(),bMeta.getDisplayName(), -0.4, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.FEET);
        bMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, bModifier);
        List<String> bLore = new ArrayList<>();
        bLore.add(Rarity.INSANE.toString());
        bLore.add("");
        int chance = plugin.getConfig().getInt("boots-double-chance");
        bLore.add(Lang.BOOTSABIL1.toString().replace("!c", String.valueOf(chance)));
        bLore.add("");
        bLore.add(Lang.BOOTSDESC.toString());
        bMeta.setLore(bLore);
        boots.setItemMeta(bMeta);
        venBoots = boots;
    }

    private static void createTalisman(){
        ItemStack item = new ItemStack(Material.CONDUIT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.TALISNAME.toString());
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.INSANE.toString());
        lore.add("");
        lore.add(Lang.TALISABIL.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        vendrickTalisman = item;
    }

    private static void createDarkFrag(){
        ItemStack item = new ItemStack(Material.FLINT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.VOIDFRAGNAME.toString());
        List<String> lore = new ArrayList<>();
        lore.add(Rarity.RARE.toString());
        lore.add("");
        lore.add(Lang.VOIDFRAGDESC.toString());
        meta.setLore(lore);
        item.setItemMeta(meta);
        voidCoreFragment = item;
    }
}
