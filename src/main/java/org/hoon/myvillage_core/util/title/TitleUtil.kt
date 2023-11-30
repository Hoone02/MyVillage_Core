package org.hoon.myvillage_core.util.title

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.hoon.myvillage_core.MyVillage_Core

object TitleUtil {
    var task = mutableMapOf<Player, BukkitTask>()
    var text = mutableMapOf<Player, String>()
    var subText = mutableMapOf<Player, String>()

    fun run(player: Player) {
        task[player] = Bukkit.getScheduler().runTaskTimer(MyVillage_Core.instance, Runnable {
            player.sendTitle(text[player], subText[player] , 0, 2, 0)
        }, 0, 1)
    }

    fun setTitle(player: Player, text: String) {
        this.text[player] = text
    }

    fun setSubTitle(player: Player, text: String) {
        this.subText[player] = text
    }

    fun stop(player: Player) {
        task[player]?.cancel()
        task.remove(player)
    }
}