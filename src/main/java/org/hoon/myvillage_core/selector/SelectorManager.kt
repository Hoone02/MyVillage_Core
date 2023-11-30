package org.hoon.myvillage_core.selector

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hoon.myvillage_core.util.updateTransformation

object SelectorManager {
    private val selectors = mutableMapOf<Player, Selector>()

    fun spawn(selector: Selector) { // 선택기 생성
        val player = selector.player
        val entity = player.world.spawn(player.location, ItemDisplay::class.java)
        val range = player.world.spawn(player.location, BlockDisplay::class.java)
        val itemStack = ItemStack(selector.placeable)
        val rangeBlockData = Bukkit.createBlockData(Material.LIME_STAINED_GLASS)

        range.updateTransformation { scale { // 범위 엔티티 크기 조정
            x = selector.rangeX
            y = 0.5f
            z = selector.rangeZ
        } }


        entity.itemStack = itemStack // 선택기 엔티티 블록 설정
        range.block = rangeBlockData // 범위 엔티티 블록 설정

        selector.pointEntity = entity // 선택기 엔티티
        selector.selectorEntity = range // 범위 엔티티


        selectors[player] = selector

    }

    fun remove(selector: Selector) { // 선택기 제거
        val player = selector.player
        selectors[player]?.pointEntity?.remove()
        selectors[player]?.selectorEntity?.remove()
        selectors.remove(player)
    }

    fun removeAll() { // 모든 선택기 제거
        if (selectors.isEmpty()) return
        for (selector in selectors.values) {
            selector.pointEntity?.remove()
        }
        selectors.clear()
    }

    fun get(player: Player): Selector? { // 선택기 가져오기
        return selectors[player]
    }

    fun getList(): List<Selector> { // 선택기 목록 가져오기
        val selectors = mutableListOf<Selector>()
        for (selector in this.selectors.values) {
            selectors.add(selector)
        }
        return selectors
    }

    fun exists(player: Player): Boolean { // 선택기 존재 여부
        return selectors.containsKey(player)
    }
}