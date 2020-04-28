package com.xiaou.plugin.json2dart.utils

import java.util.regex.Pattern

//首字母转大写
fun String.toUpperCaseFirstOne(): String {
    return when {
        isEmpty() -> ""
        Character.isUpperCase(this[0]) -> this
        else -> StringBuilder().append(Character.toUpperCase(this[0])).append(this.substring(1)).toString()
    }
}

//首字母转小写
fun String.toLowerCaseFirstOne(): String {
    return if (Character.isLowerCase(this[0]))
        this
    else
        StringBuilder().append(Character.toLowerCase(this[0])).append(this.substring(1)).toString()
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
fun String.underline2Hump(): String {
    val linePattern = Pattern.compile("_([a-z])")
    val matcher = linePattern.matcher(this)
    val sb = StringBuffer()
    while (matcher.find()) {
        matcher.appendReplacement(sb, matcher.group(1).toUpperCase())
    }
    matcher.appendTail(sb)
    return sb.toString()
}

//驼峰命名转_命名
fun String.hump2Underline(): String {
    val humpPattern = Pattern.compile("\\B(\\p{Upper})(\\p{Lower}*)")
    val matcher = humpPattern.matcher(this)
    val sb = StringBuffer()
    while (matcher.find()) {
        matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
    }
    matcher.appendTail(sb)
    return sb.toString()
}