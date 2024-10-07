package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.CroissantSys
import org.bukkit.block.spawner.SpawnerEntry.Equipment
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter

abstract class Cmd : CommandExecutor {
    companion object {
        fun load(p: CroissantSys) {
            val commands = listOf(
                CroissantCmd(),
                UserDataCmd(),
                //EquipmentCmd(),
                MenuCmd()
            )
            commands.forEach {
                it.register(p)
            }
            CroissantSys.instance.logger.info("${commands.size} Commands loaded!")
        }
    }

    fun register(plugin: CroissantSys) {
        val c: PluginCommand? = plugin.getCommand(commandName)
        if (c != null) {
            c.setExecutor(this)
            if (this is TabCompleter)
                c.tabCompleter = this
        }
    }

    abstract val commandName: String
}