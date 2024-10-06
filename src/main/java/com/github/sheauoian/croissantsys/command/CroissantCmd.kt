package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.CroissantSys.Companion.instance
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class CroissantCmd : TabCompleter, Cmd() {
    override fun onCommand(sender: CommandSender, cmd: Command, text: String, args: Array<String>): Boolean {
        if (args.isNotEmpty()) {
            when (args[0]) {
                "initialspawn" -> { // 初期スポーン地点の設定を行います。
                    if (args.size >= 2) {
                        when (args[1]) {
                            "set" -> {
                                if (sender is Player) {
                                    instance.let {
                                        it.initialSpawnPoint = sender.location
                                        sender.sendMessage("初期スポーン地点を設定しました。")
                                    }
                                }
                            }
                            "tp" -> {
                                if (sender is Player) {
                                    instance.initialSpawnPoint.let { sender.teleport(it) }
                                    sender.sendMessage("初期スポーン地点にテレポートしました。")
                                }
                            }
                        }
                    } else {
                        if (sender is Player) {
                            instance.initialSpawnPoint.let { sender.teleport(it) }
                            sender.sendMessage("初期スポーン地点にテレポートしました。")
                        } else {
                            sender.sendMessage(instance.initialSpawnPoint.toString())
                        }
                    }
                }

                "reload" -> {

                }
            }
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, s: String, args: Array<String>): List<String>? {
        if (args.size == 1) {
            return listOf("initialspawn", "reload")
        } else if (args.size == 2) {
            when (args[1]) {
                "initialspawn" -> {
                    return listOf("set", "tp")
                }
            }
        }
        return null
    }

    override val commandName = "croissant"
}
