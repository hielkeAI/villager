execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run tellraw @s [{"text":"no permission.","color":"red"}]
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run return 0
execute as @e[type=minecraft:villager,limit=1,sort=nearest,distance=..5] run tag @s add dpshop.shop
execute as @e[type=minecraft:villager,limit=1,sort=nearest,distance=..5] run data merge entity @s {CustomName:'{"text":"[shop]"}',CustomNameVisible:1b}
tellraw @s [{"text":"nearest villager tagged as shop.","color":"green"}]
