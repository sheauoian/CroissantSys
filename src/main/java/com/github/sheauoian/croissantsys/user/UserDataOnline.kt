package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.user.ui.MainMenu
import com.github.sheauoian.croissantsys.pve.DamageLayer
import com.github.sheauoian.croissantsys.user.ui.WarpUI
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class UserDataOnline(val player: Player): UserData(player.uniqueId), DamageLayer {
    private val menu: MainMenu = MainMenu(this)
    val warp: WarpUI = WarpUI(this)

    fun openMenu() {
        menu.open()
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