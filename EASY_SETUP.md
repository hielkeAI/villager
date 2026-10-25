# 🛒 Villager Shops — Super Easy Setup (for Ysgol y Deri)

You only need to do these steps once. Take your time — read one step at a time. 🙂

## Step 1 — Install the datapack
1. Find your Minecraft **world** folder.
   - Usually: `%appdata%\.minecraft\saves\YOUR-WORLD\`
2. Open the folder called `datapacks`.
   (If it is not there, make a new folder and name it `datapacks`.)
3. Copy the folder named **`datapack`** (inside this download) into `datapacks`.
4. Open Minecraft and join your world.
5. Type in chat:  `/reload`   (then press Enter)
   - You should see a gold message: **"[DPShops] loaded..."**

✅ Done with Step 1.

## Step 2 — Make a villager shop (no commands needed!)
1. Make a **Name Tag** in survival (or use creative).
2. Put the name tag in an **Anvil**.
3. Rename it to exactly:  `[Shop]`
4. Go to a villager and **right-click** them with the name tag.
   - The villager will now be called **[Shop]**.
5. Any player who right-clicks that villager opens the shop. 🎉

## Step 3 — Make an admin / money shop
1. Rename a name tag to `[Admin Shop]` in an anvil.
2. Right-click a villager with it.
3. Right-click that villager to open admin tools (only OPs / admins).

Money starts at **500** for everyone automatically.

## Step 4 — Chest shops (ChestShop style, super easy!)
1. Place a **Chest**.
2. Put items inside the chest.
3. Place a **Sign** on the chest (on top or side).
4. Write on line 1:  `shop`   (or `admin` for infinite stock)
   - The sign auto-fills the rest (owner, amount, price B/S, item).
5. Done! Right-click chest = buy, left-click = sell. Shift right-click = open chest.

## Step 5 — See help in the game
Type in chat (if you are OP):
```
/function dpshop:shop/open_help
```
A help screen appears with buttons.

---

## ❓ Problems?
- "Unknown command" → You need to be on Minecraft Java **1.21.6 or newer** for the new shop screens.
- Shop not opening → Make sure the villager name is exactly `[Shop]` (with the square brackets).
- Lost money? Money is saved to the world automatically when you `/reload` or quit.

Language: set `lang: cy` or `lang: en` in `plugins/DpShop/config.yml`.

That's it! Have fun with your shops. 💛

### extra features (plugin version)
- /home /sethome /delhome /homes  (+ Welsh: /cartref /gosodcartref /dileucartref /cartrefau)
- /rtp  (random teleport)
- /tpa /tpahere /tpaccept /tpdeny  (+ Welsh: /galw /galwyfan /derbyn /gwrthod)
- /pay <player> <amount>
- 0% tax by default; admins change `shop.tax-percent` in config.yml

---

## 🏗️ The "Plugin" version (for teachers / grown-ups)
There is also a `plugin/` folder. It needs Java 21 + Gradle to build.
If that sounds scary, **stick with the datapack above** — it does the same job
with no installing of software. Ask a grown-up or an IT helper if you want the plugin.