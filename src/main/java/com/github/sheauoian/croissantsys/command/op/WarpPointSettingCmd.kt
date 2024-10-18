package com.github.sheauoian.croissantsys.command.op

import com.github.sheauoian.croissantsys.world.WarpPointManager
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import org.bukkit.command.CommandSender

@Command(name = "warp_setting")
class WarpPointSettingCmd {
    @Execute
    fun help(@Context sender: CommandSender) {
        sender.sendMessage("/warp_setting list: ワープポイントのリストを表示します")
    }

    @Execute(name = "list")
    fun list(@Context sender: CommandSender) {
        sender.sendMessage("WarpPoint List:")
        WarpPointManager.instance.getAll().forEach {
            sender.sendMessage(" - ${it.id}")
        }
    }
}