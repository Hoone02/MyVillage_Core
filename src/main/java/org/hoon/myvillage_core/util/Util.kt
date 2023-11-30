package org.hoon.myvillage_core.util

fun isOdd(value: Float): Boolean {
    val intValue = value.toInt()
    return intValue % 2 != 0
}