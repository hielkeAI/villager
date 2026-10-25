tag @s add dpshop.admin_check
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run tellraw @s [{"text":"no permission.","color":"red"}]
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run return 0
scoreboard players add @s dpshop.money 100
tellraw @s [{"text":"+100$. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
