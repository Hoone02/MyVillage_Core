package org.hoon.myvillage_core.selector

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.hoon.myvillage_core.protection.ProtectionManager
import org.hoon.myvillage_core.protection.ProtectionPlace
import org.hoon.myvillage_core.util.setHideEntity
import org.hoon.myvillage_core.util.sound
import org.hoon.myvillage_core.util.title.TitleUtil
import org.hoon.myvillage_core.util.updateTransformation

abstract class AbstractSelector {
    private val unicodeChar = (0x0201).toChar()
    private val left = unicodeChar.toString()
    private val unicodeChar2 = (0x0202).toChar()
    private val right = unicodeChar2.toString()

    private val t = ChatColor.of("#4e5c24")

    fun execute(player : Player , event: PlayerInteractEvent, place : () -> Unit, rangeX: Float, rangeY: Float = 0.5f, rangeZ: Float) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            handleRightClickAction(player, place, rangeX, rangeY, rangeZ)
        } else {
            cancelActions(player)
            TitleUtil.stop(player)
        }
    }

    private fun handleRightClickAction(player: Player, place : () -> Unit, rangeX: Float, rangeY: Float, rangeZ: Float) {
        if (SelectorManager.exists(player)) {
            manageExistingSelector(player, place)
        } else {
            createAndSpawnNewSelector(player, rangeX, rangeY, rangeZ)
            TitleUtil.setTitle(player, "")
            player.sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
            player.sendMessage("§e[MyVillage] §f설치 구역을 선택해주세요.")
            TitleUtil.setSubTitle(player, "${t}[취소]${left}                    ${right} [확인]")
            TitleUtil.run(player)
        }
    }

    private fun manageExistingSelector(player: Player, place : () -> Unit) {
        val selector = SelectorManager.get(player)!!
        if (!selector.isCheck) {
            player.sendMessage("§c[MyVillage] §f설치할 수 없는 구역입니다.")
            return
        }

        if (selector.placeStage == 0) {
            tempPlaceSelector(selector, player)
            player.sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
            player.sendMessage("§e[MyVillage] §f설치 구역을 다시 확인해주세요.")
            TitleUtil.setSubTitle(player, "${t}[취소]${left}                    ${right} [확인]")
            selector.placeStage = 1
        } else {
            player.sendMessage("§e[MyVillage] §f구역을 설치했습니다.")
            place()
        }
    }

    fun cancelActions(player: Player) {
        if (SelectorManager.exists(player)) {
            SelectorManager.remove(SelectorManager.get(player)!!)
            player.sound(Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f)
            SelectorTask.selectorTask[player]!!.cancel()
        }
    }

    private fun createAndSpawnNewSelector(player: Player, rangeX: Float, rangeY: Float, rangeZ: Float) {
        val entity = player.world.spawn(player.location, ArmorStand::class.java)
        val entity2 = player.world.spawn(player.location, ArmorStand::class.java)
        val selector = Selector(player, rangeX, rangeY, rangeZ, Material.WHITE_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_GLAZED_TERRACOTTA)
        SelectorManager.spawn(selector)
        SelectorTask.run(selector, entity2, entity)
    }
}