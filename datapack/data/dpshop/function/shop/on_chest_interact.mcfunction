advancement revoke @s only dpshop:shop/interact_chest

# only act if a sign with [Shop] is adjacent to the clicked chest (above or side)
# store clicked position
execute store result score @s dpshop.rc run random value 0..0

# simple: open chest shop dialog if player has permission check done in dialog
function dpshop:shop/open_chest_dialog