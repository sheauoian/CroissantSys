package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.command.EquipmentCommand
import com.github.sheauoian.croissantsys.command.MenuCmd
import com.github.sheauoian.croissantsys.command.WearingCommand
import com.github.sheauoian.croissantsys.command.argument.EDataArgument
import com.github.sheauoian.croissantsys.listener.PlayerJoinListener
import com.github.sheauoian.croissantsys.pve.DamageListener
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.user.UserDataManager
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
        lateinit var instance: CroissantSys
    }

    private var liteCommands: LiteCommands<CommandSender>? = null

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        this.liteCommands = LiteBukkitFactory.builder("croissant", this)
            .extension(LiteAdventureExtension()) { config ->
                config.miniMessage(true)
            }
            .commands(
                EquipmentCommand(),
                WearingCommand(),
                MenuCmd()
            )
            .argument(
                EquipmentData::class.java, EDataArgument()
            )
            .build()
        // Depends
        InventoryAPI(this).init()

        val manager = Bukkit.getPluginManager()
        manager.registerEvents(PlayerJoinListener(), this)
        manager.registerEvents(DamageListener(), this)
        EDataManager.instance.reload()
        UserRunnable().runTaskTimer(this, 5, 2)
    }

    override fun onDisable() {
        saveConfig()
        UserDataManager.instance.saveAll()
        EDataManager.instance.saveAll()
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