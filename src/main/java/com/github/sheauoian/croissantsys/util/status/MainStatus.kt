package com.github.sheauoian.croissantsys.util.status

class MainStatus(override val volume: Double, val slope: Double, override val type: StatusType): Status(volume, type) {
    fun getVolumeFromLevel(level: Int): Double {
        return volume + slope * level
    }
}