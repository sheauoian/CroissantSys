package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.pve.equipment.data.EDataManager
import com.github.sheauoian.croissantsys.user.UserDataManager
import com.github.sheauoian.croissantsys.util.BodyPart
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import dev.rollczi.litecommands.annotations.permission.Permission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(name = "equipment")
@Permission("item")
class EquipmentCommand {

    @Execute
    fun executeEquipment() {
        // Execute the /equipment
    }

    @Execute(name = "info")
    fun info(@Context sender: Player) {
        val user = UserDataManager.instance.getOnline(sender)
        if (user != null) sender.sendMessage(user.wearing.getWearingComponent())
    }

    @Execute(name = "create")
    fun create(@Context sender: CommandSender, @Arg(value = "データID") dataId: String, @OptionalArg bodyPart: BodyPart?) {
        if (EDataManager.instance.addInitialData(dataId, bodyPart) != null) {
            sender.sendMessage("追加に成功しました")
        }
        else {
            sender.sendMessage("追加に失敗しました(データIDの重複があります)")
        }
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender) {
        sender.sendMessage("Equipment ID 一覧:")
        EDataManager.instance.getAll().forEach {
            sender.sendMessage(" - ${it.id} : ${it.name}")
        }
    }

    @Execute(name = "reload")
    fun reload(@Context sender: CommandSender) {
        EDataManager.instance.reload()
        sender.sendMessage("Equipment Data のリロードが完了しました。")
    }
}