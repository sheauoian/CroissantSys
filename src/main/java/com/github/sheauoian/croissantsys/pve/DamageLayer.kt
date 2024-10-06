package com.github.sheauoian.croissantsys.pve

interface DamageLayer {
    fun getReceiveDamage(d: Double): Double
    fun getInflictDamage(d: Double): Double
}