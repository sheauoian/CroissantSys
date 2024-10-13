package com.github.sheauoian.croissantsys.user.online.ui

import com.github.sheauoian.croissantsys.user.online.CMaterialStorage
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import net.kyori.adventure.text.minimessage.MiniMessage

private val title = ComponentHolder.of(MiniMessage.miniMessage().deserialize("""
    マテリアルストレージ
""".trimIndent()))

class CMaterialStorageGui(storage: CMaterialStorage): ChestGui(6, title) {
    init {
        addPane(storage.getPane(1, 1, 7, 2))
        update()
    }
}