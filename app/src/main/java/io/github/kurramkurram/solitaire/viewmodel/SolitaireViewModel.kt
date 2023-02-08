package io.github.kurramkurram.solitaire.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.*

class SolitaireViewModel : ViewModel() {

    private lateinit var stockList: MutableList<TrumpCard>

    private val _spadeFound = MutableLiveData<TrumpCard>()
    val spadeFound: LiveData<TrumpCard>
        get() = _spadeFound

    private val _heartFound = MutableLiveData<TrumpCard>()
    val heartFound: LiveData<TrumpCard>
        get() = _heartFound

    private val _cloverFound = MutableLiveData<TrumpCard>()
    val cloverFound: LiveData<TrumpCard>
        get() = _cloverFound

    private val _diamondFound = MutableLiveData<TrumpCard>()
    val diamondFound: LiveData<TrumpCard>
        get() = _diamondFound

    private var listFound: List<MutableLiveData<TrumpCard>> =
        listOf(_spadeFound, _heartFound, _cloverFound, _diamondFound)

    private val _listLayout = MutableLiveData<MutableList<MutableList<TrumpCard>>>(mutableListOf())
    val listLayout: LiveData<MutableList<MutableList<TrumpCard>>>
        get() = _listLayout

    private var stockIndex: Int = -1
    private val initialCard = TrumpCard(NUMBER.NONE, PATTERN.CLOVER, MutableLiveData(SIDE.BACK))

    private var _openCard = MutableLiveData<TrumpCard>()
    val openCard: LiveData<TrumpCard>
        get() = _openCard

    private var _count = MutableLiveData(0)
    val count: LiveData<Int>
        get() = _count

    init {
        initCard()
    }

    /**
     * 初期化.
     */
    private fun initCard() {
        val shuffleList = shuffleTrump()
        stockList = createStock(shuffleList)

        _spadeFound.value = TrumpCard(NUMBER.NONE, PATTERN.SPADE, MutableLiveData(SIDE.FRONT))
        _heartFound.value = TrumpCard(NUMBER.NONE, PATTERN.HEART, MutableLiveData(SIDE.FRONT))
        _cloverFound.value = TrumpCard(NUMBER.NONE, PATTERN.CLOVER, MutableLiveData(SIDE.FRONT))
        _diamondFound.value = TrumpCard(NUMBER.NONE, PATTERN.DIAMOND, MutableLiveData(SIDE.FRONT))

        createLayout(shuffleList)
        stockIndex = -1
        _openCard.value = initialCard
    }

    /**
     * カードの移動.
     */
    private fun move(data: SelectData) {
        L.d(TAG, "#move")
        val column = data.column
        val index = data.index
        val card = data.card
        L.d(TAG, "#move card = $card \n index = $index column = $column")

        if (card.side.value == SIDE.BACK) return

        // 組札へ移動
        for (item in listFound) {
            if (canMoveToFound(card, item)) {
                when (data.position) {
                    POSITION.LAYOUT -> {
                        val baseList = _listLayout.value!![column]
                        if (baseList.size - 1 == index) {
                            item.value = card
                            baseList.removeAll(baseList.subList(index, baseList.size))
                            changeToFront(baseList, index)
                            _listLayout.value = _listLayout.value
                            countUp()
                            return
                        }
                    }

                    POSITION.STOCK -> {
                        item.apply {
                            card.isLast.value = true
                            value = card
                        }
                        stockList.removeAt(index)
                        stockIndex--
                        if (stockIndex >= 0 && stockList.size > 0) {
                            _openCard.value = stockList[stockIndex]
                        } else {
                            _openCard.value = initialCard
                        }
                        countUp()
                        return
                    }
                    else -> return
                }
            }
        }

        // 場札へ移動
        for (list in _listLayout.value!!) {
            if (canMoveToLayout(card, list)) {
                if (list.isNotEmpty()) {
                    list.last().isLast.value = false
                }
                when (data.position) {
                    POSITION.FOUNDATION -> {
                        list.add(card)
                        val selected = listFound[column]
                        selected.value = TrumpCard(
                            NUMBER.getNumber(selected.value!!.number.ordinal - 1),
                            selected.value!!.pattern,
                            MutableLiveData(SIDE.FRONT),
                            MutableLiveData(true)
                        )
                    }

                    POSITION.LAYOUT -> {
                        val baseList = _listLayout.value!![column]
                        val moveList = ArrayList(baseList.subList(index, baseList.size))
                        list.addAll(moveList)
                        baseList.removeAll(baseList.subList(index, baseList.size))
                        changeToFront(baseList, index)
                    }

                    POSITION.STOCK -> {
                        card.apply {
                            isLast.value = true
                            list.add(this)
                        }
                        stockList.apply {
                            removeAt(index)
                            stockIndex--
                            if (stockIndex >= 0 && stockList.size > 0) {
                                _openCard.value = stockList[stockIndex]
                            } else {
                                _openCard.value = initialCard
                            }
                        }
                    }
                }
                _listLayout.value = _listLayout.value
                countUp()
                return
            }
        }
        return
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
                    stockList[stockIndex].side.value = SIDE.FRONT
                    _openCard.value = stockList[stockIndex]
                    countUp()
                }
            }

            stockList.size == 0 -> {
                _openCard.value = initialCard
            }

            stockIndex == stockList.size -> {
                stockIndex = -1
                _openCard.value = initialCard
            }
        }
    }

    private fun countUp() {
        _count.value = (_count.value ?: 0) + 1
    }

    /**
     * めくった山札を移動する.
     */
    fun moveStock() {
        if (stockIndex >= 0 && stockList.size > stockIndex) {
            val data = SelectData(stockList[stockIndex], POSITION.STOCK, 0, stockIndex)
            move(data)
        }
    }

    /**
     * 組札を移動する.
     */
    fun moveFound(column: Int) = move(
        SelectData(
            listFound[column].value!!,
            POSITION.FOUNDATION,
            column
        )
    )

    /**
     * 組札へ移動できるかの判定.
     */
    private fun canMoveToFound(
        selectCard: TrumpCard,
        item: MutableLiveData<TrumpCard>
    ): Boolean {
        val last = item.value ?: return false
        if (selectCard.pattern != last.pattern) return false
        if (selectCard.number.ordinal == (last.number.ordinal + 1)) return true
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
                    add(TrumpCard(number, pattern, MutableLiveData(SIDE.BACK)))
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
    fun createLayout(list: MutableList<TrumpCard>) {
        _listLayout.value = mutableListOf<MutableList<TrumpCard>>().apply {
            var size = 1
            var start = STOCK_CARD_SIZE
            var end = start + size
            run {
                repeat(LAYOUT_CARD_SIZE) {
                    L.d(TAG, "#createLayout start = $start end = $end size = $size")
                    val mutableList = mutableListOf<TrumpCard>()
                    mutableList.addAll(list.subList(start, end))
                    mutableList.last().apply {
                        side.value = SIDE.FRONT
                        isLast.value = true
                        mutableList[mutableList.size - 1] = this
                    }

                    start += size
                    size++
                    end = start + size
                    add(mutableList)

                    if (end > TOTAL_CARD_SIZE) return@run
                }
            }
        }
    }

    /**
     * 場札を表向きにする.
     */
    private fun changeToFront(baseList: MutableList<TrumpCard>, index: Int) {
        if (baseList.size > 0) {
            baseList[index - 1].side.value = SIDE.FRONT
            baseList[index - 1].isLast.value = true
        }
    }

    /**
     * 場札を選択.
     */
    fun onItemClick(item: TrumpCard) {
        getSelectData(item)?.let {
            move(it)
        }
    }

    /**
     * 場札を検索.
     */
    private fun getSelectData(card: TrumpCard): SelectData? {
        for ((i, list) in _listLayout.value!!.withIndex()) {
            for ((j, item) in list.withIndex()) {
                if (item == card) {
                    return SelectData(card, POSITION.LAYOUT, i, j)
                }
            }
        }
        return null
    }

    /**
     * Restartボタン選択.
     */
    fun onRestartClick() = initCard()

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getCardId(context: Context, card: TrumpCard): Drawable? = when {
        card.number == NUMBER.NONE -> {
            context.resources.getDrawable(R.drawable.ic_launcher_foreground, null)
        }
        card.side.value == SIDE.BACK -> {
            context.resources.getDrawable(R.drawable.trump_background, null)
        }
        else -> {
            val array = context.resources.obtainTypedArray(R.array.trump_card_array)
            val resourceId = array.getResourceId(card.id, 0)
            array.recycle()
            context.getDrawable(resourceId)
        }
    }

    private data class SelectData(
        val card: TrumpCard,
        val position: POSITION,
        val column: Int,
        val index: Int = -1
    )

    companion object {
        private const val TAG = "SolitaireViewModel"
        private const val TOTAL_CARD_SIZE = 52
        private const val LAYOUT_CARD_SIZE = 28
        private const val STOCK_CARD_SIZE = TOTAL_CARD_SIZE - LAYOUT_CARD_SIZE
    }
}

