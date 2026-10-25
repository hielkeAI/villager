package com.dpshop.plugin.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.economy.MoneyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopManager {
    private final DpShopPlugin plugin;
    private final MoneyManager money;
    private final List<ShopItem> items = new ArrayList<>();

    public static class ShopItem {
        public final Material material;
        public final double price;
        public ShopItem(Material m, double p) { material = m; price = p; }
    }

    public ShopManager(DpShopPlugin plugin, MoneyManager money) {
        this.plugin = plugin;
        this.money = money;
        reload();
    }

    public void reload() {
        items.clear();
        List<Map<?,?>> list = plugin.getConfig().getMapList("shop.items");
        for (Map<?,?> m : list) {
            try {
                Material mat = Material.valueOf(String.valueOf(m.get("material")).toUpperCase());
                double price = Double.parseDouble(String.valueOf(m.get("price")));
                items.add(new ShopItem(mat, price));
            } catch (Exception e) { plugin.getLogger().warning("Bad shop item: " + m); }
        }
    }

    public List<ShopItem> items() { return items; }

    public void openPlayerShop(Player p) {
        // Try the new Dialog GUI (1.21.6+). Falls back to chat menu.
        try { DialogShop.openPlayer(plugin, p, items, money); return; } catch (Throwable t) {
            plugin.getLogger().fine("Dialog GUI unavailable: " + t.getMessage());
        }
        sendChatMenu(p, "Villager Shop", items);
    }

    public void openAdminShop(Player p) {
        if (!p.hasPermission("dpshop.admin")) { p.sendMessage(ChatColor.RED + "No permission."); return; }
        try { DialogShop.openAdmin(plugin, p, money); return; } catch (Throwable t) { plugin.getLogger().fine(t.getMessage()); }
        p.sendMessage(ChatColor.GOLD + "Admin Shop");
        p.sendMessage(ChatColor.GRAY + "Grant money: " + ChatColor.WHITE + "/dpshop grant <amount>");
        p.sendMessage(ChatColor.GRAY + "Tag villager: " + ChatColor.WHITE + "/dpshop tag villager shop|admin");
        p.sendMessage(ChatColor.GRAY + "Tag chest: " + ChatColor.WHITE + "/dpshop tag chest");
    }

    public void openChestShop(Player p) {
        try { DialogShop.openChest(plugin, p, money); return; } catch (Throwable t) { plugin.getLogger().fine(t.getMessage()); }
        sendChatMenu(p, "Chest Shop", items);
    }

    private void sendChatMenu(Player p, String title, List<ShopItem> list) {
        p.sendMessage(ChatColor.GOLD + title);
        for (ShopItem si : list) {
            p.sendMessage(ChatColor.YELLOW + si.material.name() + ChatColor.GRAY + " - " + ChatColor.GOLD + si.price + "$ " +
                    ChatColor.GREEN + "[Buy: /dpshop buy " + si.material.name() + "]");
        }
        p.sendMessage(ChatColor.GRAY + "Balance: " + ChatColor.GOLD + money.get(p.getUniqueId()) + "$");
    }

    public boolean buy(Player p, Material mat) {
        ShopItem si = items.stream().filter(i -> i.material == mat).findFirst().orElse(null);
        if (si == null) { p.sendMessage(ChatColor.RED + "Item not in shop."); return false; }
        double tax = plugin.getConfig().getDouble("shop.tax-percent", 0);
        double finalPrice = si.price + (si.price * tax / 100.0);
        if (!money.withdraw(p.getUniqueId(), finalPrice)) { p.sendMessage(ChatColor.RED + "Not enough money (need " + finalPrice + "$)."); return false; }
        // tax money sink -> server bank (optional: store in MoneyManager not needed)
        p.getInventory().addItem(new ItemStack(mat, 1));
        p.sendMessage(ChatColor.GREEN + "Bought " + mat + " for " + finalPrice + "$ (tax " + tax + "%). Balance: " + ChatColor.GOLD + money.get(p.getUniqueId()) + "$");
        return true;
    }

    public void grant(Player p, double amt, Player target) {
        if (!p.hasPermission("dpshop.admin")) { p.sendMessage(ChatColor.RED + "No permission."); return; }
        money.deposit(target.getUniqueId(), amt);
        p.sendMessage(ChatColor.GREEN + "Granted " + amt + "$ to " + target.getName());
    }
}