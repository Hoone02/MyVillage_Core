package org.hoon.myvillage_core

import org.bukkit.plugin.java.JavaPlugin
import org.hoon.myvillage_core.command.Command
import org.hoon.myvillage_core.protection.ProtectionManager

class MyVillage_Core : JavaPlugin() {

    companion object {
        lateinit var instance: MyVillage_Core
            private set
    }

    override fun onEnable() {
        instance = this

        getCommand("test")?.setExecutor(Command())
        server.pluginManager.registerEvents(Listener(), this)
        ProtectionManager.load()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
