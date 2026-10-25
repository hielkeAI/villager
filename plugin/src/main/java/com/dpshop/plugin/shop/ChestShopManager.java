package com.dpshop.plugin.shop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.dpshop.plugin.DpShopPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestShopManager {
    private final DpShopPlugin plugin;
    private final File file;
    private final Map<String, ChestShop> shops = new HashMap<>();

    public ChestShopManager(DpShopPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "chestshops.yml");
        load();
    }

    public ChestShop get(Block chest) {
        if (chest == null) return null;
        return shops.get(key(chest.getWorld().getName(), chest.getX(), chest.getY(), chest.getZ()));
    }

    public ChestShop getBySign(Block signBlock) {
        // find adjacent chest
        for (org.bukkit.block.BlockFace f : new org.bukkit.block.BlockFace[]{org.bukkit.block.BlockFace.DOWN, org.bukkit.block.BlockFace.UP, org.bukkit.block.BlockFace.NORTH, org.bukkit.block.BlockFace.SOUTH, org.bukkit.block.BlockFace.EAST, org.bukkit.block.BlockFace.WEST}) {
            Block b = signBlock.getRelative(f);
            if (b.getState() instanceof Chest) {
                ChestShop s = get(b);
                if (s != null) return s;
            }
        }
        return null;
    }

    public void add(ChestShop s) { shops.put(s.key(), s); save(); }
    public void remove(String k) { shops.remove(k); save(); }
    public void removeByChest(Block chest) { remove(key(chest.getWorld().getName(), chest.getX(), chest.getY(), chest.getZ())); }

    private String key(String w,int x,int y,int z){ return w+":"+x+":"+y+":"+z; }

    public void save(){ try {
        FileConfiguration c = new YamlConfiguration();
        for (ChestShop s : shops.values()) {
            String b = s.key();
            c.set(b+".world", s.world); c.set(b+".x",s.x); c.set(b+".y",s.y); c.set(b+".z",s.z);
            c.set(b+".ownerName", s.ownerName);
            if (s.owner!=null) c.set(b+".owner", s.owner.toString());
            c.set(b+".admin", s.admin);
            c.set(b+".material", s.material.name());
            c.set(b+".amount", s.amount);
            c.set(b+".buy", s.buyPrice);
            c.set(b+".sell", s.sellPrice);
        }
        c.save(file);
    } catch (IOException e) { plugin.getLogger().warning(e.getMessage()); } }

    private void load() {
        if (!file.exists()) return;
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        for (String k : c.getKeys(false)) {
            try {
                String w = c.getString(k+".world");
                int x=c.getInt(k+".x"),y=c.getInt(k+".y"),z=c.getInt(k+".z");
                String ownerName = c.getString(k+".ownerName","?");
                UUID owner = (c.getString(k+".owner")==null)? null : UUID.fromString(c.getString(k+".owner"));
                boolean admin = c.getBoolean(k+".admin");
                org.bukkit.Material m = org.bukkit.Material.valueOf(c.getString(k+".material"));
                int amount = c.getInt(k+".amount",1);
                double buy = c.getDouble(k+".buy",0);
                double sell = c.getDouble(k+".sell",0);
                ChestShop s = new ChestShop(w,x,y,z,owner,ownerName,admin,m,amount,buy,sell);
                shops.put(s.key(), s);
            } catch (Exception ignored) {}
        }
    }

    public Map<String,ChestShop> all(){ return shops; }
}