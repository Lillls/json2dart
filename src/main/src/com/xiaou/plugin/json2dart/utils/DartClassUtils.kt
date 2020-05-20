package com.xiaou.plugin.json2dart.utils

import com.xiaou.plugin.json2dart.CustomClassType
import com.xiaou.plugin.json2dart.TypeDefinition


object DartClassUtils {
    private const val IMPORT_CONSTANT = """import 'package:json_annotation/json_annotation.dart'; """

    fun insertClassHead(fileName: String, originalContent: String): String {
        val sb = StringBuilder()
        sb.append(IMPORT_CONSTANT)
        sb.append("\n")
        sb.append("\n")
        sb.append(partStr(fileName))
        sb.append("\n")
        sb.append("\n")
        sb.append(originalContent)
        return sb.toString()
    }

    private fun partStr(fileName: String): String {
        return "part '$fileName.g.dart'; "
    }

    fun dartClassStartStr(className: String): String {
        val sb = StringBuilder()
        sb.append("@JsonSerializable(nullable: true)")
        sb.append("\n")
        sb.append("class $className {")
        sb.append("\n")
        return sb.toString()
    }

    fun fieldsStr(fields: List<TypeDefinition>): String {
        val sb = StringBuilder()
        fields.forEach {
            sb.append("  @JsonKey(name: \"${it.name}\")")
            sb.append("\n")
            sb.append("  ${it.typeName} ${it.name.underline2Hump()};")
            sb.append("\n")
        }
        return sb.toString()
    }


    fun constructorStr(dartClass: CustomClassType): String {
        val sb = StringBuilder()
        sb.append("  ${dartClass.typeName}(")

        val constructorStr = StringBuilder()
        if (dartClass.fieldList.isNotEmpty()) {
            constructorStr.append("{")
            dartClass.fieldList.forEach {
                constructorStr.append("this.${it.name.underline2Hump()}")
                constructorStr.append(", ")
            }
            constructorStr.setLength(constructorStr.length - 2)
            constructorStr.append("}")
        }
        sb.append(constructorStr)
        sb.append(")")
        sb.append(";")
        sb.append("\n")
        return sb.toString()
    }

    fun factoryConstructorStr(className: String): String {
        return "  factory $className.fromJson(Map<String, dynamic> json) => _$${className}FromJson(json);\n"
    }

    fun toJsonStr(className: String): String {
        return "  Map<String, dynamic> toJson() => _$${className}ToJson(this);\n"
    }

    fun dartClassEndStr(): String {
        return "}\n"
    }

}