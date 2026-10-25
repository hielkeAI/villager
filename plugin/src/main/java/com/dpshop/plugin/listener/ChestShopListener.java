package com.dpshop.plugin.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.shop.ShopManager;

public class ChestShopListener implements Listener {
    private final DpShopPlugin plugin;
    private final ShopManager shop;

    public ChestShopListener(DpShopPlugin plugin, ShopManager shop) {
        this.plugin = plugin;
        this.shop = shop;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = e.getClickedBlock();
        if (b == null) return;
        // chest shop: sign attached (on top) with [Shop] on line 0/1
        Block signBlock = b.getRelative(org.bukkit.block.BlockFace.UP);
        if (signBlock.getState() instanceof Sign && isShopSign((Sign) signBlock.getState())) {
            e.setCancelled(true);
            shop.openChestShop(e.getPlayer());
            return;
        }
        if (b.getType() == Material.CHEST && hasAdjacentShopSign(b)) {
            e.setCancelled(true);
            shop.openChestShop(e.getPlayer());
        }
    }

    private boolean hasAdjacentShopSign(Block chest) {
        for (org.bukkit.block.BlockFace f : new org.bukkit.block.BlockFace[]{org.bukkit.block.BlockFace.UP, org.bukkit.block.BlockFace.NORTH, org.bukkit.block.BlockFace.SOUTH, org.bukkit.block.BlockFace.EAST, org.bukkit.block.BlockFace.WEST}) {
            Block s = chest.getRelative(f);
            if (s.getState() instanceof Sign && isShopSign((Sign) s.getState())) return true;
        }
        return false;
    }

    private boolean isShopSign(Sign sign) {
        String line0 = sign.getLine(0);
        return line0.equalsIgnoreCase("[Shop]") || line0.equalsIgnoreCase("[shop]");
    }
}