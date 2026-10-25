execute unless score @s dpshop.money matches 50.. run tellraw @s [{"text":"not enough money (need 50$).","color":"red"}]
execute if score @s dpshop.money matches 50.. run function dpshop:shop/_buy_pearl_do
