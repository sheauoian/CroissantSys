package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.pve.Equipment
import com.github.sheauoian.croissantsys.pve.EquipmentData
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.util.BodyPart
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class EquipmentCmd: Cmd(), TabCompleter {
    override val commandName: String
        get() = "equipment"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val user = UserData.getOnline(sender)
        if (args.isEmpty()) {
            sender.level += 10
            sender.sendMessage(user.getWearingComponent())
            return false
        }
        when (args[0]) {
            "set" -> {
                if (args.size >= 3) {
                    val body: BodyPart?
                    try {
                        body = BodyPart.valueOf(args[1])
                    } catch (_: IllegalArgumentException) {
                        sender.sendMessage("bodyPart is wrong")
                        return false
                    }
                    val data = EquipmentData.get(args[2])
                    if (data != null) {
                        val equipment = Equipment.generate(data, user.uuid.toString())
                        user.setWearing(body, equipment)
                        sender.sendMessage("${body.name} -> ${equipment.uniqueId}")
                    } else {
                        sender.sendMessage("data id is wrong")
                    }
                }
            }
            "clear" -> {
                user.clearWearing()
            }
        }

        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        if (args.isNotEmpty()) {
            when(args[0]) {
                "set" -> {
                    if (args.size >= 2) {
                        return enumValues<BodyPart>().map {it.name}
                    }
                    return EquipmentData.getIds()
                }
            }
            return enumValues<BodyPart>().map {it.name}
        }
        return listOf("set", "clear")
    }
}