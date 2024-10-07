package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.pve.EquipmentData
import com.github.sheauoian.croissantsys.user.UserData
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(name = "equipment")
class EquipmentCommand {

    @Execute
    fun executeEquipment() {
        // Execute the /equipment
    }

    @Execute(name = "info")
    fun info(@Context sender: Player) {
        val user = UserData.getOnline(sender)
        sender.sendMessage(user.getWearingComponent())
    }

    @Execute(name = "create")
    fun create(@Context sender: CommandSender, @Arg(value = "データID") dataId: String) {
        if (EquipmentData.addInitialData(dataId) != null) {
            sender.sendMessage("追加に成功しました")
        }
        else {
            sender.sendMessage("追加に失敗しました")
        }
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender) {
        sender.sendMessage("Equipment ID 一覧:")
        EquipmentData.getAll().forEach {
            sender.sendMessage(" - ${it.id} : ${it.name}")
        }
    }

    @Execute(name = "reload")
    fun reload(@Context sender: CommandSender) {
        EquipmentData.reload()
        sender.sendMessage("Equipment Data のリロードが完了しました。")
    }
}