package org.hoon.myvillage_core.protection

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.hoon.myvillage_core.MyVillage_Core
import java.io.File
import java.util.*

object ProtectionManager {
    private val file : File = File(MyVillage_Core.instance.dataFolder, "/protection")
    private val list : MutableList<Protection> = mutableListOf()

    fun create(protection: Protection) {
        val file = File(file, "${protection.uuid}.yml")
        val config = YamlConfiguration.loadConfiguration(file)

        config["owner"] = protection.owner
        config["firstLocation"] = protection.firstPoint
        config["secondLocation"] = protection.secondPoint
        config["cantorLocation"] = protection.cantorPoint

        list.add(protection)
        config.save(file)
    }

    fun load() {
        val files = file.listFiles() ?: return

        for (file in files) {
            val config = YamlConfiguration.loadConfiguration(file)

            val player = config.get("owner") as String
            val firstLocation = config.get("firstLocation") as Location
            val secondLocation = config.get("secondLocation") as Location
            val cantorLocation = config.get("cantorLocation") as Location
            val title = file.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val uuid = UUID.fromString(title)
            val p = Protection(uuid, player, firstLocation, secondLocation, cantorLocation)
            list.add(p)
        }
    }

    fun remove(protection: Protection) {
        val file = File(file, "${protection.uuid}.yml")
        file.delete()
        list.remove(protection)
    }

    fun get(uuid: UUID): Protection? {
        for (protection in list) {
            if (protection.uuid == uuid) {
                return protection
            }
        }
        return null
    }

    fun getList(): MutableList<Protection> {
        return list
    }
}