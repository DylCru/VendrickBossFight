package me.crazyrain.vendrickbossfight.items;

import java.util.HashMap;

public class DefaultRecipes {

    public static HashMap<String, String[]> getDefaultRecipes() {
        HashMap<String, String[]> defaultRecipes = new HashMap<>();

        defaultRecipes.put("VEN_TRUE_ETERNAL_HATCHET", new String[]{
                "", "VEN_ETERNAL_FRAGMENT", "",
                "VEN_ETERNAL_FRAGMENT", "VEN_VENDRICK_HATCHET", "VEN_ETERNAL_FRAGMENT",
                "", "VEN_ETERNAL_FRAGMENT", ""
        });

        defaultRecipes.put("VEN_SHATTER_STICK", new String[]{
                "", "VEN_ESSENCE_OF_ETERNITY", "",
                "VEN_ESSENCE_OF_ETERNITY", "VEN_SHATTER_SPINE", "VEN_ESSENCE_OF_ETERNITY",
                "", "VEN_ESSENCE_OF_ETERNITY", ""
        });

        defaultRecipes.put("VEN_NUTRIMENT_OF_THE_INFINITE", new String[]{
                "", "VEN_ESSENCE_OF_ETERNITY", "",
                "VEN_ETERNAL_FRAGMENT", "GOLDEN_APPLE", "VEN_ESSENCE_OF_ETERNITY",
                "", "VEN_ETERNAL_FRAGMENT", ""
        });

        defaultRecipes.put("VEN_NUTRIMENT_U", new String[]{
                "VEN_ETERNAL_FRAGMENT", "VEN_NUTRIMENT_OF_THE_INFINITE", "VEN_ETERNAL_FRAGMENT",
                "VEN_ETERNAL_FRAGMENT", "VEN_LUSCIOUS_APPLE", "VEN_ETERNAL_FRAGMENT",
                "VEN_ETERNAL_FRAGMENT", "", "VEN_ETERNAL_FRAGMENT"
        });

        defaultRecipes.put("VEN_FLAMING_STAR", new String[]{
                "", "", "",
                "VEN_THE_CATALYST", "VEN_ETERNAL_STAR", "VEN_FLAME_CORE",
                "", "VEN_FLAME_CORE", "VEN_FLAME_CORE"
        });

        defaultRecipes.put("VEN_TIDAL_STAR", new String[]{
                "", "", "",
                "VEN_THE_CATALYST", "VEN_ETERNAL_STAR", "VEN_WAVE_CORE",
                "", "VEN_WAVE_CORE", "VEN_WAVE_CORE"
        });

        defaultRecipes.put("VEN_STORM_STAR", new String[]{
                "", "", "",
                "VEN_THE_CATALYST", "VEN_ETERNAL_STAR", "VEN_VOLTAIC_CORE",
                "", "VEN_VOLTAIC_CORE", "VEN_VOLTAIC_CORE"
        });

        defaultRecipes.put("VEN_ENERGY_RIFLE", new String[]{
                "VEN_INFINIUM", "VEN_INFINIUM", "VEN_INFINIUM",
                "", "", "VEN_FUSION_CHAMBER",
                "", "", "VEN_VOLTAIC_CORE"
        });

        defaultRecipes.put("VEN_THE_CATALYST", new String[]{
                "VEN_ESSENCE_OF_ETERNITY", "END_CRYSTAL", "VEN_ETERNAL_FRAGMENT",
                "VEN_ESSENCE_OF_ETERNITY", "", "VEN_ETERNAL_FRAGMENT",
                "VEN_ESSENCE_OF_ETERNITY", "END_CRYSTAL", "VEN_ETERNAL_FRAGMENT"
        });

        defaultRecipes.put("VEN_ENCHANTED_INFINIUM", new String[]{
                "", "VEN_INFINIUM", "",
                "", "VEN_PLASMA_TORCH", "",
                "", "VEN_INFINIUM", ""
        });

        defaultRecipes.put("VEN_VEN_HEAD", new String[]{
                "VEN_ENCHANTED_INFINIUM", "VEN_INFINIUM", "VEN_ENCHANTED_INFINIUM",
                "VEN_INFINIUM", "", "VEN_INFINIUM",
                "", "", ""
        });

        defaultRecipes.put("VEN_VEN_CHEST", new String[]{
                "VEN_ENCHANTED_INFINIUM", "", "VEN_ENCHANTED_INFINIUM",
                "VEN_INFINIUM", "VEN_INFINIUM", "VEN_INFINIUM",
                "VEN_INFINIUM", "VEN_INFINIUM", "VEN_INFINIUM"
        });

        defaultRecipes.put("VEN_VEN_LEGS", new String[]{
                "VEN_ENCHANTED_INFINIUM", "VEN_INFINIUM", "VEN_ENCHANTED_INFINIUM",
                "VEN_INFINIUM", "", "VEN_INFINIUM",
                "VEN_INFINIUM", "", "VEN_INFINIUM"
        });

        defaultRecipes.put("VEN_VEN_BOOTS", new String[]{
                "VEN_ENCHANTED_INFINIUM", "", "VEN_ENCHANTED_INFINIUM",
                "VEN_INFINIUM", "", "VEN_INFINIUM",
                "", "", ""
        });

        defaultRecipes.put("VEN_DARK_STAR", new String[]{
                "", "", "",
                "VEN_THE_CATALYST", "VEN_ETERNAL_STAR", "VEN_VOID_CORE",
                "", "VEN_VOID_CORE", "VEN_VOID_CORE"
        });

        return defaultRecipes;
    }
}
