package com.xiaou.plugin.json2dart.utils

import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.*


/**
 * 为了解决number类型在解析的过程中
 * int会被转成double
 */
class MapTypeAdapter : TypeAdapter<Any>() {

    override fun read(reader: JsonReader): Any {
        when (reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                val list = ArrayList<Any>()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(read(reader))
                }
                reader.endArray()
                return list
            }

            JsonToken.BEGIN_OBJECT -> {
                val map = LinkedTreeMap<String, Any>()
                reader.beginObject()
                while (reader.hasNext()) {
                    map[reader.nextName()] = read(reader)
                }
                reader.endObject()
                return map
            }

            JsonToken.STRING -> return reader.nextString()

            JsonToken.NUMBER -> {
                /**
                 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                 */
                val dbNum = reader.nextString()
                return if (!dbNum.contains(".")) {
                    //返回0是int
                    0
                } else {
                    //返回double类型代表是double类型
                    0.0
                }
            }

            JsonToken.BOOLEAN -> return reader.nextBoolean()

            else -> throw IllegalStateException()
        }
    }

    override fun write(out: JsonWriter, value: Any) {
    }

}
