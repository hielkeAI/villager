execute unless score @s dpshop.money matches 40.. run tellraw @s [{"text":"not enough money (need 40$).","color":"red"}]
execute if score @s dpshop.money matches 40.. run function dpshop:shop/_buy_gold_do
