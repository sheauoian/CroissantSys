package com.github.sheauoian.croissantsys.discord.embed

import com.github.sheauoian.croissantsys.user.UserDataManager
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.OfflinePlayer
import java.awt.Color

class UserDataEmbed(val player: OfflinePlayer?): EmbedBuilder() {
    init {
        if (player != null) {
            setTitle("${player.playerProfile.name} のステータス", null)
            val user = UserDataManager.instance.get(player.uniqueId)
            if (user != null) {
                setColor(Color(255, 236, 133))
                setDescription(user.uuid.toString())
                addField("Title of field", "test of field", false)
                addBlankField(false)
                setAuthor("")
            } else {
                setColor(Color(0xAF1111))
                setDescription("存在しなかった:sob:")
            }
        }
    }
}