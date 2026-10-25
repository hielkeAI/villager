package com.dpshop.plugin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.shop.ShopManager;
import com.dpshop.plugin.lang.Lang;
import com.dpshop.plugin.villager.VillagerShop;
import com.dpshop.plugin.listener.VillagerShopListener;
import java.util.UUID;

public class DpShopCommand implements CommandExecutor {
    private final DpShopPlugin plugin;
    private final ShopManager shop;

    public DpShopCommand(DpShopPlugin plugin, ShopManager shop) {
        this.plugin = plugin;
        this.shop = shop;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Players only."); return true; }
        Player p = (Player) sender;
        if (args.length == 0) { p.sendMessage(ChatColor.GOLD + "/dpshop shop|admin|chest|buy <MATERIAL>|grant <amount> [player]|tag ..."); return true; }
        switch (args[0].toLowerCase()) {
            case "shop": shop.openPlayerShop(p); return true;
            case "admin": shop.openAdminShop(p); return true;
            case "chest": shop.openChestShop(p); return true;
            case "buy": {
                if (args.length < 2) { p.sendMessage(ChatColor.RED + "Usage: /dpshop buy <MATERIAL>"); return true; }
                try { shop.buy(p, org.bukkit.Material.valueOf(args[1].toUpperCase())); } catch (Exception e) { p.sendMessage(ChatColor.RED + "Unknown material."); }
                return true;
            }
            case "grant": {
                if (args.length < 2) { p.sendMessage(ChatColor.RED + "Usage: /dpshop grant <amount> [player]"); return true; }
                try {
                    double amt = Double.parseDouble(args[1]);
                    Player target = (args.length >= 3) ? plugin.getServer().getPlayer(args[2]) : p;
                    if (target == null) { p.sendMessage(ChatColor.RED + "Player not found."); return true; }
                    shop.grant(p, amt, target);
                } catch (Exception e) { p.sendMessage(ChatColor.RED + "Invalid amount."); }
                return true;
            }
            case "tag": {
                if (!p.hasPermission("dpshop.admin")) { p.sendMessage(ChatColor.RED + "No permission."); return true; }
                if (args.length < 3) { p.sendMessage(ChatColor.RED + "Usage: /dpshop tag villager shop|admin  OR  /dpshop tag chest"); return true; }
                if (args[1].equalsIgnoreCase("villager")) {
                    org.bukkit.entity.Villager v = null;
                    for (org.bukkit.entity.Entity e : p.getNearbyEntities(5,5,5)) if (e instanceof org.bukkit.entity.Villager) { v = (org.bukkit.entity.Villager) e; break; }
                    if (v == null) { p.sendMessage(ChatColor.RED + "No villager nearby."); return true; }
                    if (args[2].equalsIgnoreCase("shop")) { v.setCustomName(org.bukkit.ChatColor.YELLOW + "[Shop]"); v.setCustomNameVisible(true); v.setMetadata("dpshop.shop", new org.bukkit.metadata.FixedMetadataValue(plugin, true)); p.sendMessage(ChatColor.GREEN + "Tagged as shop."); }
                    else if (args[2].equalsIgnoreCase("admin")) { v.setCustomName(org.bukkit.ChatColor.RED + "[Admin Shop]"); v.setCustomNameVisible(true); p.sendMessage(ChatColor.GREEN + "Tagged as admin shop."); }
                } else if (args[1].equalsIgnoreCase("chest")) {
                    p.sendMessage(ChatColor.GREEN + "Stand on the chest and place a sign reading [Shop] on top/side.");
                }
                return true;
            }
            case "money": { p.sendMessage(ChatColor.GOLD + "Balance: " + plugin.getMoney().get(p.getUniqueId()) + "$"); return true; }
            case "vbuy": {
                if (args.length < 2) { p.sendMessage(ChatColor.RED + "usage: /dpshop vbuy <index>"); return true; }
                UUID vid = VillagerShopListener.lastShop.get(p.getUniqueId());
                if (vid == null) { p.sendMessage(ChatColor.RED + "no villager selected. right-click one first."); return true; }
                VillagerShop s = plugin.getVillagerShops().get(vid);
                if (s == null) { p.sendMessage(ChatColor.RED + "no shop."); return true; }
                int idx;
                try { idx = Integer.parseInt(args[1]); } catch (Exception e) { p.sendMessage(ChatColor.RED + "bad index."); return true; }
                if (idx < 0 || idx >= s.stock.size()) { p.sendMessage(ChatColor.RED + "invalid index."); return true; }
                VillagerShop.Entry e = s.stock.get(idx);
                boolean ok = plugin.getVillagerShops().buy(s, p, e);
                if (ok) p.sendMessage(ChatColor.GREEN + Lang.fmt("bought","n",1,"item",e.material.name(),"p", e.buy));
                else p.sendMessage(ChatColor.RED + Lang.get("not.enough.money"));
                // gain reputation handled in manager
                return true;
            }
            case "vsell": {
                if (args.length < 2) { p.sendMessage(ChatColor.RED + "usage: /dpshop vsell <index>"); return true; }
                UUID vid2 = VillagerShopListener.lastShop.get(p.getUniqueId());
                if (vid2 == null) { p.sendMessage(ChatColor.RED + "no villager selected."); return true; }
                VillagerShop s2 = plugin.getVillagerShops().get(vid2);
                if (s2 == null) { p.sendMessage(ChatColor.RED + "no shop."); return true; }
                int idx2;
                try { idx2 = Integer.parseInt(args[1]); } catch (Exception e) { p.sendMessage(ChatColor.RED + "bad index."); return true; }
                if (idx2 < 0 || idx2 >= s2.stock.size()) { p.sendMessage(ChatColor.RED + "invalid index."); return true; }
                VillagerShop.Entry e2 = s2.stock.get(idx2);
                boolean ok2 = plugin.getVillagerShops().sell(s2, p, e2);
                if (ok2) p.sendMessage(ChatColor.GREEN + Lang.fmt("sold","n",1,"item",e2.material.name(),"p", e2.sell));
                else p.sendMessage(ChatColor.RED + Lang.get("not.enough.items"));
                return true;
            }
            case "vrestock": {
                if (!p.hasPermission("dpshop.admin")) { p.sendMessage(ChatColor.RED + Lang.get("no.perm")); return true; }
                UUID vid3 = VillagerShopListener.lastShop.get(p.getUniqueId());
                if (vid3==null){ p.sendMessage(ChatColor.RED+"no villager selected."); return true;}
                VillagerShop s3 = plugin.getVillagerShops().get(vid3);
                if (s3!=null){ s3.lastRestock=0; plugin.getVillagerShops().restockIfNeeded(s3); p.sendMessage(ChatColor.GREEN+"restocked."); }
                return true;
            }
            case "vinfo": {
                for (java.util.UUID v : new java.util.ArrayList<>(VillagerShopListener.lastShop.values())) {}
                p.sendMessage(ChatColor.GRAY + "interact with a villager to see their shop.");
                return true;
            }
            default: p.sendMessage(ChatColor.RED + "Unknown subcommand."); return true;
        }
    }
}