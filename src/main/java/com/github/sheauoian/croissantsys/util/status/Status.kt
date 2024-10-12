package com.github.sheauoian.croissantsys.util.status

import kotlinx.serialization.Serializable
import java.util.*


@Serializable
open class Status(open val volume: Double, open val type: StatusType) {
    companion object {
        fun generate(exclude: List<Status>): Status {
            val type = StatusType.getRandom(exclude.map {it.type})
            return Status(
                Random().nextDouble(0.3, 0.8) * type.baseVolume, type
            )
        }
    }
}