clear @s minecraft 1
scoreboard players add @s dpshop.money 60
tellraw @s [{"text":"sold 1 diamond. balance: ","color":"green"},{"score":{"name":"@s","objective":"dpshop.money"},"color":"gold"},{"text":"$","color":"gold"}]
