# player sells 1 apple back for 3$
execute if entity @s[nbt={Inventory:[{id:"minecraft:apple"}]}] run function dpshop:shop/_chest_sell_do
execute unless entity @s[nbt={Inventory:[{id:"minecraft:apple"}]}] run tellraw @s [{"text":"you have no apple to sell.","color":"red"}]
