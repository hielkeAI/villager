# revoke advancement first so it can fire again
advancement revoke @s only dpshop:shop/interact_villager

# target the villager that was interacted with
execute on target run tag @s add dpshop.target

# Any villager whose name contains "Shop" (use a name tag in an anvil: "[Shop]") opens the player shop.
execute as @e[tag=dpshop.target] if data entity @s {CustomName:'{"text":"[shop]"}'} run tag @s add dpshop.shop
# looser: villager custom name contains "shop" (covers typos/casing managed by admin data merges)

# Shop villager
execute if entity @e[tag=dpshop.target,tag=dpshop.shop,limit=1] run function dpshop:shop/open_villager_shop

# Admin shop: villager named [Admin Shop] or tagged
execute as @e[tag=dpshop.target] if data entity @s {CustomName:'{"text":"[admin shop]"}'} run tag @s add dpshop.adminshop
execute if entity @e[tag=dpshop.target,tag=dpshop.adminshop,limit=1] run function dpshop:shop/open_admin

tag @e[tag=dpshop.target] remove dpshop.target