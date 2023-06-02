package me.crazyrain.vendrickbossfight.functionality;

import org.bukkit.ChatColor;

public enum Rarity {
    RARE(Lang.RARETIER.toString()),
    EPIC(Lang.EPICTIER.toString()),
    SPECIAL(Lang.SPECIALTIER.toString()),
    INSANE(Lang.INSANETIER.toString());

    String text;
    Rarity(String text){
        this.text = text;
    }

    @Override
    public String toString(){
        return ChatColor.translateAlternateColorCodes('&', this.text);
    }
}
