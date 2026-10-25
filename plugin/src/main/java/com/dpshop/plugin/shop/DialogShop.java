package com.dpshop.plugin.shop;

import org.bukkit.entity.Player;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.economy.MoneyManager;
import java.util.List;

/**
 * Placeholder for Paper 1.21.6+ Dialog GUI integration.
 *
 * The Dialog API lives in io.papermc.paper.dialog.* and is only present on
 * Paper 1.21.6+. Because method names differ between builds, this class is kept
 * as a no-op that safely falls back to the chat-based menu. Wire it up by
 * replacing each method with the concrete Dialog construction for your Paper
 * build (see README).
 */
public final class DialogShop {
    private DialogShop() {}

    public static boolean openPlayer(DpShopPlugin p, Player player, List<ShopManager.ShopItem> items, MoneyManager money) { return false; }
    public static boolean openAdmin(DpShopPlugin p, Player player, MoneyManager money) { return false; }
    public static boolean openChest(DpShopPlugin p, Player player, MoneyManager money) { return false; }
}