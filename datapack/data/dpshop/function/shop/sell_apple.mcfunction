execute if entity @s[nbt={Inventory:[{id:"minecraft"}]}] run function dpshop:shop/_sell_apple_do
execute unless entity @s[nbt={Inventory:[{id:"minecraft"}]}] run tellraw @s [{"text":"you have no apple to sell.","color":"red"}]
