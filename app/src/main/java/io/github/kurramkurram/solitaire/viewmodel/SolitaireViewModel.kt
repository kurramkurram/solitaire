package io.github.kurramkurram.solitaire.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.*
class SolitaireViewModel : ViewModel() {

    private lateinit var layoutList: MutableList<MutableList<TrumpCard>>
    lateinit var stockList: MutableList<TrumpCard>
    lateinit var foundList: MutableList<MutableList<TrumpCard>>
    var stockIndex: Int = -1

    val listLayout = MutableLiveData<List<List<TrumpCard>>>(emptyList())

    init {
        initCard()
    }

    /**
     * 初期化.
     */
    private fun initCard() {
        val shuffleList = shuffleTrump()
        stockList = createStock(shuffleList)
        layoutList = createLayout(shuffleList)
        foundList = createFoundation()
        stockIndex = -1
    }

    data class SelectData(
        val card: TrumpCard,
        val position: POSITION,
        val column: Int,
        val index: Int
    )

    /**
     * カードの移動.
     */
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
                return true
            }
        }
        return false
    }

    /**
     * 山札をめくる.
     */
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

    /**
     * めくった山札を移動する.
     */
    fun moveStock() = if (stockIndex >= 0) {
        val data = SelectData(stockList[stockIndex], POSITION.STOCK, 0, stockIndex)
        move(data)
    } else {
        false
    }

    /**
     * 組札を移動する.
     */
    fun moveFound(column: Int): Boolean = move(
        SelectData(
            foundList[column].last(),
            POSITION.FOUNDATION,
            column,
            foundList[column].size - 1
        )
    )

    /**
     * 山札からの移動処理を共通化.
     */
    private fun moveFromStock(card: TrumpCard, index: Int, list: MutableList<TrumpCard>) {
        list.add(card)
        stockList.removeAt(index)
        stockIndex--
    }

    /**
     * 組札へ移動できるかの判定.
     */
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

    /**
     * 場札で移動できるかの判定.
     */
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

    /**
     * 初期のカード配置のシャッフル.
     */
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

    /**
     * 山札を作成.
     */
    @VisibleForTesting
    fun createStock(list: MutableList<TrumpCard>): MutableList<TrumpCard> =
        list.subList(0, STOCK_CARD_SIZE)

    /**
     * 場札を作成.
     */
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

    /**
     * 場札を表向きにする.
     */
    private fun changeToFront(baseList: MutableList<TrumpCard>, index: Int) {
        if (baseList.size > 0) {
            baseList[index - 1].side = SIDE.FRONT
        }
    }

    fun onItemClick(item: TrumpCard) {
        getSelectData(item)?.let {
            move(it)
        }
    }

    private fun getSelectData(card: TrumpCard): SelectData? {
        for ((i, list) in foundList.withIndex()) {
            for ((j, item) in list.withIndex()) {
                if (item == card) {
                    return SelectData(card, POSITION.LAYOUT, i, j)
                }
            }
        }
        return null
    }

    fun onRestartClick() {
//        initCard()

        L.d(TAG, "#onRestartClick")
        val item: List<List<TrumpCard>> =
            listOf(listOf(TrumpCard(NUMBER.ACE, PATTERN.CLOVER, SIDE.BACK)))
        listLayout.value = (listLayout.value ?: emptyList()) + item
    }

    companion object {
        private const val TAG = "SolitaireViewModel"
        private const val TOTAL_CARD_SIZE = 52
        private const val LAYOUT_CARD_SIZE = 28
        private const val STOCK_CARD_SIZE = TOTAL_CARD_SIZE - LAYOUT_CARD_SIZE
    }
}

