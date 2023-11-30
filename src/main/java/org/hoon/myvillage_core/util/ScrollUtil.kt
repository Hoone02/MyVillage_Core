package org.hoon.myvillage_core.util

fun getSlotChangeDirection(newSlot: Int, oldSlot: Int): String {
    return when {
        newSlot == 0 && oldSlot == 8 -> "오른쪽"
        newSlot == 8 && oldSlot == 0 -> "왼쪽"
        newSlot > oldSlot -> "오른쪽"
        newSlot < oldSlot -> "왼쪽"
        else -> "" // 이 경우는 일반적으로 발생하지 않지만, 혹시 모를 경우를 위해 처리
    }
}