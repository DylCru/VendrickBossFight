package me.crazyrain.vendrickbossfight.functionality;

public enum Distortion {
    NORMAL("Normal"),
    FIRE("Flaming"),
    WATER("Tidal"),
    STORMY("Stormy"),
    DARK("Dark");

    String distortion;
    Distortion(String distortion) { this.distortion = distortion; }

    public String getDistortion() {
        return distortion;
    }
}
