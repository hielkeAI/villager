# admin helper: /function dpshop:shop/grant_money {amount:100}
$scoreboard players add @p dpshop.money $(amount)
$tellraw @p [{"text":"+$(amount)$ granted. balance: ","color":"green"},{"score":{"name":"@p","objective":"dpshop.money"},"color":"gold"}]