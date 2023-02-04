package com.nw.enums

enum class PrintTypeEnum(val type: String) {
    BOOK("BOOK"),
    EBOOK("EBOOK"),
    MAGAZINE("MAGAZINE");

    companion object {
        fun getByValue(type: String): PrintTypeEnum {
            return values().find { it.type == type }
                ?: throw IllegalArgumentException("$type is not a valid PrintType")
        }
    }
}
