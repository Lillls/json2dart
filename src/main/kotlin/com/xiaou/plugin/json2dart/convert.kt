package com.xiaou.plugin.json2dart

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.xiaou.plugin.json2dart.utils.MapTypeAdapter
import com.xiaou.plugin.json2dart.utils.getTypeName

@Suppress("UNCHECKED_CAST")
fun map2CustomClassDefinition(fileName: String, map: Map<String, Any>): CustomClassType {
    val fieldList = mutableListOf<TypeDefinition>()
    map.entries.forEach {
        when (it.value) {
            is Map<*, *> -> {
                val customClassType = map2CustomClassDefinition(it.key, it.value as Map<String, Any>)
                fieldList.add(customClassType)
            }
            is List<*> -> {
                val listValue = it.value as List<*>
                listValue.firstOrNull()?.apply {
                    if (this is Map<*, *>) {
                        val customClassType = map2CustomClassDefinition(it.key, this as Map<String, Any>)
                        fieldList.add(ListClassType(it.key, customClassType))
                    } else {
                        fieldList.add(ListClassType(it.key, InternalClassType(it.key, getTypeName(this))))
                    }
                }

            }
            else -> {
                val typeName = getTypeName(it.value)
                val internalClassType = InternalClassType(it.key, typeName)
                fieldList.add(internalClassType)
            }
        }
    }
    return CustomClassType(fileName, fieldList)
}

fun parseInputJson(json: String): Map<String,Any> {
    val gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<Map<String, Any>>() {}.type, MapTypeAdapter()).create()
    val originalStr = json.trim()
    return gson.fromJson(originalStr, object : TypeToken<Map<String, Any>>() {}.type)
}


