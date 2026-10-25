scoreboard players remove @s dpshop.money 100
give @s minecraft:diamond 1
tellraw @s [{"text":"bought diamond. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
