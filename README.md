# Villager & Chest Shops

Two deliverables:

## 1. Datapack (uses the 1.21.11 Dialog GUI)
Folder: `datapack/`

Install: drop `datapack/` into `world/datapacks/` and `/reload`. (Requires Minecraft Java 1.21.6+ for the new dialog screens.)

Setup:
- Grant yourself admin: `/tag @s add dpshop.admin` (or run as OP).
- Tag a villager as a shop: open admin shop dialog via right-clicking a villager tagged `dpshop.adminshop`, or run `/function dpshop:shop/tag_villager_shop` near one.
- Admin shop lets you tag villagers and grant money.

Currency: `dpshop.money` scoreboard. New players start with 100$. Adjust in `datapack/data/dpshop/function/load.mcfunction`.

Chest shops: place a chest, put a sign on top/side containing `[Shop]` on the first line, stock the chest, right-click the chest to open the chest shop dialog.

## 2. Paper Plugin
Folder: `plugin/` (Gradle project).

Build: `./gradlew build` (requires Java 21 + Paper dev bundle). Target Paper 1.21.6+.

Features:
- Right-click tagged villager → shop dialog (falls back to chat menu + `/dpshop buy`).
- Chest with `[Shop]` sign → chest shop.
- `/dpshop shop|admin|chest|buy|MATERIAL|money|grant|tag`.
- Vault economy supported if present; else internal balances.yml.
- Permissions: `dpshop.admin` (default op), `dpshop.use` (default true). Works with LuckPerms.

## Dialog GUI wiring (Paper)
`plugin/src/main/java/com/dpshop/plugin/shop/DialogShop.java` is currently a no-op fallback.
To enable real dialogs, implement each method using your Paper build's
`io.papermc.paper.dialog.Dialog` API (multi-action dialog with buttons per item).