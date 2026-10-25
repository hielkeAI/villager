package com.dpshop.plugin.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.lang.Lang;
import com.dpshop.plugin.villager.VillagerShop;
import com.dpshop.plugin.villager.VillagerShopManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VillagerShopListener implements Listener {
    public static final Map<UUID, UUID> lastShop = new HashMap<>(); // player -> villager
    private final DpShopPlugin plugin;

    public VillagerShopListener(DpShopPlugin plugin, com.dpshop.plugin.shop.ShopManager shop) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Entity ent = e.getRightClicked();
        if (!(ent instanceof Villager)) return;
        Player p = e.getPlayer();
        if (p.isSneaking()) return; // allow vanilla trade
        Villager v = (Villager) ent;
        e.setCancelled(true);
        VillagerShopManager mgr = plugin.getVillagerShops();
        VillagerShop s = mgr.getOrCreate(v);
        mgr.restockIfNeeded(s);
        lastShop.put(p.getUniqueId(), v.getUniqueId());
        // header
        int disc = s.discount(p.getName());
        p.sendMessage(ChatColor.GOLD + "Villager Shop" + (s.admin?" (Admin)":"") + " [" + s.profession + " lvl " + s.tier + "] (discount " + disc + "%)");
        int i=0;
        for (VillagerShop.Entry e2 : s.stock) {
            p.sendMessage(ChatColor.YELLOW + (i++) + ": " + e2.material.name() + " x" + e2.amount +
                "  " + ChatColor.GREEN + "B " + (e2.buy*(1-disc/100.0)) + "$" + ChatColor.GRAY + " / " + ChatColor.AQUA + "S " + e2.sell + "$");
        }
        p.sendMessage(ChatColor.GRAY + Lang.get("click.right.buy") + "  " + Lang.fmt("balance","b", plugin.getMoney().get(p.getUniqueId())));
        p.sendMessage(ChatColor.GRAY + "buy: /dpshop vbuy <index>   sell: /dpshop vsell <index>");
    }
}