package org.hoon.myvillage_core.util

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import kotlin.math.max
import kotlin.math.min

object BlockUtil {

    fun getBlocksInArea(loc1: Location, loc2: Location): List<Block> {
        val world = loc1.world
        val minX = min(loc1.blockX, loc2.blockX)
        val minY = min(loc1.blockY, loc2.blockY)
        val minZ = min(loc1.blockZ, loc2.blockZ)
        val maxX = max(loc1.blockX, loc2.blockX)
        val maxY = max(loc1.blockY, loc2.blockY)
        val maxZ = max(loc1.blockZ, loc2.blockZ)

        return (minX..maxX).flatMap { x ->
            (minY..maxY).flatMap { y ->
                (minZ..maxZ).map { z ->
                    world.getBlockAt(x, y, z)
                }
            }
        }
    }

    fun checkOverlap(
        loc1a: Location,
        loc1b: Location,
        loc2a: Location,
        loc2b: Location
    ): Boolean {
        // 첫 번째 위치 범위의 최소 및 최대 좌표 계산
        val minX1 = min(loc1a.blockX, loc1b.blockX)
        val minY1 = min(loc1a.blockY, loc1b.blockY)
        val minZ1 = min(loc1a.blockZ, loc1b.blockZ)
        val maxX1 = max(loc1a.blockX, loc1b.blockX)
        val maxY1 = max(loc1a.blockY, loc1b.blockY)
        val maxZ1 = max(loc1a.blockZ, loc1b.blockZ)

        // 두 번째 위치 범위의 최소 및 최대 좌표 계산
        val minX2 = min(loc2a.blockX, loc2b.blockX)
        val minY2 = min(loc2a.blockY, loc2b.blockY)
        val minZ2 = min(loc2a.blockZ, loc2b.blockZ)
        val maxX2 = max(loc2a.blockX, loc2b.blockX)
        val maxY2 = max(loc2a.blockY, loc2b.blockY)
        val maxZ2 = max(loc2a.blockZ, loc2b.blockZ)

        // 모든 축에서 겹침이 있는지 확인
        return maxX1 >= minX2 && minX1 <= maxX2 && maxY1 >= minY2 && minY1 <= maxY2 && maxZ1 >= minZ2 && minZ1 <= maxZ2
    }
}