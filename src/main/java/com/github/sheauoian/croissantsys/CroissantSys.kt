package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.command.*
import com.github.sheauoian.croissantsys.command.argument.EDataArgument
import com.github.sheauoian.croissantsys.listener.PlayerJoinListener
import com.github.sheauoian.croissantsys.pve.EquipmentData
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.UserRunnable
import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import mc.obliviate.inventory.InventoryAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin


class CroissantSys: JavaPlugin() {
    companion object {
        // Singleton
        lateinit var instance: CroissantSys

        fun load() {
            instance.let {
                Cmd.load(it)

                val manager = Bukkit.getPluginManager()
                manager.registerEvents(PlayerJoinListener(), it)

                EquipmentData.reload()

                UserRunnable().runTaskTimer(it, 5, 2)
            }
        }
    }

    var liteCommands: LiteCommands<CommandSender>? = null

    override fun onEnable() {
        instance = this

        this.liteCommands = LiteBukkitFactory.builder("croi", this)
            .extension(LiteAdventureExtension()) { config ->
                config.miniMessage(true)
            }
            .commands(
                EquipmentCommand(),
                WearingCommand()
            )
            .argument(
                EquipmentData::class.java, EDataArgument()
            )
            .build()
        // Depends
        InventoryAPI(this).init()

        saveDefaultConfig()

        load()
    }

    override fun onDisable() {
        saveConfig()
        UserData.save()
        EquipmentData.save()

        DbDriver.instance.close()
    }

    var initialSpawnPoint: Location
        get() {
            return config.getLocation("initial_spawn_point", server.getWorld("world")?.spawnLocation)!!
        }
        set(location) {
            config.set("initial_spawn_point", location)
            saveConfig()
        }
}