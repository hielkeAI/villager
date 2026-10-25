package com.dpshop.plugin.economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.economy.Economy;
import com.dpshop.plugin.DpShopPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoneyManager {
    private final DpShopPlugin plugin;
    private final File file;
    private final Map<UUID, Double> balances = new HashMap<>();
    private Economy vault;

    public MoneyManager(DpShopPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "balances.yml");
        if (Bukkit.getServicesManager().getRegistration(Economy.class) != null) {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
            vault = rsp.getProvider();
        }
        load();
    }

    public boolean hasVault() { return vault != null; }

    public double get(UUID u) {
        if (vault != null) return vault.getBalance(Bukkit.getOfflinePlayer(u));
        return balances.getOrDefault(u, (double) plugin.getConfig().getInt("shop.default-balance", 100));
    }

    public void set(UUID u, double v) {
        if (vault != null) { vault.withdrawPlayer(Bukkit.getOfflinePlayer(u), get(u)); vault.depositPlayer(Bukkit.getOfflinePlayer(u), v); return; }
        balances.put(u, v);
    }

    public boolean withdraw(UUID u, double amt) {
        if (vault != null) return vault.withdrawPlayer(Bukkit.getOfflinePlayer(u), amt).transactionSuccess();
        double cur = get(u);
        if (cur < amt) return false;
        balances.put(u, cur - amt);
        return true;
    }

    public void deposit(UUID u, double amt) {
        if (vault != null) { vault.depositPlayer(Bukkit.getOfflinePlayer(u), amt); return; }
        balances.put(u, get(u) + amt);
    }

    public void save() {
        if (vault != null) return;
        FileConfiguration c = new YamlConfiguration();
        for (Map.Entry<UUID, Double> e : balances.entrySet()) c.set(e.getKey().toString(), e.getValue());
        try { c.save(file); } catch (IOException ex) { plugin.getLogger().warning(ex.getMessage()); }
    }

    private void load() {
        if (vault != null || !file.exists()) return;
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        for (String k : c.getKeys(false)) {
            try { balances.put(UUID.fromString(k), c.getDouble(k)); } catch (Exception ignored) {}
        }
    }
}