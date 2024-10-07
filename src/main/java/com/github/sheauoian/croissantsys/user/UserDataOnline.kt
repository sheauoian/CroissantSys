package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.user.ui.MainMenu
import com.github.sheauoian.croissantsys.pve.DamageLayer
import com.github.sheauoian.croissantsys.user.ui.StatusGui
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class UserDataOnline(val player: Player): UserData(player.uniqueId), DamageLayer {
    fun openMenu() {
        MainMenu(this).open()
    }

    // Status
    fun openStatusMenu() {
        StatusGui(this).show(player)
    }

    fun update() {
        val jump = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)
        if (jump != null) {
            jump.baseValue = 1.0
        }
        player.sendActionBar(Component.text("${health} / ${maxHealth} | ${money}\$"))
    }


    override fun getReceiveDamage(d: Double): Double {
        return d
    }

    override fun getInflictDamage(d: Double): Double {
        return d
    }
}