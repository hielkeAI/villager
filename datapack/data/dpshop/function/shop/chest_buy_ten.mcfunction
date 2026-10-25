execute unless score @s dpshop.money matches 50.. run tellraw @s [{"text":"not enough money (need 50$ for x10).","color":"red"}]
execute if score @s dpshop.money matches 50.. run function dpshop:shop/_chest_buy_ten_do
