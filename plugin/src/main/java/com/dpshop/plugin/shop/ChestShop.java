package com.dpshop.plugin.shop;

import org.bukkit.inventory.ItemStack;
import java.util.UUID;
import java.io.Serializable;

public class ChestShop {
    public String world;
    public int x,y,z;
    public String ownerName;
    public UUID owner;
    public boolean admin;
    public org.bukkit.Material material;
    public int amount;
    public double buyPrice;
    public double sellPrice;
    public ChestShop(String w,int x,int y,int z,UUID owner,String ownerName,boolean admin,org.bukkit.Material m,int amount,double buy,double sell){
        this.world=w;this.x=x;this.y=y;this.z=z;this.owner=owner;this.ownerName=ownerName;this.admin=admin;this.material=m;this.amount=amount;this.buyPrice=buy;this.sellPrice=sell;
    }
    public String key(){ return world+":"+x+":"+y+":"+z; }
}