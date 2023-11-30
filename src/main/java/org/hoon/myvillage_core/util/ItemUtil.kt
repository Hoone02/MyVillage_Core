package org.hoon.myvillage_core.util

import org.bukkit.inventory.meta.ItemMeta

fun ItemMeta.addTag(key: String, value: String) {
    val tag = ItemUtil().addResult(this.localizedName, key, value)
    this.setLocalizedName(tag)
}

fun ItemMeta.getTag(key: String): String? {
    val tag = ItemUtil().getResult(this.localizedName, key)
    if (tag != null) {
        return tag
    }
    return null
}

class ItemUtil {
    private fun parseKeyValueString(input: String): Map<String, String> {
        val keyValuePairList = input.split(",").map { it.trim() }
        val resultMap = mutableMapOf<String, String>()

        for (keyValuePair in keyValuePairList) {
            val (key, value) = keyValuePair.split(":").map { it.trim() }
            resultMap[key] = value
        }

        return resultMap
    }

    private fun mapToListString(inputMap: Map<String, String>): MutableList<String> {
        return inputMap.map { "${it.key}:${it.value}" }.toMutableList()
    }

    fun addResult(input: String, key: String, value: String): String {
        if (input.isEmpty()) {
            return "$key:$value"
        }
        val toMap = parseKeyValueString(input)
        val toList = mapToListString(toMap)
        toList.add("$key:$value")
        return toList.joinToString(",")
    }

    fun getResult(input: String, key: String): String? {
        val toMap = parseKeyValueString(input)
        return toMap[key]
    }
}