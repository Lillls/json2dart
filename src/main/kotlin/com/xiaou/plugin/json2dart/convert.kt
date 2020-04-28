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


/**
 *
 * 下划线的命名方式转驼峰
 * matcher.group用法.
 * group(0),符合整个正则串
 * group(1),符合一个正则串一个()
 * 例如 正则为 _([a-z]),传入is_bind
 * group(0) 为 _b,  符合整个正则
 * group(1) 为  b,  符合一个正则串一个()
 *
 */
fun underlineToHump(str: String): String {
    val linePattern = Pattern.compile("_([a-z])")
    val matcher = linePattern.matcher(str)
    val sb = StringBuffer()
    while (matcher.find()) {
        matcher.appendReplacement(sb, matcher.group(1).toUpperCase())
    }
    matcher.appendTail(sb)
    return sb.toString()
}

