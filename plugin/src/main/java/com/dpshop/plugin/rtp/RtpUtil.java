package com.dpshop.plugin.rtp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.util.Random;

public class RtpUtil {
    public static Location findSafe(World w, int radius, int maxTries) {
        Random r = new Random();
        for (int i = 0; i < maxTries; i++) {
            int x = (r.nextInt(radius * 2) - radius);
            int z = (r.nextInt(radius * 2) - radius);
            int y = w.getHighestBlockYAt(x, z);
            Block ground = w.getBlockAt(x, y, z);
            Block feet = w.getBlockAt(x, y + 1, z);
            Block head = w.getBlockAt(x, y + 2, z);
            if (ground.getType().isSolid() && feet.getType().isAir() && head.getType().isAir()) {
                return new Location(w, x + 0.5, y + 1, z + 0.5);
            }
        }
        return null;
    }
}