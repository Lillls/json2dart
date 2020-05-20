package com.xiaou.plugin.json2dart

import com.xiaou.plugin.json2dart.utils.toUpperCaseFirstOne
import com.xiaou.plugin.json2dart.utils.underline2Hump

abstract class TypeDefinition(val name: String, var typeName: String)

/**
 * custom class type
 * need generate class code
 */
class CustomClassType(
    name: String,
    val fieldList: List<TypeDefinition>
) : TypeDefinition(name, name.underline2Hump().toUpperCaseFirstOne()) {
}

/**
 * dart language internal class type
 * like int,double,String,bool
 * don't need generate class code
 */
class InternalClassType(name: String, fieldType: String) : TypeDefinition(name, fieldType)

class ListClassType(name: String, val genericsType: TypeDefinition) :
    TypeDefinition(name, "List<${genericsType.typeName}>")