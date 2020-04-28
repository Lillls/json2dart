package com.xiaou.plugin.json2dart

import com.xiaou.plugin.json2dart.utils.toUpperCaseFirstOne
import com.xiaou.plugin.json2dart.utils.underline2Hump

class DartClassDefinition(
    val fileName: String,
    val fields: List<FieldDefinition>,
    val childClassDefinition: List<DartClassDefinition>
) {
    val className: String = fileName
        get() {
            return field.underline2Hump().toUpperCaseFirstOne()
        }
}


data class FieldDefinition(val fieldName: String, val fieldType: String)