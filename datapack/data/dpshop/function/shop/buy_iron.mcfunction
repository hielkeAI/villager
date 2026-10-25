execute unless score @s dpshop.money matches 20.. run tellraw @s [{"text":"not enough money (need 20$).","color":"red"}]
execute if score @s dpshop.money matches 20.. run function dpshop:shop/_buy_iron_do
