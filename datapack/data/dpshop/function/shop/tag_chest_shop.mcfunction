execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run tellraw @s [{"text":"no permission.","color":"red"}]
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run return 0
# tag the chest the player is looking at
execute if block ~ ~ ~ minecraft:chest run tag @n[type=item_display,distance=..] add dpshop.dummy
# mark position by summoning an invisible marker
summon minecraft:marker ~ ~ ~ {Tags:["dpshop.chestshop"]}
tellraw @s [{"text":"marked current block as chest shop. place a sign on top and stock the chest.","color":"green"}]
