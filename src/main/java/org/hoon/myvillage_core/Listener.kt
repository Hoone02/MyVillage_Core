package org.hoon.myvillage_core

import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.ItemDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.hoon.myvillage_core.protection.ProtectionInteract
import org.hoon.myvillage_core.protection.ProtectionManager
import org.hoon.myvillage_core.protection.ProtectionPlace
import org.hoon.myvillage_core.selector.SelectorManager
import org.hoon.myvillage_core.selector.changeDirection
import org.hoon.myvillage_core.util.canInteract
import org.hoon.myvillage_core.util.getSlotChangeDirection
import java.util.UUID

class Listener : Listener {

    @EventHandler
    fun onChangeSlot(event: PlayerItemHeldEvent) {
        val player = event.player
        val newSlot = event.newSlot
        val oldSlot = event.previousSlot
        val direction = getSlotChangeDirection(newSlot, oldSlot)

        val test = SelectorManager.exists(player)

        if (test) {
            val selector = SelectorManager.get(player)!!
            selector.direction = changeDirection(selector, direction)

            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (!canInteract(event.player)) return
        ProtectionInteract.onInteract(event)
        if (event.item == null) return

        onProtectionPlace(event)
    }

    private fun onProtectionPlace(event : PlayerInteractEvent) {
        val stack1 = ItemStack(Material.SHULKER_SHELL)
        val meta = stack1.itemMeta
        meta.setCustomModelData(2)
        stack1.itemMeta = meta

        val stack2 = ItemStack(Material.SHULKER_SHELL)
        val meta2 = stack2.itemMeta
        meta2.setCustomModelData(3)
        stack2.itemMeta = meta2
        ProtectionPlace(stack1, stack2).placeWorkstation(event.player, event)
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
                        if (e is ItemDisplay) {
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