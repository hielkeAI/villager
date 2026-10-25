package com.dpshop.plugin.villager;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import com.dpshop.plugin.DpShopPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class VillagerShopManager {
    private final DpShopPlugin plugin;
    private final File file;
    private final Map<java.util.UUID, VillagerShop> shops = new HashMap<>();

    public VillagerShopManager(DpShopPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "villagershops.yml");
        load();
    }

    public VillagerShop getOrCreate(Villager v) {
        VillagerShop s = shops.get(v.getUniqueId());
        if (s == null) {
            s = new VillagerShop();
            s.villager = v.getUniqueId();
            s.profession = (v.getProfession()==null) ? "none" : v.getProfession().name().toLowerCase();
            s.admin = false;
            s.tier = 1;
            s.lastRestock = 0;
            defaultStock(s);
            shops.put(v.getUniqueId(), s);
            save();
        }
        return s;
    }

    private void defaultStock(VillagerShop s) {
        s.stock.clear();
        // profession-based defaults
        switch (s.profession) {
            case "armorer":
                add(s, Material.IRON_INGOT,5,12,6,20);
                add(s, Material.IRON_CHESTPLATE,1,40,20,3);
                add(s, Material.SHIELD,1,30,15,2);
                break;
            case "weaponsmith":
                add(s, Material.IRON_SWORD,1,25,12,3);
                add(s, Material.IRON_AXE,1,22,11,3);
                break;
            case "toolsmith":
                add(s, Material.IRON_PICKAXE,1,28,14,3);
                add(s, Material.IRON_SHOVEL,1,18,9,3);
                break;
            case "cleric":
                add(s, Material.REDSTONE,10,20,10,32);
                add(s, Material.GLOWSTONE,4,16,8,16);
                break;
            case "librarian":
                add(s, Material.BOOK,6,12,6,16);
                add(s, Material.PAPER,10,5,2,32);
                break;
            case "butcher":
                add(s, Material.COOKED_BEEF,8,12,6,16);
                add(s, Material.LEATHER,4,10,5,8);
                break;
            case "farmer":
                add(s, Material.BREAD,6,6,3,16);
                add(s, Material.APPLE,5,5,2,16);
                add(s, Material.WHEAT,10,4,2,32);
                break;
            default:
                add(s, Material.APPLE,3,5,2,16);
                add(s, Material.BREAD,3,6,3,16);
        }
    }

    private void add(VillagerShop s, Material m, int amt, double buy, double sell, int max) {
        s.stock.add(new VillagerShop.Entry(m, amt, buy, sell, max));
    }

    public void restockIfNeeded(VillagerShop s) {
        long now = System.currentTimeMillis();
        long day = 24*60*60*1000L;
        if (now - s.lastRestock < day) return;
        s.lastRestock = now;
        for (VillagerShop.Entry e : s.stock) { e.amount = e.maxStock; }
        save();
    }

    public boolean buy(VillagerShop s, org.bukkit.entity.Player p, VillagerShop.Entry e) {
        int disc = s.discount(p.getName());
        double finalPrice = e.buy * (1 - disc/100.0);
        if (!s.admin && e.amount <= 0) { return false; }
        if (!plugin.getMoney().withdraw(p.getUniqueId(), finalPrice)) return false;
        if (!s.admin) { e.amount--; }
        p.getInventory().addItem(new ItemStack(e.material, 1));
        // reputation bump
        s.reputation.put(p.getName(), s.reputation.getOrDefault(p.getName(),0)+1);
        save();
        return true;
    }

    public boolean sell(VillagerShop s, org.bukkit.entity.Player p, VillagerShop.Entry e) {
        if (e.sell <= 0) return false;
        if (!p.getInventory().contains(e.material)) return false;
        p.getInventory().removeItem(new ItemStack(e.material,1));
        if (!s.admin) e.amount++;
        plugin.getMoney().deposit(p.getUniqueId(), e.sell);
        if (s.owner?? ) {}
        save();
        return true;
    }

    public void save() {
        try {
            FileConfiguration c = new YamlConfiguration();
            for (VillagerShop s : shops.values()) {
                String b = s.villager.toString();
                c.set(b+".profession", s.profession);
                if (s.owner != null) c.set(b+".owner", s.owner.toString());
                c.set(b+".tier", s.tier);
                c.set(b+".admin", s.admin);
                c.set(b+".lastRestock", s.lastRestock);
                int i=0;
                for (VillagerShop.Entry e : s.stock) {
                    c.set(b+".stock."+i+".material", e.material.name());
                    c.set(b+".stock."+i+".amount", e.amount);
                    c.set(b+".stock."+i+".buy", e.buy);
                    c.set(b+".stock."+i+".sell", e.sell);
                    c.set(b+".stock."+i+".max", e.maxStock);
                    i++;
                }
                c.set(b+".stockCount", i);
                for (Map.Entry<String,Integer> r : s.reputation.entrySet()) c.set(b+".rep."+r.getKey(), r.getValue());
            }
            c.save(file);
        } catch (IOException e) { plugin.getLogger().warning(e.getMessage()); }
    }

    private void load() { if (!file.exists()) return;
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        for (String k : c.getKeys(false)) {
            try {
                java.util.UUID id = java.util.UUID.fromString(k);
                VillagerShop s = new VillagerShop();
                s.villager = id;
                s.profession = c.getString(k+".profession","none");
                String o = c.getString(k+".owner");
                s.owner = (o==null)? null : java.util.UUID.fromString(o);
                s.tier = c.getInt(k+".tier",1);
                s.admin = c.getBoolean(k+".admin",false);
                s.lastRestock = c.getLong(k+".lastRestock",0);
                int cnt = c.getInt(k+".stockCount",0);
                for (int i=0;i<cnt;i++){
                    org.bukkit.Material m = org.bukkit.Material.valueOf(c.getString(k+".stock."+i+".material","APPLE"));
                    int a = c.getInt(k+".stock."+i+".amount",1);
                    double b = c.getDouble(k+".stock."+i+".buy",5);
                    double se = c.getDouble(k+".stock."+i+".sell",2);
                    int max = c.getInt(k+".stock."+i+".max", a);
                    s.stock.add(new VillagerShop.Entry(m,a,b,se,max));
                }
                org.bukkit.configuration.ConfigurationSection rep = c.getConfigurationSection(k+".rep");
                if (rep!=null) for (String r : rep.getKeys(false)) s.reputation.put(r, rep.getInt(r));
                shops.put(id,s);
            } catch (Exception ignored) {}
        }
    }
    public VillagerShop get(java.util.UUID id){ return shops.get(id); }
}