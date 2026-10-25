package com.dpshop.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpshop.plugin.DpShopPlugin;

public class PayCommand implements CommandExecutor {
    private final DpShopPlugin plugin;
    public PayCommand(DpShopPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("players only."); return true; }
        if (args.length < 2) { sender.sendMessage(ChatColor.RED + "usage: /pay <player> <amount>"); return true; }
        Player p = (Player) sender;
        Player t = Bukkit.getPlayerExact(args[0]);
        if (t == null || !t.isOnline() || t.equals(p)) { p.sendMessage(ChatColor.RED + "player not found."); return true; }
        double amt;
        try { amt = Double.parseDouble(args[1]); } catch (Exception e) { p.sendMessage(ChatColor.RED + "invalid amount."); return true; }
        if (amt <= 0) { p.sendMessage(ChatColor.RED + "amount must be positive."); return true; }
        if (!plugin.getMoney().withdraw(p.getUniqueId(), amt)) { p.sendMessage(ChatColor.RED + "not enough money."); return true; }
        plugin.getMoney().deposit(t.getUniqueId(), amt);
        p.sendMessage(ChatColor.GREEN + "paid " + amt + "$ to " + t.getName() + ".");
        t.sendMessage(ChatColor.GREEN + p.getName() + " paid you " + amt + "$.");
        return true;
    }
}