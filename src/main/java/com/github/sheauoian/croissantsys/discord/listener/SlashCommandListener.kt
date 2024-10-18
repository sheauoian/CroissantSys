package com.github.sheauoian.croissantsys.discord.listener

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.discord.embed.UserDataEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class SlashCommandListener : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "status" -> {
                val id = event.getOption("mcid", OptionMapping::getAsString)
                if (id != null) {
                    val player = CroissantSys.instance.server.getOfflinePlayer(id)
                    event.replyEmbeds(UserDataEmbed(player).build()).setEphemeral(true).queue()
                }
                else {
                    event.reply("""
                        mcidを入力せぬなどはなはだおそろしき;;
                    """.trimIndent()).queue()
                }
            }
            else -> {
                event.reply("""
                        え
                    """.trimIndent()).queue()
            }
        }
    }
}