package com.github.sheauoian.croissantsys

import com.github.sheauoian.croissantsys.command.EquipmentCommand
import com.github.sheauoian.croissantsys.command.MenuCmd
import com.github.sheauoian.croissantsys.command.StatusCmd
import com.github.sheauoian.croissantsys.command.argument.EDataArgument
import com.github.sheauoian.croissantsys.command.argument.WarpPointArgument
import com.github.sheauoian.croissantsys.command.op.WarpPointSettingCmd
import com.github.sheauoian.croissantsys.discord.RabbitBot
import com.github.sheauoian.croissantsys.discord.listener.MessageReceiveListener
import com.github.sheauoian.croissantsys.discord.listener.SlashCommandListener
import com.github.sheauoian.croissantsys.listener.PlayerJoinListener
import com.github.sheauoian.croissantsys.pve.DamageListener
import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.pve.equipment.weapon.WeaponListener
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.user.UserRunnable
import com.github.sheauoian.croissantsys.world.WarpPoint
import com.github.sheauoian.croissantsys.world.WarpPointManager
import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory
import mc.obliviate.inventory.InventoryAPI
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class CroissantSys: JavaPlugin() {
    companion object {
        lateinit var instance: CroissantSys
    }

    private var liteCommands: LiteCommands<CommandSender>? = null
    private var rabbit: RabbitBot? = null

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        this.liteCommands = LiteBukkitFactory.builder("croissant", this)
            .extension(LiteAdventureExtension()) { config ->
                config.miniMessage(true)
            }
            .commands(
                EquipmentCommand(),
                StatusCmd(),
                MenuCmd(),
                WarpPointSettingCmd()
            )
            .argument(
                EquipmentData::class.java, EDataArgument()
            )
            .argument(
                WarpPoint::class.java, WarpPointArgument()
            )
            .build()
        // Depends
        InventoryAPI(this).init()

        val manager = Bukkit.getPluginManager()
        manager.registerEvents(PlayerJoinListener(), this)
        manager.registerEvents(DamageListener(), this)
        manager.registerEvents(WeaponListener(), this)

        EDataManager.instance.reload()
        UserRunnable().runTaskTimer(this, 5, 2)
        WarpPointManager.instance.reload()

        val token = config.getString("discord_token")
        val guild = config.getString("discord_guild")
        if (token != null && guild != null)
            rabbit = RabbitBot(token, guild)
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