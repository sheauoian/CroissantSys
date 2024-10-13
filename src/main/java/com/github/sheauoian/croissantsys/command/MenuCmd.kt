package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.RootCommand
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@RootCommand
class MenuCmd {
    @Execute(name = "menu")
    fun menu(@Context sender: Player) {
        UserDataManager.instance.getOnline(sender)?.openMenu()
    }

    @Execute(name = "equipment_storage", aliases = ["es"])
    fun eStorage(@Context sender: Player, @OptionalArg bodyPart: BodyPart?) {
        UserDataManager.instance.getOnline(sender)?.openEStorage(bodyPart)
    }

    @Execute(name = "spawn")
    fun spawn(@Context sender: Player) {
        sender.teleport(CroissantSys.instance.initialSpawnPoint)
    }

    @Execute(name = "userinfo")
    fun userInfo(@Context sender: CommandSender, @Arg mcid: String) {
        val user = UserDataManager.instance.get(CroissantSys.instance.server.getOfflinePlayer(mcid).uniqueId)
        if (user == null) {
            sender.sendMessage("そのアカウントは存在しません")
        }
        else {
            sender.sendMessage(user.wearing.getWearingComponent())
        }
    }

    @Execute(name = "cm")
    fun cmStorage(@Context sender: Player) {
        val user = UserDataManager.instance.getOnline(sender)
        if (user == null) {
            sender.sendMessage("そのアカウントは存在しません")
        }
        else {
            user.openCMaterialStorage()
        }
    }
}