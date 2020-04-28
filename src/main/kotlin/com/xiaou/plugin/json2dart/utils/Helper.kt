package com.xiaou.plugin.json2dart.utils


import java.math.BigDecimal

fun getTypeName(obj: Any?): String {
    return when (obj) {
        is String -> /*if (DateUtil.canParseDate(obj.toString())) "DateTime" else*/ "String"
        is Int -> "int"
        is Double -> "double"
        is Long -> "int"
        is BigDecimal -> "double"
        is Boolean -> "bool"
        null -> "null"
        is List<*> -> "List"
        else -> "Class"
    }
}
