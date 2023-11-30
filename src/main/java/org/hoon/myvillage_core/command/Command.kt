package org.hoon.myvillage_core.command

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.hoon.myvillage_core.MyVillage_Core
import org.hoon.myvillage_core.selector.*
import org.hoon.myvillage_core.util.addTag
import org.hoon.myvillage_core.util.setHideEntity

class Command : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        val player = p0 as Player

        if (p3!!.isEmpty()) {
            player.sendMessage("§c명령어를 입력해주세요.")
            return true
        }
        else {
            when (p3[0]) {
                "give" -> {
                    val item = ItemStack(Material.SHULKER_SHELL, 1)
                    val meta = item.itemMeta
                    meta!!.addTag("areaItem", "true")
                    val name = Component.text("§e§l건축 작업대")
                    meta.displayName(name)
                    item.itemMeta = meta
                    player.inventory.addItem(item)
                }
                "remove" -> {
                    SelectorManager.remove(SelectorManager.get(player)!!)
                }
                "check" -> {
                    player.sendMessage(SelectorManager.getList().toString())
                }
                "final" -> {
                    placeSelector(SelectorManager.get(player)!!, player).block.type = Material.DIAMOND_BLOCK
                }
                "test" -> {
                    val selector = SelectorManager.get(player)!!
                    selector.direction = p3[1].toDouble()
                }
            }
        }

        return true
    }
}