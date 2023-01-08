package io.github.kurramkurram.solitaire.viewmodel

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.*
import io.github.kurramkurram.solitaire.view.CardListAdapter

class SolitaireViewModel : ViewModel() {

    lateinit var layoutList: MutableList<MutableList<TrumpCard>>
    lateinit var stockList: MutableList<TrumpCard>
    lateinit var foundList: MutableList<MutableList<TrumpCard>>
    private val adapterList = mutableListOf<ArrayAdapter<TrumpCard>>()

    /**
     * 初期化.
     */
    fun initCard() {
        val shuffleList = shuffleTrump()
        stockList = createStock(shuffleList)
        layoutList = createLayout(shuffleList)
        foundList = createFoundation()

        L.d(TAG, "#createStock size = " + stockList.size)
        L.d(TAG, "#createStock size = " + layoutList.size)
    }

    fun getAdapter(context: Context): List<ArrayAdapter<TrumpCard>> {
        if (adapterList.isEmpty()) {
            for (list in layoutList) {
                adapterList.add(CardListAdapter(context, list))
            }
        }

        return adapterList
    }

    class SelectData(val card: TrumpCard, val position: POSITION, val column: Int, val index: Int)

    fun move(data: SelectData): Boolean {
        val column = data.column
        val index = data.index
        val card = data.card

        if (card.side == SIDE.BACK) {
            return false
        }

        for ((i, list) in layoutList.withIndex()) {
            if (list.isEmpty()) {
                return false
            }
            val last = list.last()

            if (canMoveToLayout(card, last, list)) {
                when (data.position) {
                    POSITION.FOUNDATION -> {
                        list.add(card)
                        val baseList = foundList[column]
                        baseList.removeAt(index)
                    }

                    POSITION.LAYOUT -> {
                        val baseList = layoutList[column]
                        val moveList = ArrayList(baseList.subList(index, baseList.size))
                        list.addAll(moveList)
                        baseList.removeAll(baseList.subList(index, baseList.size))
                        if (baseList.size > 0) {
                            baseList[index - 1].side = SIDE.FRONT
                        }
                    }

                    POSITION.STOCK -> {
                        list.add(card)
                        stockList.removeAt(index)
                    }
                }

                adapterList[i].notifyDataSetChanged()
                adapterList[column].notifyDataSetChanged()
                return true
            }
        }

        if (card != layoutList[column].last()) {
            return false
        }

        for ((i, list) in foundList.withIndex()) {
            val last = list.last()

            if (canMoveToFound(card, last, list)) {
                when (data.position) {
                    POSITION.LAYOUT -> {
                        list.add(card)
                        val baseList = layoutList[column]
                        baseList.removeAt(index)
                    }
                    POSITION.STOCK -> {
                        list.add(card)
                        stockList.removeAt(index)
                    }
                    else -> {}
                }
                adapterList[i].notifyDataSetChanged()
                adapterList[column].notifyDataSetChanged()
//                for ((num, list) in layoutList.withIndex()) {
//                    L.d(TAG, "#move $num num = ${list.size}")
//                }
                return true
            }
        }

        return false
    }

    private fun canMove(
        selectCard: TrumpCard,
        lastCard: TrumpCard,
        first: NUMBER,
        list: MutableList<TrumpCard>
    ): Boolean {
        if (selectCard.number == first && list.size == 0) return true

        if (selectCard.number.ordinal == (lastCard.number.ordinal - 1)
            && ((selectCard.pattern.ordinal % 2 == 0 && lastCard.pattern.ordinal % 2 == 1)
                    || (selectCard.pattern.ordinal % 2 == 1 && lastCard.pattern.ordinal % 2 == 0))
        ) return true

        return false
    }

    private fun canMoveToFound(
        selectCard: TrumpCard,
        lastCard: TrumpCard,
        list: MutableList<TrumpCard>
    ): Boolean = canMove(selectCard, lastCard, NUMBER.ACE, list)

    private fun canMoveToLayout(
        selectCard: TrumpCard,
        lastCard: TrumpCard,
        list: MutableList<TrumpCard>
    ): Boolean = canMove(selectCard, lastCard, NUMBER.KING, list)

    @VisibleForTesting
    fun shuffleTrump(): MutableList<TrumpCard> = mutableListOf<TrumpCard>().apply {
        NUMBER.values().forEach { number ->
            if (number != NUMBER.NONE) {
                PATTERN.values().forEach { pattern ->
                    add(TrumpCard(number, pattern, SIDE.BACK))
                }
            }
        }
        shuffle()
    }

    @VisibleForTesting
    fun createStock(list: MutableList<TrumpCard>): MutableList<TrumpCard> =
        list.subList(0, STOCK_CARD_SIZE)

    @VisibleForTesting
    fun createLayout(list: MutableList<TrumpCard>): MutableList<MutableList<TrumpCard>> =
        mutableListOf<MutableList<TrumpCard>>().apply {
            var size = 1
            var start = STOCK_CARD_SIZE
            var end = start + size
            run {
                repeat(LAYOUT_CARD_SIZE) {
                    L.d(TAG, "#createLayout start = $start end = $end size = $size")
                    val mutableList = mutableListOf<TrumpCard>()
                    mutableList.addAll(list.subList(start, end))
                    mutableList.last().apply {
                        side = SIDE.FRONT
                        mutableList[mutableList.size - 1] = this
                    }

                    start += size
                    size++
                    end = start + size
                    add(mutableList)

                    if (end > TOTAL_CARD_SIZE) {
                        return@run
                    }
                }
            }
        }

    @VisibleForTesting
    fun createFoundation(): MutableList<MutableList<TrumpCard>> =
        mutableListOf<MutableList<TrumpCard>>().apply {
            PATTERN.values().forEach { pattern ->
                add(mutableListOf(TrumpCard(NUMBER.NONE, pattern, SIDE.BACK)))
            }
        }

    companion object {
        private const val TAG = "SolitaireViewModel"
        private const val TOTAL_CARD_SIZE = 52
        private const val LAYOUT_CARD_SIZE = 28
        private const val STOCK_CARD_SIZE = TOTAL_CARD_SIZE - LAYOUT_CARD_SIZE
    }
}