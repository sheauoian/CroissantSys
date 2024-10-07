package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.pve.Equipment
import com.github.sheauoian.croissantsys.pve.EquipmentData
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.util.BodyPart
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
        val data = UserData.getOnline(sender)
        data.openStatusMenu()
    }

    @Execute(name = "info")
    fun info(@Context sender: CommandSender, @Arg target: Player) {
        val user = UserData.getOnline(target)
        sender.sendMessage(user.getWearingComponent())
    }

    @Execute(name = "set")
    fun info(@Context sender: Player, @Arg bodyPart: BodyPart, @Arg data: EquipmentData) {
        val user = UserData.getOnline(sender)
        user.setWearing(bodyPart, Equipment.generate(data, sender.uniqueId.toString()))
    }
}
