scoreboard players remove @s dpshop.money 50
give @s minecraft:ender_pearl 1
tellraw @s [{"text":"bought ender pearl. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
