package com.github.sheauoian.croissantsys.pve

class EquipmentData(val id: String, val name: String) {
    companion object {
        private var datum: MutableMap<String, EquipmentData> = mutableMapOf()
        fun reload() {
            datum.clear()
            //TODO YAMLからロード

            datum.put("test", EquipmentData("test", "テスト用装備"))
        }

        fun get(id: String): EquipmentData? {
            return datum.get(id)
        }

        fun getIds(): List<String> {
            return datum.keys.toList()
        }
    }
}