execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run tellraw @s [{"text":"no permission.","color":"red"}]
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run return 0
tellraw @s [{"text":"to refill: open the chest behind the sign and place items inside. players can then buy via the chest shop dialog.","color":"gray"}]
