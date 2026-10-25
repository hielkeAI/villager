package com.dpshop.plugin.home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.dpshop.plugin.DpShopPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeManager {
    private final DpShopPlugin plugin;
    private final File file;
    private final Map<UUID, Map<String, Location>> homes = new HashMap<>();

    public HomeManager(DpShopPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "homes.yml");
        load();
    }

    private int limit() { return plugin.getConfig().getInt("homes.default-limit", 3); }

    public Map<String, Location> get(UUID u) { return homes.getOrDefault(u, new HashMap<>()); }

    public boolean set(org.bukkit.entity.Player player, String name, Location loc) {
        UUID u = player.getUniqueId();
        Map<String, Location> m = homes.computeIfAbsent(u, k -> new HashMap<>());
        if (!m.containsKey(name) && m.size() >= limit()) return false;
        loc = loc.clone(); loc.setWorld(loc.getWorld());
        m.put(name.toLowerCase(), loc);
        homes.put(u.uuid, m);
        save();
        return true;
    }

    public boolean teleport(org.bukkit.entity.Player p, String name) {
        Location l = get(p.getUniqueId()).get(name.toLowerCase());
        if (l == null || l.getWorld() == null) return false;
        p.teleport(l); return true;
    }

    public boolean delete(UUID u, String name) {
        Map<String, Location> m = homes.get(u);
        if (m == null || !m.containsKey(name.toLowerCase())) return false;
        m.remove(name.toLowerCase()); save(); return true;
    }

    public List<String> names(UUID u) { return new ArrayList<>(get(u).keySet()); }

    public void save() {
        FileConfiguration c = new YamlConfiguration();
        for (Map.Entry<UUID, Map<String, Location>> e : homes.entrySet()) {
            for (Map.Entry<String, Location> h : e.getValue().entrySet()) {
                Location l = h.getValue();
                String base = e.getKey() + "." + h.getKey();
                c.set(base + ".world", l.getWorld().getName());
                c.set(base + ".x", l.getX()); c.set(base + ".y", l.getY()); c.set(base + ".z", l.getZ());
                c.set(base + ".yaw", l.getYaw()); c.set(base + ".pitch", l.getPitch());
            }
        }
        try { c.save(file); } catch (IOException ex) { plugin.getLogger().warning(ex.getMessage()); }
    }

    private void load() {
        if (!file.exists()) return;
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        for (String u : c.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(u);
                Map<String, Location> m = new HashMap<>();
                org.bukkit.configuration.ConfigurationSection sec = c.getConfigurationSection(u);
                if (sec == null) continue;
                for (String h : sec.getKeys(false)) {
                    String w = c.getString(u + "." + h + ".world");
                    World world = Bukkit.getWorld(w);
                    if (world == null) continue;
                    Location l = new Location(world,
                        c.getDouble(u+"."+h+".x"), c.getDouble(u+"."+h+".y"), c.getDouble(u+"."+h+".z"),
                        (float) c.getDouble(u+"."+h+".yaw"), (float) c.getDouble(u+"."+h+".pitch"));
                    m.put(h.toLowerCase(), l);
                }
                homes.put(uuid, m);
            } catch (Exception ignored) {}
        }
    }

    // helper class
    public static class PlayerRef {
        public final UUID uuid;
        public PlayerRef(org.bukkit.entity.Player p) { this.uuid = p.getUniqueId(); }
    }
}