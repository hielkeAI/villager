scoreboard players remove @s dpshop.money 20
give @s minecraft:iron_ingot 1
tellraw @s [{"text":"bought iron ingot. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
