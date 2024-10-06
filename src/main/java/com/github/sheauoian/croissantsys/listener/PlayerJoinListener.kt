package com.github.sheauoian.croissantsys.listener

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.user.UserData
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.server.ServerListPingEvent

class PlayerJoinListener : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        UserData.addOnline(e.player)
    }
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        UserData.getOnline(e.player).save()
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        e.respawnLocation = CroissantSys.instance.initialSpawnPoint
    }


    @EventHandler
    fun onServerPing(e: ServerListPingEvent) {
        e.motd(
            MiniMessage.miniMessage()
                .deserialize("<gradient:#7755BA:#AA5591><bold>CROISSANT WORLD</bold></gradient> ❚ <color:#AAAAAA>Original RPG Server</color>")
        )
    }
}
