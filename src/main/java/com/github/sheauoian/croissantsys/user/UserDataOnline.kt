package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.pve.DamageLayer
import com.github.sheauoian.croissantsys.pve.Equipment
import com.github.sheauoian.croissantsys.user.ui.MainMenu
import com.github.sheauoian.croissantsys.user.ui.StatusGui
import com.github.sheauoian.croissantsys.user.ui.equipment.ELevelUpUI
import com.github.sheauoian.croissantsys.user.ui.equipment.EStorageUI
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.Status
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class UserDataOnline(val player: Player): UserData(player.uniqueId), DamageLayer {
    val eManager: UserEquipmentManager = UserEquipmentManager(this)

    fun levelUpWearing(bodyPart: BodyPart?) {
        val equipment = wearing[bodyPart]
        if (equipment != null) eManager.levelUp(equipment)
    }

    fun addSubStatusToWearing(bodyPart: BodyPart?, status: Status) {
        val equipment = wearing[bodyPart]
        if (equipment != null) eManager.addSubStatus(equipment, status)
    }

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
        val jump = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH)
        if (jump != null) {
            jump.baseValue = 1.0
        }
        player.sendActionBar(Component.text("$health / $maxHealth | ${money}\$"))
    }


    override fun getReceiveDamage(d: Double): Double {
        return d
    }

    override fun getInflictDamage(d: Double): Double {
        return d
    }
}