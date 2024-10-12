package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.pve.DamageLayer
import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.user.UserData
import com.github.sheauoian.croissantsys.user.ui.MainMenu
import com.github.sheauoian.croissantsys.user.ui.StatusGui
import com.github.sheauoian.croissantsys.user.ui.equipment.ELevelUpUI
import com.github.sheauoian.croissantsys.user.ui.equipment.EStorageUI
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.StatusType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class UserDataOnline
    (val player: Player, override val money: Int, override val health: Double, override val maxHealth: Double):
    UserData(player.uniqueId, money, health, maxHealth),
    DamageLayer
{
    val eManager: EquipmentStorage = EquipmentStorage(this)

    fun openMenu() {
        MainMenu(this).open()
    }

    // Status
    fun openStatusMenu() {
        StatusGui(this).show(player)
    }

    fun openEStorage(bodyPart: BodyPart?) {
        EStorageUI(this, bodyPart).show(player)
    }
    fun openELevelingStorage(bodyPart: BodyPart?) {
        EStorageUI(this, bodyPart, true).show(player)
    }

    fun openELeveling(equipment: Equipment?) {
        val ui = ELevelUpUI(this)
        ui.setEquipment(equipment)
        ui.show(player)
    }

    fun update() {
        updateStatus()
        val jump = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)
        if (jump != null) {
            jump.baseValue = 0.5
        }
        player.sendActionBar(Component.text("$health / $maxHealth | ${money}\$"))
    }


    override fun getInflictDamage(d: Double): Double {
        val damage = d * (baseStatus[StatusType.STR] ?: 1.0)
        player.sendMessage(Component.text(damage).color(TextColor.color(0xccaa88)))
        return damage
    }

    override fun getReceiveDamage(d: Double): Double {
        return d / (baseStatus[StatusType.DEF] ?: 1.0)
    }
}