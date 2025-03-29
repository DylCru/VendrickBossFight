package me.crazyrain.vendrickbossfight.items;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Supplier;

public enum ItemID {
    VEN_ETERNALSTAR("VEN_ETERNAL_STAR", () -> ItemManager.eternalStar),
    VEN_VENDRICKHATCHET("VEN_VENDRICK_HATCHET", () -> ItemManager.vendrickHatchet),
    VEN_GROWTHSWORD("VEN_GROWTH_SWORD", () -> ItemManager.growthSword),
    VEN_ETERNALFRAGMENT("VEN_ETERNAL_FRAGMENT", () -> ItemManager.eternalFragment),
    VEN_ESSENCEOFETERNITY("VEN_ESSENCE_OF_ETERNITY", () -> ItemManager.essenceOfEternity),
    VEN_TRUEETERNALHATCHET("VEN_TRUE_ETERNAL_HATCHET", () -> ItemManager.trueEternalHatchet),
    VEN_SHATTERSPINE("VEN_SHATTER_SPINE", () -> ItemManager.shatterSpine),
    VEN_NUTRIMENTOFTHEINFINITE("VEN_NUTRIMENT_OF_THE_INFINITE", () -> ItemManager.nutrimentOfTheInfinite),
    VEN_SHATTERSTICK("VEN_SHATTER_STICK", () -> ItemManager.shatterStick),
    VEN_TRADELOC("VEN_TRADE_LOC", () -> ItemManager.tradeLoc),
    VEN_DTRADELOC("VEN_D_TRADE_LOC", () -> ItemManager.DtradeLoc),
    VEN_MTRADELOC("VEN_M_TRADE_LOC", () -> ItemManager.MtradeLoc),
    VEN_PIECRUST("VEN_PIE_CRUST", () -> ItemManager.pieCrust),
    VEN_FLAMINGSTAR("VEN_FLAMING_STAR", () -> ItemManager.flamingStar),
    VEN_TIDALSTAR("VEN_TIDAL_STAR", () -> ItemManager.tidalStar),
    VEN_LUSCIOUSAPPLE("VEN_LUSCIOUS_APPLE", () -> ItemManager.lusciousApple),
    VEN_OVEN("VEN_OVEN", () -> ItemManager.oven),
    VEN_NUTRIMENTU("VEN_NUTRIMENT_U", () -> ItemManager.nutrimentU),
    VEN_THECATALYST("VEN_THE_CATALYST", () -> ItemManager.theCatalyst),
    VEN_FLAMECORE("VEN_FLAME_CORE", () -> ItemManager.flameCore),
    VEN_WAVECORE("VEN_WAVE_CORE", () -> ItemManager.waveCore),
    VEN_VOLTAICCORE("VEN_VOLTAIC_CORE", () -> ItemManager.voltaicCore),
    VEN_VOIDCORE("VEN_VOID_CORE", () -> ItemManager.voidCore),
    VEN_VOIDCOREFRAGMENT("VEN_VOID_CORE_FRAGMENT", () -> ItemManager.voidCoreFragment),
    VEN_VOLATILESTAR("VEN_VOLATILE_STAR", () -> ItemManager.volatileStar),
    VEN_STORMSTAR("VEN_STORM_STAR", () -> ItemManager.stormStar),
    VEN_BALLLIGHTNING("VEN_BALL_LIGHTNING", () -> ItemManager.ballLightning),
    VEN_ENERGYRIFLE("VEN_ENERGY_RIFLE", () -> ItemManager.energyRifle),
    VEN_FUSIONCHAMBER("VEN_FUSION_CHAMBER", () -> ItemManager.fusionChamber),
    VEN_CATALYSTPARTA("VEN_CATALYST_PART_A", () -> ItemManager.catalystPartA),
    VEN_CATALYSTPARTB("VEN_CATALYST_PART_B", () -> ItemManager.catalystPartB),
    VEN_UNCHARGEDRIFLE("VEN_UNCHARGED_RIFLE", () -> ItemManager.unchargedRifle),
    VEN_INFINIUM("VEN_INFINIUM", () -> ItemManager.infinium),
    VEN_ENCHANTEDINFINIUM("VEN_ENCHANTED_INFINIUM", () -> ItemManager.enchantedInfinium),
    VEN_PLASMATORCH("VEN_PLASMA_TORCH", () -> ItemManager.plasmaTorch),
    VEN_DARKSTAR("VEN_DARK_STAR", () -> ItemManager.darkStar),
    VEN_VENCHEST("VEN_VEN_CHEST", () -> ItemManager.venChest),
    VEN_VENLEGS("VEN_VEN_LEGS", () -> ItemManager.venLegs),
    VEN_VENHEAD("VEN_VEN_HEAD", () -> ItemManager.venHead),
    VEN_VENBOOTS("VEN_VEN_BOOTS", () -> ItemManager.venBoots),
    VEN_VENDRICKTALISMAN("VEN_VENDRICK_TALISMAN", () -> ItemManager.vendrickTalisman);


    private final String id;
    private final Supplier<ItemStack> itemStack;


    // Cache for fast lookup
    private static final HashMap<String, ItemID> ID_MAP = new HashMap<>();

    static {
        for (ItemID item : values()) {
            ID_MAP.put(item.id, item);
        }
    }

    ItemID(String id, Supplier<ItemStack> itemStack) {
        this.id = id;
        this.itemStack = itemStack;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack.get();
    }

    // Method to find an ItemStack by ID
    public static ItemStack getItemStackById(String id) {
        ItemID item = ID_MAP.get(id);
        return (item != null) ? item.getItemStack() : null;
    }

    public static String getIDByItemStack(ItemStack item) {
        return NBTEditor.getString(item,NBTEditor.CUSTOM_DATA, "VEN_ITEM_ID");
    }
}
