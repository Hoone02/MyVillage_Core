package org.hoon.myvillage_core.util

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.hoon.myvillage_core.MyVillage_Core

fun Player.setHideEntity(entity: Entity) {
    val players = Bukkit.getOnlinePlayers()
    players.forEach {
        player ->
        if (player != this) {
            player.hideEntity(MyVillage_Core.instance, entity)
        }
    }
}