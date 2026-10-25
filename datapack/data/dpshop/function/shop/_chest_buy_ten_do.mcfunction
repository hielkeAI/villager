scoreboard players remove @s dpshop.money 50
give @s minecraft:apple 10
tellraw @s [{"text":"bought 10 from chest shop. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
