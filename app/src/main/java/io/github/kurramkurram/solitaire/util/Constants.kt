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

const val DIALOG_RESULT = "dialog_result"
const val DIALOG_RESULT_KEY = "dialog_result_key"
const val DIALOG_RESULT_OK = 0
const val DIALOG_RESULT_NG = 1

const val SHOW_DIALOG_KEY = "show_dialog_key"