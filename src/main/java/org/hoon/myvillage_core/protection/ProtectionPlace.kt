package org.hoon.myvillage_core.protection

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.hoon.myvillage_core.MyVillage_Core
import org.hoon.myvillage_core.selector.AbstractSelector
import org.hoon.myvillage_core.selector.Selector
import org.hoon.myvillage_core.selector.SelectorManager
import org.hoon.myvillage_core.selector.placeSelector
import org.hoon.myvillage_core.util.getTag
import org.hoon.myvillage_core.util.rotateY
import org.hoon.myvillage_core.util.sound
import org.hoon.myvillage_core.util.title.TitleUtil
import java.util.*


class ProtectionPlace (stack1: ItemStack, stack2 : ItemStack): AbstractSelector(stack1, stack2) {

    fun placeWorkstation(player: Player, event: PlayerInteractEvent) {
        if (!isValidAction(player, event)) return

        execute(player, event, place = {
            setBlockPlace(player, SelectorManager.get(player)!!)
        }, rangeX = 11.0f, rangeY = 10.0f, rangeZ = 11.0f)
    }

    private fun isValidAction(player: Player, event: PlayerInteractEvent): Boolean {
        val item = player.inventory.itemInMainHand

        return !(item.type.isEmpty ||
                item.itemMeta.localizedName.isEmpty() ||
                item.itemMeta!!.getTag("areaItem") != "true" ||
                event.hand == EquipmentSlot.OFF_HAND)
    }


    private fun setBlockPlace(player: Player, selector: Selector) {
        val pair = selector.rangeLoc ?: return
        val location = placeSelector(selector, player).block.location.add(0.5, 0.5, 0.5)

        val block = player.world.spawn(location, ItemDisplay::class.java)
        val blockData = ItemStack(Material.SHULKER_SHELL)
        val meta = blockData.itemMeta
        meta.setCustomModelData(1)
        blockData.itemMeta = meta
        TitleUtil.stop(player)

        block.rotateY(selector.direction)

        val randomUUID = UUID.randomUUID()

        val protection = Protection(randomUUID, player.uniqueId.toString(), pair.first, pair.second, location)

        ProtectionManager.create(protection)

        setBarrier(location, player, randomUUID)
        block.itemStack = blockData

        player.sound(Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 1.0f)
        player.sound(Sound.BLOCK_WOOD_PLACE, 1.0f, 1.0f)

        ProtectionRangeViewer.remove(player)

    }

    private fun setBarrier(location: Location, player: Player, uuid: UUID) {
        val block = location.block
        location.block.type = Material.BARRIER
        block.setMetadata("ID", FixedMetadataValue(MyVillage_Core.instance, uuid.toString()))
        block.setMetadata("type", FixedMetadataValue(MyVillage_Core.instance, "workbench"))
        block.setMetadata("owner", FixedMetadataValue(MyVillage_Core.instance, player.uniqueId.toString()))
    }

}