package com.xiaou.plugin.json2dart

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.xiaou.plugin.json2dart.utils.MapTypeAdapter
import com.xiaou.plugin.json2dart.utils.getTypeName
import java.util.regex.Pattern

fun map2DartClassDefinition(fileName: String, map: Map<String, Any>): DartClassDefinition {
    val fieldList = mutableListOf<FieldDefinition>()
    val childClassDefinition = mutableListOf<DartClassDefinition>()

    map.entries.forEach {
        if (it.value is Map<*, *>) {
            val dartClassDefinition = map2DartClassDefinition(it.key, it.value as Map<String, Any>)
            childClassDefinition.add(dartClassDefinition)
        } else {
            val typeName = getTypeName(it.value)
            val fieldTypeDefinition = FieldDefinition(it.key, typeName)
            fieldList.add(fieldTypeDefinition)
        }
    }
    return DartClassDefinition(fileName, fieldList, childClassDefinition)
}

fun inputJson2Map(json: String): Map<String, Any> {
    val gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<Map<String, Any>>() {}.type, MapTypeAdapter()).create()
    val originalStr = json.trim()
    return gson.fromJson(originalStr, object : TypeToken<Map<String, Any>>() {}.type)
}


