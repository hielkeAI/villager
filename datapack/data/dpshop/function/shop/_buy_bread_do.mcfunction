scoreboard players remove @s dpshop.money 8
give @s minecraft:bread 1
tellraw @s [{"text":"bought bread. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]