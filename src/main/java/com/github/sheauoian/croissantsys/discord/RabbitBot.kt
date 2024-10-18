package com.github.sheauoian.croissantsys.discord

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.discord.listener.MessageReceiveListener
import com.github.sheauoian.croissantsys.discord.listener.SlashCommandListener
import mc.obliviate.inventory.Gui
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import javax.security.auth.login.LoginException

class RabbitBot(token: String, guildId: String) {
    private val jda: JDA = try {
        val j = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
            .addEventListeners(MessageReceiveListener())
            .addEventListeners(SlashCommandListener())
            .setActivity(Activity.playing("クロワッサン"))
            .build()
        j.awaitReady()
        j
    } catch (_: LoginException) {
        throw RuntimeException("[discord] jda is null")
    }
    private val guild: Guild = jda.getGuildById(guildId) ?: throw RuntimeException("[discord] guild is null")

    init {
        val testCommand = Commands.slash("status", "ステータスを表示します")
            .addOption(OptionType.STRING, "mcid", "表示させたいプレイヤーのMCID")
        guild.updateCommands()
            .addCommands(testCommand)
            .queue()
    }
}