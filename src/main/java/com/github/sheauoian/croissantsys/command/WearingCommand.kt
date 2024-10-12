package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.pve.equipment.EquipmentManager
import com.github.sheauoian.croissantsys.pve.equipment.data.EquipmentData
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.Status
import com.github.sheauoian.croissantsys.util.status.StatusType
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(name = "wearing")
class WearingCommand {

    @Execute
    fun executeWearing(@Arg sender: Player) {
        UserDataManager.instance.getOnline(sender)?.openStatusMenu()
    }

    @Execute(name = "info")
    fun info(@Context sender: CommandSender, @Arg target: Player) {
        val user = UserDataManager.instance.getOnline(target)
        if (user != null) sender.sendMessage(user.wearing.getWearingComponent())
    }

    @Execute(name = "set")
    fun info(@Context sender: Player, @Arg bodyPart: BodyPart, @Arg data: EquipmentData) {
        val user = UserDataManager.instance.getOnline(sender)
        user?.wearing?.setWearing(bodyPart, EquipmentManager.instance.generate(data, sender.uniqueId.toString()))
    }

    @Execute(name = "levelup")
    fun levelup(@Context sender: Player, @Arg bodyPart: BodyPart) {
        val user = UserDataManager.instance.getOnline(sender)
        if (user != null) {
            user.wearing.get(bodyPart)?.levelUp()
        }
    }

    @Execute(name = "sub_status")
    fun subStatus(@Context sender: Player, @Arg bodyPart: BodyPart, @Arg volume: Double, @Arg type: StatusType) {
        val user = UserDataManager.instance.getOnline(sender)
        if (user != null) {
            user.wearing.get(bodyPart)?.addSubStatus(Status(volume, type))
        }
    }
}
