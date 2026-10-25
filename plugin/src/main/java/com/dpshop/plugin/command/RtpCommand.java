package com.dpshop.plugin.command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.rtp.RtpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RtpCommand implements CommandExecutor {
    private final DpShopPlugin plugin;
    private final Map<UUID, Long> cd = new HashMap<>();

    public RtpCommand(DpShopPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("players only."); return true; }
        Player p = (Player) sender;
        int cool = plugin.getConfig().getInt("rtp.cooldown", 60);
        long now = System.currentTimeMillis();
        Long last = cd.get(p.getUniqueId());
        if (last != null && now - last < cool * 1000L) {
            long wait = (cool * 1000L - (now - last)) / 1000L;
            p.sendMessage(ChatColor.RED + "rtp cooldown: " + wait + "s");
            return true;
        }
        World w = p.getWorld();
        int radius = plugin.getConfig().getInt("rtp.radius", 5000);
        int tries = plugin.getConfig().getInt("rtp.max-tries", 30);
        p.sendMessage(ChatColor.YELLOW + "finding safe location...");
        org.bukkit.Location loc = RtpUtil.findSafe(w, radius, tries);
        if (loc == null) { p.sendMessage(ChatColor.RED + "could not find safe location, try again."); return true; }
        p.teleport(loc);
        cd.put(p.getUniqueId(), now);
        p.sendMessage(ChatColor.GREEN + "teleported to a random location.");
        return true;
    }
}