package com.dpshop.plugin.tpa;

import com.dpshop.plugin.DpShopPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaManager {
    public static final int TPA = 0, TPAHERE = 1;
    public static class Request { public UUID from, to; public int type; public long time; public Request(UUID f, UUID t, int type){from=f;to=t;this.type=type;time=System.currentTimeMillis();} }
    private final DpShopPlugin plugin;
    private final Map<UUID, Request> pendingTo = new HashMap<>(); // target -> request

    public TpaManager(DpShopPlugin plugin) { this.plugin = plugin; }

    private long timeoutMs() { return plugin.getConfig().getInt("tpa.timeout", 60) * 1000L; }

    public void request(UUID from, UUID to, int type) {
        pendingTo.put(to, new Request(from, to, type));
        // schedule expiry
        new BukkitRunnable() { public void run() {
            Request r = pendingTo.get(to);
            if (r != null && r.from.equals(from) && System.currentTimeMillis()-r.time >= timeoutMs()) pendingTo.remove(to);
        }}.runTaskLater(plugin, plugin.getConfig().getInt("tpa.timeout",60)*20L);
    }

    public Request get(UUID to) { return pendingTo.get(to); }
    public void clear(UUID to) { pendingTo.remove(to); }
}