package com.dpshop.plugin.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.home.HomeManager;

public class HomeCommand implements CommandExecutor {
    private final DpShopPlugin plugin;
    private final HomeManager homes;

    public HomeCommand(DpShopPlugin plugin, HomeManager homes) {
        this.plugin = plugin;
        this.homes = homes;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("players only."); return true; }
        Player p = (Player) sender;
        String c = cmd.getName().toLowerCase();
        if (c.equals("sethome") || c.equals("gosodcartref")) {
            String name = (args.length > 0) ? args[0] : "home";
            boolean ok = homes.set(p, name, p.getLocation());
            if (ok) p.sendMessage(ChatColor.GREEN + "home '" + name + "' set.");
            else p.sendMessage(ChatColor.RED + "home limit reached. delete one first.");
            return true;
        }
        if (c.equals("delhome") || c.equals("dileucartref") || c.equals("delcartref")) {
            if (args.length == 0) { p.sendMessage(ChatColor.RED + "usage: /delhome <name>"); return true; }
            if (homes.delete(p.getUniqueId(), args[0])) p.sendMessage(ChatColor.GREEN + "home deleted.");
            else p.sendMessage(ChatColor.RED + "no home named " + args[0]);
            return true;
        }
        if (c.equals("homes") || c.equals("cartrefau")) {
            p.sendMessage(ChatColor.GOLD + "homes: " + String.join(", ", homes.names(p.getUniqueId())));
            return true;
        }
        // /home or /cartref
        String name = (args.length > 0) ? args[0] : "home";
        if (homes.teleport(p, name)) p.sendMessage(ChatColor.GREEN + "teleported to " + name + ".");
        else p.sendMessage(ChatColor.RED + "no home named " + name);
        return true;
    }
}