package org.hoon.myvillage_core.protection

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.hoon.myvillage_core.util.setHideEntity
import org.hoon.myvillage_core.util.updateTransformation

object ProtectionRangeViewer {

    private val entityList = mutableMapOf<Player, MutableList<Entity>>()

    fun view(player: Player) {
        if (ProtectionManager.getList().isEmpty()) return
        for (p in ProtectionManager.getList()) {
            val location = p.cantorPoint.clone().add(-5.5, -0.5, -5.5)
            val entity = player.world.spawn(location, BlockDisplay::class.java)
            if (p.owner == player.uniqueId.toString()) {
                entity.block = Bukkit.createBlockData(Material.LIME_STAINED_GLASS)
            } else {
                entity.block = Bukkit.createBlockData(Material.YELLOW_STAINED_GLASS)
            }

            entityList[player]?.add(entity) ?: entityList.put(player, mutableListOf(entity))

            entity.updateTransformation {
                scale {
                    x = 11f
                    y = 0.5f
                    z = 11f
                }
            }
            player.setHideEntity(entity)
        }
    }

    fun remove(player: Player) {
        if (entityList[player] == null) return
        for (entity in entityList[player]!!) {
            entity.remove()
        }
        entityList.remove(player)
    }
}