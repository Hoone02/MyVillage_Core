package org.hoon.myvillage_core.util

import org.bukkit.entity.Player
import java.util.*

private val lastInteractTimes = mutableMapOf<UUID, Long>()

fun canInteract(player: Player): Boolean {
    val currentTime = System.currentTimeMillis()
    val lastInteractTime = lastInteractTimes[player.uniqueId] ?: 0L

    if (currentTime - lastInteractTime < 200) { // 500ms 이내의 클릭은 무시 // 0.2초
        return false
    }

    lastInteractTimes[player.uniqueId] = currentTime
    return true
}