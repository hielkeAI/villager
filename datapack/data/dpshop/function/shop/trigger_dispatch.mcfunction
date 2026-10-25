# dispatch on trigger value
execute if score @s dpshop.trigger matches 1 run function dpshop:shop/open_main
execute if score @s dpshop.trigger matches 2 run function dpshop:shop/open_admin
execute if score @s dpshop.trigger matches 3 run function dpshop:shop/open_chest