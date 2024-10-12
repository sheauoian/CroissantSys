package com.github.sheauoian.croissantsys.util

import com.github.sheauoian.croissantsys.DbDriver

abstract class Manager<K, V>: DbDriver() {
    abstract fun load(k: K): V?
    abstract fun save(v: V)
}