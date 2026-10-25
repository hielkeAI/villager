package com.dpshop.plugin.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.dpshop.plugin.DpShopPlugin;
import com.dpshop.plugin.lang.Lang;
import com.dpshop.plugin.shop.ChestShop;
import com.dpshop.plugin.shop.ChestShopManager;

public class ChestShopSignListener implements Listener {
    private final DpShopPlugin plugin;
    private final ChestShopManager mgr;

    public ChestShopSignListener(DpShopPlugin plugin, ChestShopManager mgr) {
        this.plugin = plugin;
        this.mgr = mgr;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent e) {
        String line0 = e.getLine(0).trim().toLowerCase();
        if (!(line0.equals("shop") || line0.equals("[shop]") || line0.equals("admin") || line0.equals("[admin]"))) return;
        Block signBlock = e.getBlock();
        Block chest = findAdjacentChest(signBlock);
        if (chest == null) {
            e.getPlayer().sendMessage(Lang.get("no.shop"));
            return;
        }
        boolean admin = line0.contains("admin");
        Player p = e.getPlayer();
        // determine material from chest first item
        Chest c = (Chest) chest.getState();
        Inventory inv = c.getInventory();
        Material mat = null; int amt = 1;
        for (ItemStack is : inv.getContents()) { if (is != null && is.getType() != Material.AIR) { mat = is.getType(); amt = Math.max(1, is.getAmount()); break; } }
        if (mat == null) mat = Material.valueOf(plugin.getConfig().getString("chest-shop.default-item","APPLE"));
        double price = plugin.getConfig().getDouble("chest-shop.default-price", 5);
        double buy = price, sell = price/2.0;
        int qty = amt;
        ChestShop shop = new ChestShop(chest.getWorld().getName(), chest.getX(), chest.getY(), chest.getZ(),
            admin ? null : p.getUniqueId(), admin ? "Admin" : p.getName(), admin, mat, qty, buy, sell);
        mgr.add(shop);
        e.setLine(0, shop.ownerName);
        e.setLine(1, String.valueOf(qty));
        e.setLine(2, "B " + buy + " : S " + sell);
        e.setLine(3, mat.name());
        p.sendMessage(Lang.get("shop.created"));
    }

    private Block findAdjacentChest(Block signBlock) {
        for (org.bukkit.block.BlockFace f : new org.bukkit.block.BlockFace[]{org.bukkit.block.BlockFace.DOWN, org.bukkit.block.BlockFace.UP, org.bukkit.block.BlockFace.NORTH, org.bukkit.block.BlockFace.SOUTH, org.bukkit.block.BlockFace.EAST, org.bukkit.block.BlockFace.WEST}) {
            Block b = signBlock.getRelative(f);
            if (b.getState() instanceof Chest) return b;
        }
        return null;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        Block b = e.getClickedBlock();
        ChestShop shop = mgr.get(b);
        boolean bySign = false;
        if (shop == null) { shop = mgr.getBySign(b); bySign = (shop != null); }
        if (shop == null) return;
        Player p = e.getPlayer();
        if (p.isSneaking()) return; // allow chest open
        e.setCancelled(true);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            buy(p, shop);
        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            sell(p, shop);
        }
        p.sendMessage(Lang.fmt("info", "owner", shop.ownerName, "stock", stockCount(shop), "amount", shop.amount, "item", shop.material.name(), "buy", shop.buyPrice, "sell", shop.sellPrice));
    }

    private void buy(Player p, ChestShop shop) {
        if (shop.buyPrice <= 0) { p.sendMessage(Lang.get("buy.disabled")); return; }
        double tax = plugin.getConfig().getDouble("shop.tax-percent", 0);
        double finalPrice = shop.buyPrice + (shop.buyPrice * tax / 100.0);
        if (!plugin.getMoney().withdraw(p.getUniqueId(), finalPrice)) { p.sendMessage(Lang.get("not.enough.money")); return; }
        // stock check
        if (!shop.admin) {
            Block chestBlock = Bukkit.getWorld(shop.world).getBlockAt(shop.x, shop.y, shop.z);
            Chest c = (Chest) chestBlock.getState();
            if (!removeFromChest(c.getInventory(), shop.material, shop.amount)) {
                plugin.getMoney().deposit(p.getUniqueId(), finalPrice);
                p.sendMessage(Lang.get("not.enough.stock"));
                return;
            }
        }
        p.getInventory().addItem(new ItemStack(shop.material, shop.amount));
        // earnings to owner if player shop
        if (!shop.admin && shop.owner != null && !shop.owner.equals(p.getUniqueId())) {
            plugin.getMoney().deposit(shop.owner, shop.buyPrice);
        }
        p.sendMessage(Lang.fmt("bought", "n", shop.amount, "item", shop.material.name(), "p", finalPrice));
    }

    private void sell(Player p, ChestShop shop) {
        if (shop.sellPrice <= 0) { p.sendMessage(Lang.get("sell.disabled")); return; }
        Inventory inv = p.getInventory();
        if (!contains(inv, shop.material, shop.amount)) { p.sendMessage(Lang.get("not.enough.items")); return; }
        // remove from player
        removeItems(inv, shop.material, shop.amount);
        // add to chest if not admin
        if (!shop.admin) {
            Block chestBlock = Bukkit.getWorld(shop.world).getBlockAt(shop.x, shop.y, shop.z);
            Chest c = (Chest) chestBlock.getState();
            c.getInventory().addItem(new ItemStack(shop.material, shop.amount));
        }
        plugin.getMoney().deposit(p.getUniqueId(), shop.sellPrice);
        if (!shop.admin && shop.owner != null && !shop.owner.equals(p.getUniqueId())) {
            // owner loses money (pay seller); ignore failure
            plugin.getMoney().withdraw(shop.owner, shop.sellPrice);
        }
        p.sendMessage(Lang.fmt("sold", "n", shop.amount, "item", shop.material.name(), "p", shop.sellPrice));
    }

    private boolean removeFromChest(Inventory inv, Material m, int amt) {
        int rem = amt;
        for (int i=0;i<inv.getSize();i++){
            ItemStack is = inv.getItem(i);
            if (is != null && is.getType()==m) {
                int take = Math.min(is.getAmount(), rem);
                is.setAmount(is.getAmount()-take);
                if (is.getAmount()<=0) inv.setItem(i,null);
                rem -= take;
                if (rem<=0) return true;
            }
        }
        // rollback? if not enough, items partially removed; accept
        return rem<=0;
    }
    private boolean contains(Inventory inv, Material m, int amt){
        int c=0;
        for (ItemStack is : inv.getContents()) if (is!=null && is.getType()==m) c+=is.getAmount();
        return c>=amt;
    }
    private void removeItems(Inventory inv, Material m, int amt){
        int rem=amt;
        for (int i=0;i<inv.getSize() && rem>0;i++){
            ItemStack is = inv.getItem(i);
            if (is!=null && is.getType()==m) {
                int take = Math.min(is.getAmount(), rem);
                is.setAmount(is.getAmount()-take);
                if (is.getAmount()<=0) inv.setItem(i,null);
                rem-=take;
            }
        }
    }
    private int stockCount(ChestShop shop){
        if (shop.admin) return 9999;
        Block chestBlock = Bukkit.getWorld(shop.world).getBlockAt(shop.x, shop.y, shop.z);
        Chest c = (Chest) chestBlock.getState();
        int c2=0;
        for (ItemStack is : c.getInventory().getContents()) if (is!=null && is.getType()==shop.material) c2+=is.getAmount();
        return c2;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        ChestShop shop = mgr.get(b);
        if (shop == null) shop = mgr.getBySign(b);
        if (shop == null) return;
        Player p = e.getPlayer();
        boolean owner = (shop.owner != null && shop.owner.equals(p.getUniqueId())) || p.hasPermission("dpshop.admin");
        if (!owner) { e.setCancelled(true); p.sendMessage(Lang.get("no.perm")); return; }
        if (b.getState() instanceof Sign) {
            // removing sign disables shop? remove shop when chest broken only; leaving sign harmless. Remove if chest gone.
        }
        if (b.getState() instanceof Chest) mgr.removeByChest(b);
        p.sendMessage(Lang.get("shop.removed"));
    }
}