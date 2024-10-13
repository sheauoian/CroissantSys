package com.github.sheauoian.croissantsys.user.online

import com.github.sheauoian.croissantsys.util.CMaterial
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import com.github.stefvanschie.inventoryframework.pane.Pane

class CMaterialStorage(val uuid: String) {
    private lateinit var datum: LongArray

    init {
        reload()
    }

    private fun reload() {
        datum = LongArray(CMaterial.entries.size) {0L}
        CMaterialStorageManager.instance.reload(this)
    }


    fun set(id: Int, quantity: Long) {
        datum[id] = quantity
    }

    fun save() {
        for ((m, q) in datum.withIndex())
            CMaterialStorageManager.instance.save(uuid, m, q)
    }

    fun add(material: CMaterial, quantity: Long) {
        datum[material.ordinal] += quantity
    }

    fun getPane(x: Int, y: Int, l: Int, h: Int): PaginatedPane {
        val paginatedPane = PaginatedPane(x, y, l, h)
        var p = 0
        var i = 0
        var pane = OutlinePane(0, 0, l, h)
        for ((m, q) in datum.withIndex()) {
            if (i >= l * h) {
                paginatedPane.addPane(p, pane)
                p += 1
                i = 0
                pane = OutlinePane(0, 0, l, h)
            }
            pane.addItem(GuiItem(CMaterial.entries[m].getGuiItem(q)) {
                it.whoClicked.sendMessage("hello $m")
            })
        }
        paginatedPane.addPane(p, pane)
        return paginatedPane
    }

}