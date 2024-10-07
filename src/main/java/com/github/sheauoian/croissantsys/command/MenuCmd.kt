package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.user.UserData
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MenuCmd: Cmd() {
    override val commandName: String
        get() = "menu"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            UserData.getOnline(sender).openMenu()
        } else {
            sender.sendMessage("このコマンドは Player のみ実行可能です。")
        }
        return false
    }
}