package com.github.sheauoian.croissantsys.command

import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.UserDataOnline
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UserDataCmd: Cmd() {
    override val commandName: String
        get() = "user"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            val online = UserData.datum.values.count {it is UserDataOnline}
            val offline = UserData.datum.size - online
            sender.sendMessage("ロードされたUserData:\n Online: ${online}\n Offline: $offline")
            return false
        }
        else if (args.size == 1) {
            return false
        }
        if (sender is Player) {
            val user: UserData = UserData.getOnline(sender)
            when (args[0]) {
                "status" -> {
                    var amount: Double? = null

                    if (args.size >= 3) try {
                        amount = args[2].toDouble()
                    } catch (e: NumberFormatException) {
                        sender.sendMessage("数値のフォーマットが異なります")
                        return false
                    }

                    when (args[1]) {
                        "money" -> {
                            amount?.let {
                                user.money = amount.toInt()
                                sender.sendMessage("- MONEY <- ${user.money}")
                            }.also {
                                sender.sendMessage("- MONEY =  ${user.money}")
                            }
                        }
                        "health" -> {
                            amount?.let {
                                user.health = amount
                                sender.sendMessage("- HEALTH <- ${user.health}")
                            }.also {
                                sender.sendMessage("- HEALTH =  ${user.health}")
                            }
                        }
                        "max_health" -> {
                            amount?.let {
                                user.maxHealth = amount
                                sender.sendMessage("- MAX_HEALTH <- ${user.maxHealth}")
                            }.also {
                                sender.sendMessage("- MAX_HEALTH =  ${user.maxHealth}")
                            }
                        }

                        else -> {
                            sender.sendMessage("money, health, max_health")
                        }
                    }
                }
            }
        }
        return false
    }
}