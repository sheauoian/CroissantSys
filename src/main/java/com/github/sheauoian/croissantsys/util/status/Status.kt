package com.github.sheauoian.croissantsys.util.status

import kotlinx.serialization.Serializable


@Serializable
open class Status(open val volume: Double, open val type: StatusType)