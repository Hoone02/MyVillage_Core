package org.hoon.myvillage_core.selector

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.hoon.myvillage_core.protection.ProtectionManager
import org.hoon.myvillage_core.util.BlockUtil
import org.hoon.myvillage_core.util.isOdd
import org.hoon.myvillage_core.util.rotateY

class SelectorHandler (private val selector: Selector) {

    private val player = selector.player

    // 선택기 상태 업데이트
    fun updateSelectorState(entity2: Entity, entity: Entity) {
        val location = getTargetBlockLocation()

        if (location != null) {
            movingSelector(location, entity, entity2)

            val isCheck = checkBlockStates(selector, player)
            selector.isCheck = isCheck
            updateSelectorState(isCheck)
        } else {
            selector.isCheck = false
            updateSelectorState(false)
        }
    }

    // 플레이어가 바라보는 블록 위치 검사
    private fun getTargetBlockLocation(): Location? {
        return player.getTargetBlockExact(10)?.location
    }

    // 블록 상태 검사
    private fun checkBlockStates(selector: Selector, player: Player): Boolean {
        val isTargetBlockUp = player.getTargetBlockFace(10) == BlockFace.UP
        val isPointEntityInAir = selector.pointEntity!!.location.block.type == Material.AIR
        val areBlocksInSelectedRangeAir = BlockUtil.getBlocksInArea(selector.rangeLoc!!.first, selector.rangeLoc!!.second).all { it.type == Material.AIR }
        val areBlocksBelowSelectedRangeNotAir = BlockUtil.getBlocksInArea(
            selector.rangeLoc!!.first.clone().add(0.0, -1.0, 0.0),
            selector.rangeLoc!!.second.clone().add(0.0, -selector.rangeY - 1.0, 0.0)
        ).all { it.type != Material.AIR }

        val isOverlap = isOverlap()
        val isOverlapRange = isOverlapRange(player)

        player.sendMessage("$areBlocksBelowSelectedRangeNotAir")
        return isTargetBlockUp && isPointEntityInAir && areBlocksInSelectedRangeAir && areBlocksBelowSelectedRangeNotAir && !isOverlap && !isOverlapRange
    }

    private fun isOverlap() : Boolean {
        var isOverlap = false
        for (sel in SelectorManager.getList()) {
            if (sel == selector) continue
            if (sel.rangeLoc == null) return false
            isOverlap = BlockUtil.checkOverlap(
                sel.rangeLoc!!.first,
                sel.rangeLoc!!.second,
                selector.rangeLoc!!.first,
                selector.rangeLoc!!.second
            )
        }
        return isOverlap
    }

    private fun isOverlapRange(player: Player) : Boolean {
        var isOverlap = false
        for (protection in ProtectionManager.getList()) {
            if (protection.owner == player.uniqueId.toString()) return false
            isOverlap = BlockUtil.checkOverlap(
                protection.firstPoint,
                protection.secondPoint,
                selector.rangeLoc!!.first,
                selector.rangeLoc!!.second
            )
        }

        return isOverlap
    }

    // 선택기 상태 업데이트
    private fun updateSelectorState(isCheck: Boolean) {
        if (!isCheck) {
            selector.pointEntity?.block = Bukkit.createBlockData(selector.nonPlaceable)
            selector.selectorEntity?.block = Bukkit.createBlockData(Material.RED_STAINED_GLASS)
        } else {
            selector.pointEntity?.block = Bukkit.createBlockData(selector.placeable)
            selector.selectorEntity?.block = Bukkit.createBlockData(Material.LIME_STAINED_GLASS)
            selector.location = selector.pointEntity?.location
        }
    }

    private fun movingSelector(location : Location, entity: Entity, entity2: Entity) {
        val player = selector.player

        val resultLocation = resultLocation(player.getTargetBlockFace(10)!!)

        val pair = selectorLocation(selector.direction)
        val pairLoc = pairLocation(selector.direction)

        selector.pointEntity?.teleport(location.clone().add(resultLocation))
        selector.selectorEntity?.teleport(location.clone().add(resultLocation).add(pair.first, 0.0, pair.second))
        selector.rangeLoc = Pair(pairLoc.first, pairLoc.second)

        selector.selectorEntity?.rotateY(selector.direction)

        entity.teleport(selector.rangeLoc!!.first)
        entity2.teleport(selector.rangeLoc!!.second)
    }

    private fun selectorLocation(direction: Double) : Pair<Double, Double> {
        val rangeX: Double = selector.rangeX / 2.0 + if (isOdd(selector.rangeX)) 0.5 else 0.0 //홀수면 0.5 빼기
        val rangeZ: Double = selector.rangeZ / 2.0 + if (isOdd(selector.rangeZ)) 0.5 else 0.0

        val pair = when (direction) {
            0.0 -> Pair(-rangeX + 1, -rangeZ + 1)
            90.0 -> Pair(-rangeZ + 1, rangeX)
            180.0 -> Pair(rangeX, rangeZ)
            270.0 -> Pair(rangeZ, -rangeX + 1)

            else -> Pair(-rangeX, -rangeZ)
        }


        return pair
    }

    private fun pairLocation(direction: Double) : Pair<Location, Location> {
        val selectorLocation = selector.pointEntity!!.location

        val rangeX: Double = selector.rangeX / 2.0 + if (isOdd(selector.rangeX)) 0.5 else 0.0
        val rangeZ: Double = selector.rangeZ / 2.0 + if (isOdd(selector.rangeZ)) 0.5 else 0.0
        val rangeXHalf = selector.rangeX - rangeX + 0.5
        val rangeZHalf = selector.rangeZ - rangeZ + 0.5

        val pair = when (direction) {
            0.0 -> Pair(selectorLocation.clone().add(-rangeX + 1.5, 0.0, -rangeZ + 1.5),
                selectorLocation.clone().add(rangeXHalf, selector.rangeY.toDouble(), rangeZHalf))

            90.0 -> Pair(selectorLocation.clone().add(-rangeZ + 1.5, 0.0, rangeX - 0.5),
                selectorLocation.clone().add(rangeZHalf , selector.rangeY.toDouble(), -rangeXHalf + 1.0))

            180.0 -> Pair(selectorLocation.clone().add(rangeZ - 0.5, 0.0, rangeX - 0.5),
                selectorLocation.clone().add(-rangeZHalf + 1.0, selector.rangeY.toDouble(), -rangeXHalf + 1.0))

            270.0 -> Pair(selectorLocation.clone().add(rangeZ - 0.5, 0.0, -rangeX + 1.5),
                selectorLocation.clone().add(-rangeZHalf + 1.0, selector.rangeY.toDouble(), rangeXHalf))

            else -> Pair(selectorLocation.clone().add(-rangeXHalf + 1.0, 0.0, rangeZHalf),
                selectorLocation.clone().add(rangeX - 0.5, selector.rangeY.toDouble(), -rangeZ + 1.5))
        }

        return pair
    }
}