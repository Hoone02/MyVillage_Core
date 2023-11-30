package org.hoon.myvillage_core

import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.hoon.myvillage_core.protection.ProtectionInteract
import org.hoon.myvillage_core.protection.ProtectionManager
import org.hoon.myvillage_core.protection.ProtectionPlace
import org.hoon.myvillage_core.util.canInteract
import java.util.UUID

class Listener : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (!canInteract(event.player)) return
        ProtectionInteract.onInteract(event)
        if (event.item == null) return
        ProtectionPlace.placeWorkstation(event.player, event)
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block

        if (block.type == Material.BARRIER) {
            for (meta in block.getMetadata("type")) {
                val value = meta.value()
                if (value == "workbench") {
                    event.player.sendMessage(block.getMetadata("ID")[0].value().toString())
                    val entity = block.location.getNearbyEntities(1.0, 1.0, 1.0)
                    for (e in entity) {
                        if (e is BlockDisplay) {
                            val uuid = UUID.fromString(block.getMetadata("ID")[0].value().toString())
                            val protection = ProtectionManager.get(uuid)
                            ProtectionManager.remove(protection!!)
                            e.remove()
                        }
                    }
                }
            }
        }
    }
}