package com.dpshop.plugin.villager;

import org.bukkit.Material;
import java.util.*;

public class VillagerShop {
    public java.util.UUID villager;
    public java.util.UUID owner; // player who configured it (null = unclaimed/default)
    public String profession = "none";
    public int tier = 1; // 1..5
    public boolean admin;
    public long lastRestock;
    public List<Entry> stock = new ArrayList<>();
    public Map<String,Integer> reputation = new HashMap<>();

    public static class Entry {
        public Material material; public int amount; public double buy; public double sell; public int maxStock;
        public Entry(Material m,int a,double b,double s,int max){material=m;amount=a;buy=b;sell=s;maxStock=max;}
    }

    public int discount(String playerName){ int rep = reputation.getOrDefault(playerName,0); return Math.min(50, rep/2); } // % discount, max 50%
}