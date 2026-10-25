# ensure money scoreboard exists for new players
execute as @a unless score @s dpshop.money matches -2147483648..2147483647 run scoreboard players set @s dpshop.money 500

# handle trigger-based commands
execute as @a[scores={dpshop.trigger=1..}] run function dpshop:shop/trigger_dispatch
scoreboard players set @a dpshop.trigger 0