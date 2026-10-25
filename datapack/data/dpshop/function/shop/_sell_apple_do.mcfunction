clear @s minecraft 1
scoreboard players add @s dpshop.money 3
tellraw @s [{"text":"sold 1 apple. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
