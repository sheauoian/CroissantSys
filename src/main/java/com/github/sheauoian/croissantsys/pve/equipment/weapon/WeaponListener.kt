package com.github.sheauoian.croissantsys.pve.equipment.weapon

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector
import java.lang.Math
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class WeaponListener: Listener {
    private fun shoot(p: Player) {
        p.launchProjectile(Arrow::class.java, getFacingVector(p).multiply(2))
    }
    private fun getFacingVector(p: Player): Vector {
        return getVectorFromYawPitch(p.yaw + 180, p.pitch)
    }
    private fun getVectorFromYawPitch(yaw: Float, pitch: Float): Vector {
        val v = Vector(sin(yaw/180*PI), 0.0, -cos(yaw/180*PI))
        return v
    }


    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        if (e.player.inventory.itemInMainHand.type == Material.BOW) {
            shoot(e.player)
        }
    }
}