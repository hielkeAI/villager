execute if entity @s[nbt={Inventory:[{id:"minecraft"}]}] run function dpshop:shop/_sell_bread_do
execute unless entity @s[nbt={Inventory:[{id:"minecraft"}]}] run tellraw @s [{"text":"you have no bread to sell.","color":"red"}]
