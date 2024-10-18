package com.github.sheauoian.croissantsys.util.status

enum class StatusType(val displayName: String, val baseVolume: Double) {
    STR("攻撃力", 1.0),
    DEF("防御力", 1.0),
    MAX_HP("体力", 2.0),
    MAGIC_EFFECT("スキル性能", 1.0),
    CRITICAL_RATE("クリティカル確率", 0.0);
    companion object {
        fun valueOfOrNull(value: String?): StatusType? {
            return try {
                valueOf(value!!)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        fun getRandom(exclude: List<StatusType>): StatusType {
            return entries.filterNot { it in exclude } .random()
        }
    }
}