scoreboard players remove @s dpshop.money 40
give @s minecraft:gold_ingot 1
tellraw @s [{"text":"bought gold ingot. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
