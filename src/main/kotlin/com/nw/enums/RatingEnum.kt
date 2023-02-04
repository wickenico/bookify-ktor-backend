package com.nw.enums

enum class RatingEnum(val rating: Int) {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    companion object {
        fun getByValue(rating: Int): RatingEnum {
            return values().find { it.rating == rating }
                ?: throw IllegalArgumentException("$rating is not a valid Rating.")
        }
    }
}
