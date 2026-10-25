execute unless score @s dpshop.money matches 100.. run tellraw @s [{"text":"not enough money (need 100$).","color":"red"}]
execute if score @s dpshop.money matches 100.. run function dpshop:shop/_buy_diamond_do
