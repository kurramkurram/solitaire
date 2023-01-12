package io.github.kurramkurram.solitaire.viewmodel

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.*
import io.github.kurramkurram.solitaire.view.CardListAdapter

class SolitaireViewModel : ViewModel() {

    private lateinit var layoutList: MutableList<MutableList<TrumpCard>>
    lateinit var stockList: MutableList<TrumpCard>
    lateinit var foundList: MutableList<MutableList<TrumpCard>>
    private val adapterList = mutableListOf<ArrayAdapter<TrumpCard>>()
    var stockIndex: Int = -1

    /**
     * 初期化.
     */
    fun initCard(context: Context) {
        val shuffleList = shuffleTrump()
        stockList = createStock(shuffleList)
        layoutList = createLayout(shuffleList)
        foundList = createFoundation()
        stockIndex = -1
        adapterList.clear()

        for (list in layoutList) {
            adapterList.add(CardListAdapter(context, list))
        }

        L.d(TAG, "#createStock size = " + stockList.size)
        L.d(TAG, "#createStock size = " + layoutList.size)
    }

    fun getAdapter(): List<ArrayAdapter<TrumpCard>> = adapterList

    class SelectData(val card: TrumpCard, val position: POSITION, val column: Int, val index: Int)

    fun move(data: SelectData): Boolean {
        L.d(TAG, "#move")
        val column = data.column
        val index = data.index
        val card = data.card
        L.d(TAG, "#move card = $card")

        if (card.side == SIDE.BACK) return false

        for (list in foundList) {
            if (canMoveToFound(card, list)) {
                when (data.position) {
                    POSITION.LAYOUT -> {
                        val baseList = layoutList[column]
                        if (baseList.size - 1 == index) {
                            list.add(card)
                            baseList.removeAll(baseList.subList(index, baseList.size))
                            changeToFront(baseList, index)
                            adapterList[column].notifyDataSetChanged()
                            return true
                        }
                    }

                    POSITION.STOCK -> {
                        moveFromStock(card, index, list)
                        return true
                    }
                    else -> {}
                }
            }
        }

        for ((i, list) in layoutList.withIndex()) {
            if (canMoveToLayout(card, list)) {
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
                        changeToFront(baseList, index)
                    }

                    POSITION.STOCK -> moveFromStock(card, index, list)
                }

                adapterList[i].notifyDataSetChanged()
                adapterList[column].notifyDataSetChanged()
                return true
            }
        }
        return false
    }

    fun openStock() {
        stockIndex++
        when {
            stockList.size > 0 && stockIndex < stockList.size -> {
                // 表に変更
                if (stockIndex >= 0) {
                    stockList[stockIndex].side = SIDE.FRONT
                }
            }

            stockIndex == stockList.size -> {
                stockIndex = -1
            }
        }
    }

    fun moveStock() = if (stockIndex >= 0) {
        val data = SelectData(stockList[stockIndex], POSITION.STOCK, 0, stockIndex)
        move(data)
    } else {
        false
    }

    fun moveFound(column: Int): Boolean {
        val data = SelectData(
            foundList[column].last(),
            POSITION.FOUNDATION,
            column,
            foundList[column].size - 1
        )
        return move(data)
    }

    private fun moveFromStock(card: TrumpCard, index: Int, list: MutableList<TrumpCard>) {
        list.add(card)
        stockList.removeAt(index)
        stockIndex--
    }

    private fun canMoveToFound(
        selectCard: TrumpCard,
        list: MutableList<TrumpCard>
    ): Boolean {
        if (list.size == 0) {
            if (selectCard.number == NUMBER.ACE) {
                return true
            }
        } else {
            val last = list.last()
            if (selectCard.number.ordinal == (last.number.ordinal + 1)
                && selectCard.pattern == last.pattern
            ) return true
        }

        return false
    }

    private fun canMoveToLayout(
        selectCard: TrumpCard,
        list: MutableList<TrumpCard>
    ): Boolean {
        if (list.size == 0) {
            if (selectCard.number == NUMBER.KING) {
                return true
            }
        } else {
            val last = list.last()
            if (selectCard.number.ordinal == (last.number.ordinal - 1)
                && ((selectCard.pattern.ordinal % 2 == 0 && last.pattern.ordinal % 2 == 1)
                        || (selectCard.pattern.ordinal % 2 == 1 && last.pattern.ordinal % 2 == 0))
            ) return true
        }

        return false
    }

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
        mutableListOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

    private fun changeToFront(baseList: MutableList<TrumpCard>, index: Int) {
        if (baseList.size > 0) {
            baseList[index - 1].side = SIDE.FRONT
        }
    }

    companion object {
        private const val TAG = "SolitaireViewModel"
        private const val TOTAL_CARD_SIZE = 52
        private const val LAYOUT_CARD_SIZE = 28
        private const val STOCK_CARD_SIZE = TOTAL_CARD_SIZE - LAYOUT_CARD_SIZE
    }
}