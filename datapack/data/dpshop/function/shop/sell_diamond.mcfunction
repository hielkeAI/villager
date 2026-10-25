execute if entity @s[nbt={Inventory:[{id:"minecraft"}]}] run function dpshop:shop/_sell_diamond_do
execute unless entity @s[nbt={Inventory:[{id:"minecraft"}]}] run tellraw @s [{"text":"you have no diamond to sell.","color":"red"}]
