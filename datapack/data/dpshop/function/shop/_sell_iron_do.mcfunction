clear @s minecraft 1
scoreboard players add @s dpshop.money 12
tellraw @s [{"text":"sold 1 iron. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
