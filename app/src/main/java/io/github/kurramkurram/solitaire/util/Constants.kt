package io.github.kurramkurram.solitaire.util

import android.content.res.Resources.NotFoundException

enum class NUMBER {
    NONE,
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    companion object {
        fun getNumber(num: Int): NUMBER =
            values().find { it.ordinal == num } ?: throw NotFoundException()
    }
}

enum class PATTERN {
    SPADE,
    HEART,
    CLOVER,
    DIAMOND
}

enum class SIDE {
    FRONT,
    BACK
}

enum class POSITION {
    STOCK,
    LAYOUT,
    FOUNDATION
}