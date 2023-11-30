package org.hoon.myvillage_core.util

import org.bukkit.entity.Display
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

fun Display.updateTransformation(block: KTransformation.() -> Unit) {
    transformation = KTransformation(transformation).apply(block).toTransformation()
}

fun Display.rotateY (angle: Double) {
    this.updateTransformation {
        leftRotation {
            val rotation = Math.toRadians(angle)
            x = 0.0
            y = sin(rotation / 2)
            z = 0.0
            w = cos(rotation / 2)
        }
    }
}

fun calculateCenterPoint(width: Float, height: Float, rotation: Int): Pair<Double, Double> {
    val centerX = width / 2.0 - if (isOdd(width)) 0.5 else 0.0
    val centerZ = height / 2.0 - if (isOdd(height)) 0.5 else 0.0

    // 회전 각도에 따라 중심점 조정
    return when (rotation) {
        0, 180 -> Pair(centerX, centerZ)
        90, 270 -> Pair(centerZ, centerX)
        else -> throw IllegalArgumentException("Invalid rotation angle: $rotation")
    }
}

class KTransformation(transformation: Transformation) {

    var translation: Vector3f = transformation.translation
    var leftRotation: Quaternionf = transformation.leftRotation
    var scale: Vector3f = transformation.scale
    var rightRotation: Quaternionf = transformation.rightRotation

    fun translation(block: KVector3f.() -> Unit) {
        translation = KVector3f().apply(block).toVector3f()
    }

    fun leftRotation(block: KQuaternionf.() -> Unit) {
        leftRotation = KQuaternionf().apply(block).toQuaternionf()
    }

    fun scale(block: KVector3f.() -> Unit) {
        scale = KVector3f().apply(block).toVector3f()
    }

    fun rightRotation(block: KQuaternionf.() -> Unit) {
        rightRotation = KQuaternionf().apply(block).toQuaternionf()
    }

    fun toTransformation(): Transformation {
        return Transformation(translation, leftRotation, scale, rightRotation)
    }
}

class KVector3f {
    var x: Float = 0.0f
    var y: Float = 0.0f
    var z: Float = 0.0f

    fun toVector3f(): Vector3f {
        return Vector3f(x, y, z)
    }
}

class KQuaternionf {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var w: Double = 0.0

    fun toQuaternionf(): Quaternionf {
        return Quaternionf(x, y, z, w)
    }
}