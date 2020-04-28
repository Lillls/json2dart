package com.xiaou.plugin.json2dart.utils

import com.xiaou.plugin.json2dart.DartClassDefinition
import com.xiaou.plugin.json2dart.FieldDefinition
import com.xiaou.plugin.json2dart.underlineToHump


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
        return sb.toString()
    }

    fun fieldsStr(field: FieldDefinition): String {
        val sb = StringBuilder()
        sb.append("  @JsonKey(name: \"${field.fieldName}\")")
        sb.append("\n")
        sb.append("  ${field.fieldType} ${underlineToHump(field.fieldName)};")
        return sb.toString()
    }

    fun childClassDefinitionStr(dartClass: DartClassDefinition): String {
        return "  ${dartClass.className} ${underlineToHump(dartClass.fileName)};"
    }

    fun constructorStr(dartClass: DartClassDefinition): String {
        val sb = StringBuilder()
        sb.append("  ${dartClass.className}(")

        val constructorStr = StringBuilder()
        if (dartClass.fields.isNotEmpty()) {
            constructorStr.append("{")
        }
        dartClass.fields.forEach {
            constructorStr.append("this.${underlineToHump(it.fieldName)}")
            constructorStr.append(", ")
        }

        if (constructorStr.isEmpty() && dartClass.childClassDefinition.isNotEmpty()) {
            constructorStr.append("{")
        }

        dartClass.childClassDefinition.forEach {
            constructorStr.append("this.${underlineToHump(it.fileName)}")
            constructorStr.append(", ")
        }
        if (constructorStr.isNotEmpty()) {
            constructorStr.setLength(constructorStr.length - 2)
            constructorStr.append("}")
        }
        sb.append(constructorStr)
        sb.append(")")
        sb.append(";")
        return sb.toString()
    }

    fun factoryConstructorStr(className: String): String {
        return "  factory $className.fromJson(Map<String, dynamic> json) => _$${className}FromJson(json);"
    }

    fun toJsonStr(className: String): String {
        return "  Map<String, dynamic> toJson() => _$${className}ToJson(this);"
    }


    fun dartClassEndStr(): String {
        return "}"
    }

}