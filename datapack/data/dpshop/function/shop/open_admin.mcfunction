# require OP or dpshop.admin tag; for vanilla datapack we check tag
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run tellraw @s [{"text":"no permission.","color":"red"}]
execute unless entity @s[tag=dpshop.admin] unless entity @s[tag=op] run return 0
dialog show @s dpshop:shop_admin