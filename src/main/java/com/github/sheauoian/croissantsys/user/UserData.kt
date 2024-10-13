package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.pve.equipment.Equipment
import com.github.sheauoian.croissantsys.util.BodyPart
import com.github.sheauoian.croissantsys.util.status.StatusType
import java.util.*

open class UserData(
    val uuid: UUID,
    open val money: Int,
    open val health: Double,
    open val maxHealth: Double
): DbDriver() {
    val baseStatus: MutableMap<StatusType, Double> = EnumMap(StatusType::class.java)
    val wearing: Wearing = Wearing(uuid.toString())

    init {
        updateStatus()
    }
    open fun save() {
        UserDataManager.instance.save(this)
        wearing.saveWearing()
    }

    fun updateStatus() {
        updateBaseStatus()
    }

    private fun updateBaseStatus() {
        StatusType.entries.forEach {
            baseStatus[it] = it.baseVolume
        }
        wearing.get().forEach { wearing ->
            if (wearing != null) {
                for ((type, volume) in wearing.getStatus()) {
                    baseStatus[type] = baseStatus[type]!! + volume
                }
            }
        }
    }
}