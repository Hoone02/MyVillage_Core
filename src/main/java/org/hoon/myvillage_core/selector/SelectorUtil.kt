package org.hoon.myvillage_core.selector

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.hoon.myvillage_core.util.BlockUtil

fun placeSelector(selector: Selector, player: Player) : Location {
    val location = selector.pointEntity!!.location
    SelectorManager.remove(SelectorManager.get(player)!!)

    return location
}

fun tempPlaceSelector(selector: Selector, player: Player) : Location {
    val location = selector.pointEntity!!.location
    SelectorTask.selectorTask[player]?.cancel()
    return location
}

fun changeDirection(selector: Selector, string: String): Double {
    val direction = selector.direction

    return when (string) {
        "오른쪽" -> when (direction) {
            0.0 -> 90.0
            90.0 -> 180.0
            180.0 -> 270.0
            270.0 -> 0.0
            else -> direction // 예상치 못한 값의 경우 현재 방향을 유지
        }
        "왼쪽" -> when (direction) {
            0.0 -> 270.0
            90.0 -> 0.0
            180.0 -> 90.0
            270.0 -> 180.0
            else -> direction // 예상치 못한 값의 경우 현재 방향을 유지
        }
        else -> direction // "오른쪽" 또는 "왼쪽"이 아닌 경우 현재 방향을 유지
    }
}

fun resultLocation(blockFace: BlockFace) : Vector {
    return when (blockFace) {
        BlockFace.NORTH -> {
            Vector(0.0, 0.0, -1.0)
        }

        BlockFace.SOUTH -> {
            Vector(0.0, 0.0, 1.0)
        }

        BlockFace.EAST -> {
            Vector(1.0, 0.0, 0.0)
        }

        BlockFace.WEST -> {
            Vector(-1.0, 0.0, 0.0)
        }

        BlockFace.DOWN -> {
            Vector(0.0, -1.0, 0.0)
        }
        else -> {
            Vector(0.0, 1.0, 0.0)
        }
    }
}
