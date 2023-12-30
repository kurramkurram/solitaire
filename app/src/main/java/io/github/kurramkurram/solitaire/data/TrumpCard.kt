package io.github.kurramkurram.solitaire.data

import androidx.lifecycle.MutableLiveData
import io.github.kurramkurram.solitaire.util.NUMBER
import io.github.kurramkurram.solitaire.util.PATTERN
import io.github.kurramkurram.solitaire.util.SIDE

/**
 * トランプカード.
 */
data class TrumpCard(
    val number: NUMBER,
    val pattern: PATTERN,
    var side: MutableLiveData<SIDE>,
    var isLast: MutableLiveData<Boolean> = MutableLiveData(false)
) {
    val id: Int = number.ordinal - 1 + pattern.ordinal * 13

    override fun toString(): String {
        return "--------------" +
            " \n$TAG " +
            " \n id = " + id +
            " \n number = " + number.ordinal +
            " \n pattern = " + pattern.ordinal +
            " \n side = " + side.value!!.ordinal +
            " \n --------------"
    }

    override fun equals(other: Any?): Boolean {
        val otherCard = other as TrumpCard
        return id == otherCard.id
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + pattern.hashCode()
        result = 31 * result + side.hashCode()
        result = 31 * result + id
        return result
    }

    companion object {
        private const val TAG = "TrumpCard"
    }
}
