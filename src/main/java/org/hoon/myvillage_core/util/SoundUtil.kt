package org.hoon.myvillage_core.util

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity

fun Location.sound(sound: Sound, volume: Float, pitch: Float) {
    for (player in this.world.players) {
        player.playSound(this, sound, volume, pitch)
    }
}

fun Entity.sound(sound: Sound, volume: Float, pitch: Float) {
    for (player in this.world.players) {
        player.playSound(this, sound, volume, pitch)
    }
}