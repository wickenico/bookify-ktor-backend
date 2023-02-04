package com.nw.enums

enum class ReadStatusEnum(val status: String) {
    UNREAD("UNREAD"),
    READING("READING"),
    READ("READ"),
    UNFINISHED("UNFINISHED");

    companion object {
        fun getByValue(status: String): ReadStatusEnum {
            return values().find { it.status == status }
                ?: throw IllegalArgumentException("$status is not a valid StatusEnum")
        }
    }
}
