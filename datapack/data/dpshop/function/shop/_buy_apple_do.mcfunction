scoreboard players remove @s dpshop.money 5
give @s minecraft:apple 1
tellraw @s [{"text":"bought apple. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]