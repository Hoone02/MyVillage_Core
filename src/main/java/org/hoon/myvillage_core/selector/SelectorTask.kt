package org.hoon.myvillage_core.selector

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.hoon.myvillage_core.MyVillage_Core
import org.hoon.myvillage_core.util.BlockUtil

object SelectorTask {
    var selectorTask = mutableMapOf<Player, BukkitTask>()

    fun run(selector: Selector, entity: Entity, entity2: Entity) {
        val player = selector.player
        selectorTask[player] = Bukkit.getScheduler().runTaskTimer(MyVillage_Core.instance, Runnable {
            SelectorHandler(selector).updateSelectorState(entity2, entity) // 선택기 상태 업데이트
        }, 0, 1)
    }
}