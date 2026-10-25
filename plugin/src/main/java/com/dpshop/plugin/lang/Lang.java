package com.dpshop.plugin.lang;

import org.bukkit.configuration.file.FileConfiguration;
import com.dpshop.plugin.DpShopPlugin;

import java.util.*;

public class Lang {
    private static final Map<String, Map<String, String>> T = new HashMap<>();
    static {
        Map<String,String> en = new HashMap<>();
        en.put("shop.created","Shop created.");
        en.put("shop.removed","Shop removed.");
        en.put("no.perm","No permission.");
        en.put("bought","Bought {n} {item} for {p}$.");
        en.put("sold","Sold {n} {item} for {p}$.");
        en.put("not.enough.money","Not enough money.");
        en.put("not.enough.stock","Not enough stock.");
        en.put("not.enough.items","You don't have the item.");
        en.put("info","Owner: {owner} | Stock: {stock} | {amount}x {item} | B: {buy}$ S: {sell}$");
        en.put("admin.shop","Admin shop (infinite).");
        en.put("no.shop","Not a shop.");
        en.put("buy.disabled","Buying disabled.");
        en.put("sell.disabled","Selling disabled.");
        en.put("click.right.buy","Right-click: buy. Left-click: sell.");
        en.put("balance","Balance: {b}$");
        en.put("created.hint","Sign line 2=amount, line3=B buy:S sell, line4=item (auto).");
        Map<String,String> cy = new HashMap<>();
        cy.put("shop.created","Siop wedi'i chreu.");
        cy.put("shop.removed","Siop wedi'i dileu.");
        cy.put("no.perm","Dim caniatâd.");
        cy.put("bought","Prynwyd {n} {item} am {p}$.");
        cy.put("sold","Gwerthwyd {n} {item} am {p}$.");
        cy.put("not.enough.money","Dim digon o arian.");
        cy.put("not.enough.stock","Dim digon mewn stoc.");
        cy.put("not.enough.items","Nid oes gennych yr eitem.");
        cy.put("info","Perchennog: {owner} | Stoc: {stock} | {amount}x {item} | P: {buy}$ G: {sell}$");
        cy.put("admin.shop","Siop weinydd (digon).");
        cy.put("no.shop","Nid siop ydyw.");
        cy.put("buy.disabled","Prynu wedi'i analluogi.");
        cy.put("sell.disabled","Gwerthu wedi'i analluogi.");
        cy.put("click.right.buy","De-glic: prynu. Chwith-glic: gwerthu.");
        cy.put("balance","Balans: {b}$");
        cy.put("created.hint","Llinell 2=swm, llinell3=P prynu:G gwerthu, llinell4=eitem (awto).");
        T.put("en", en); T.put("cy", cy);
    }
    public static String lang() {
        try { String l = DpShopPlugin.get().getConfig().getString("lang", "cy"); if (l==null) l="cy"; return l.toLowerCase(); } catch (Exception e) { return "cy"; }
    }
    public static String get(String key) {
        Map<String,String> m = T.getOrDefault(lang(), T.get("en"));
        String s = m.getOrDefault(key, key);
        return s;
    }
    public static String fmt(String key, Object... kv) {
        String s = get(key);
        for (int i=0;i+1<kv.length;i+=2) {
            s = s.replace("{"+kv[i]+"}", String.valueOf(kv[i+1]));
        }
        return s;
    }
}