package com.xiaou.plugin.json2dart.utils


import java.math.BigDecimal

fun getTypeName(obj: Any?): String {
    return when (obj) {
        is String -> "String"
        is Int -> "int"
        is Double -> "double"
        is Long -> "int"
        is BigDecimal -> "double"
        is Boolean -> "bool"
        null -> "Null"
        is List<*> -> "List"
        else -> "Class"
    }
}
