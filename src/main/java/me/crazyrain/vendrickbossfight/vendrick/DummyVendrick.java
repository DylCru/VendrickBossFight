package me.crazyrain.vendrickbossfight.vendrick;

import me.crazyrain.vendrickbossfight.VendrickBossFight;
import org.bukkit.Location;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.UUID;

public class DummyVendrick extends Vendrick{
    public DummyVendrick(List<UUID> players, Location spawnLoc, VendrickBossFight plugin) {
        super(players, spawnLoc, plugin);
        spawnBoss();
        vendrick.addScoreboardTag("DummyVendrick");
    }

    @Override
    public int getDifficulty() {
        return 5;
    }
}
