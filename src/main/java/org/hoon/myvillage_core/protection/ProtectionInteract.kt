package org.hoon.myvillage_core.protection

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

object ProtectionInteract {

    fun onInteract(event : PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val block = event.clickedBlock ?: return
            if (block.type == Material.BARRIER) {
                for (meta in block.getMetadata("type")) {
                    val value = meta.value()
                    if (value == "workbench") {
                        val uuid = UUID.fromString(block.getMetadata("ID")[0].value().toString())
                        val protection = ProtectionManager.get(uuid)
                        val player = protection!!.owner
                        if (player == event.player.uniqueId.toString()) {
                            event.player.sendMessage("당신의 땅입니다.")
                        } else {
                            val uuid = UUID.fromString(player)
                            val p = Bukkit.getPlayer(uuid) ?: return
                            event.player.sendMessage("${p.name} 님의 땅입니다.")
                        }
                    }
                }
            }
        }
    }
}