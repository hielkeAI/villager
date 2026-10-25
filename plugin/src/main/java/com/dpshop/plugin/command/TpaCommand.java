package com.dpshop.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.tpa.TpaManager;

public class TpaCommand implements CommandExecutor {
    private final DpShopPlugin plugin;
    private final TpaManager tpa;

    public TpaCommand(DpShopPlugin plugin, TpaManager tpa) { this.plugin = plugin; this.tpa = tpa; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("players only."); return true; }
        Player p = (Player) sender;
        String c = cmd.getName().toLowerCase();
        if (c.equals("tpa") || c.equals("galw")) {
            if (args.length == 0) { p.sendMessage(ChatColor.RED + "usage: /tpa <player>"); return true; }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline() || target.equals(p)) { p.sendMessage(ChatColor.RED + "player not found."); return true; }
            tpa.request(p.getUniqueId(), target.getUniqueId(), TpaManager.TPA);
            target.sendMessage(ChatColor.YELLOW + p.getName() + " requests to teleport to you. /tpaccept or /tpdeny");
            p.sendMessage(ChatColor.GREEN + "teleport request sent to " + target.getName() + ".");
            return true;
        }
        if (c.equals("tpahere") || c.equals("galwyfan")) {
            if (args.length == 0) { p.sendMessage(ChatColor.RED + "usage: /tpahere <player>"); return true; }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline() || target.equals(p)) { p.sendMessage(ChatColor.RED + "player not found."); return true; }
            tpa.request(p.getUniqueId(), target.getUniqueId(), TpaManager.TPAHERE);
            target.sendMessage(ChatColor.YELLOW + p.getName() + " asks you to teleport to them. /tpaccept or /tpdeny");
            p.sendMessage(ChatColor.GREEN + "request sent.");
            return true;
        }
        if (c.equals("tpaccept") || c.equals("derbyn")) {
            TpaManager.Request r = tpa.get(p.getUniqueId());
            if (r == null) { p.sendMessage(ChatColor.RED + "no pending request."); return true; }
            Player from = Bukkit.getPlayer(r.from);
            if (from == null || !from.isOnline()) { tpa.clear(p.getUniqueId()); p.sendMessage(ChatColor.RED + "requester offline."); return true; }
            if (r.type == TpaManager.TPA) {
                from.teleport(p.getLocation());
                from.sendMessage(ChatColor.GREEN + "teleported to " + p.getName() + ".");
            } else {
                p.teleport(from.getLocation());
                p.sendMessage(ChatColor.GREEN + "teleported to " + from.getName() + ".");
            }
            tpa.clear(p.getUniqueId());
            return true;
        }
        if (c.equals("tpdeny") || c.equals("gwrthod")) {
            TpaManager.Request r = tpa.get(p.getUniqueId());
            if (r == null) { p.sendMessage(ChatColor.RED + "no pending request."); return true; }
            tpa.clear(p.getUniqueId());
            Player from = Bukkit.getPlayer(r.from);
            if (from != null) from.sendMessage(ChatColor.RED + p.getName() + " denied your request.");
            p.sendMessage(ChatColor.YELLOW + "request denied.");
            return true;
        }
        return false;
    }
}