package com.dpshop.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import com.dpshop.plugin.economy.MoneyManager;
import com.dpshop.plugin.shop.ShopManager;
import com.dpshop.plugin.listener.VillagerShopListener;
import com.dpshop.plugin.listener.ChestShopListener;

public class DpShopPlugin extends JavaPlugin {
    private static DpShopPlugin instance;
    private MoneyManager money;
    private ShopManager shop;
    private com.dpshop.plugin.home.HomeManager homes;
    private com.dpshop.plugin.tpa.TpaManager tpa;
    private com.dpshop.plugin.shop.ChestShopManager chestShops;
    private com.dpshop.plugin.villager.VillagerShopManager villagerShops;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        money = new MoneyManager(this);
        shop = new ShopManager(this, money);
        homes = new com.dpshop.plugin.home.HomeManager(this);
        tpa = new com.dpshop.plugin.tpa.TpaManager(this);
        chestShops = new com.dpshop.plugin.shop.ChestShopManager(this);
        villagerShops = new com.dpshop.plugin.villager.VillagerShopManager(this);
        getServer().getPluginManager().registerEvents(new com.dpshop.plugin.listener.VillagerShopListener(this, shop), this);
        getServer().getPluginManager().registerEvents(new com.dpshop.plugin.listener.ChestShopListener(this, shop), this);
        getServer().getPluginManager().registerEvents(new com.dpshop.plugin.listener.ChestShopSignListener(this, chestShops), this);
        org.bukkit.command.PluginCommand pc = getCommand("dpshop");
        if (pc != null) pc.setExecutor(new com.dpshop.plugin.command.DpShopCommand(this, shop));
        wire("home", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("sethome", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("delhome", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("homes", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("cartref", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("gosodcartref", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("dileucartref", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("cartrefau", new com.dpshop.plugin.command.HomeCommand(this, homes));
        wire("rtp", new com.dpshop.plugin.command.RtpCommand(this));
        wire("tpa", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("tpahere", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("tpaccept", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("tpdeny", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("galw", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("galwyfan", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("derbyn", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("gwrthod", new com.dpshop.plugin.command.TpaCommand(this, tpa));
        wire("pay", new com.dpshop.plugin.command.PayCommand(this));
        getLogger().info("DpShop enabled (rtp/home/tpa/pay).");
    }

    private void wire(String name, org.bukkit.command.CommandExecutor ex) {
        org.bukkit.command.PluginCommand c = getCommand(name);
        if (c != null) c.setExecutor(ex);
    }

    @Override
    public void onDisable() {
        if (money != null) money.save();
        if (homes != null) homes.save();
        if (chestShops != null) chestShops.save();
        if (villagerShops != null) villagerShops.save();
    }

    public static DpShopPlugin get() { return instance; }
    public MoneyManager getMoney() { return money; }
    public ShopManager getShop() { return shop; }
    public ChestShopManager getChestShops() { return chestShops; }
    public com.dpshop.plugin.villager.VillagerShopManager getVillagerShops() { return villagerShops; }
}