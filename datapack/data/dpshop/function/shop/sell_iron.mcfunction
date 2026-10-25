execute if entity @s[nbt={Inventory:[{id:"minecraft"}]}] run function dpshop:shop/_sell_iron_do
execute unless entity @s[nbt={Inventory:[{id:"minecraft"}]}] run tellraw @s [{"text":"you have no iron to sell.","color":"red"}]
