package com.github.sheauoian.croissantsys.util.status

enum class StatusType(val displayName: String) {
    STR("攻撃力"),
    DEF("防御力");
    companion object {
        fun valueOfOrNull(value: String?): StatusType? {
            return try {
                valueOf(value!!)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}