# Datapack Shops - load
scoreboard objectives add dpshop.money dummy "Money"
scoreboard objectives add dpshop.trigger trigger "Shop Trigger"
scoreboard objectives add dpshop.buy dummy "Buy Amount"
scoreboard objectives add dpshop.sell dummy "Sell Amount"
scoreboard objectives add dpshop.sneak minecraft.custom:minecraft.sneak_time "Sneak"
scoreboard objectives add dpshop.rc dummy "RC Cooldown"

# default money for new players
execute as @a unless score @s dpshop.money matches -2147483648..2147483647 run scoreboard players set @s dpshop.money 500

tellraw @a [{"text":"[dpshops] ","color":"gold"},{"text":"loaded. right-click a villager (named [shop]) or a chest with a [shop] sign to trade.","color":"gray"}]