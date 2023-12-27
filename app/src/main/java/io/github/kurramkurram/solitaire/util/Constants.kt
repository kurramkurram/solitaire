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

const val DIALOG_RESULT_RESTART = "dialog_result_restart"
const val DIALOG_RESULT_RESET = "dialog_result_rest"
const val DIALOG_RESULT_AUTO_COMPLETE = "dialog_result_auto_complete"
const val DIALOG_RESULT_KEY = "dialog_result_key"
const val DIALOG_RESULT_OK = 0
const val DIALOG_RESULT_NG = 1

const val SHOW_DIALOG_KEY = "show_dialog_key"

const val DATE_PATTERN = "yyyy/MM/dd"
const val DATE_PATTERN_HH_MM = "yyyy/MM/dd HH:mm"

const val RECORD_RESULT_DATA = "record_result_data"

const val CLICKED_MOVIE_ITEM = "clicked_movie_item"
const val CLICKED_RESET_BUTTON = "clicked_reset_button"
