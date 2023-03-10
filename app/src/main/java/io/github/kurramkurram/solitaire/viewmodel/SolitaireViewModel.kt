package io.github.kurramkurram.solitaire.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.repository.RecordRepositoryImpl
import io.github.kurramkurram.solitaire.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SolitaireViewModel(application: Application) : AndroidViewModel(application) {

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

    private val listFound: List<MutableLiveData<TrumpCard>> =
        listOf(_spadeFound, _heartFound, _cloverFound, _diamondFound)

    private val _listLayout = MutableLiveData<MutableList<MutableList<TrumpCard>>>(mutableListOf())
    val listLayout: LiveData<MutableList<MutableList<TrumpCard>>>
        get() = _listLayout

    private var stockIndex: Int = -1
    private val initialCard = TrumpCard(NUMBER.NONE, PATTERN.CLOVER, MutableLiveData(SIDE.FRONT))

    private val _openCard = MutableLiveData<TrumpCard>()
    val openCard: LiveData<TrumpCard>
        get() = _openCard

    private val _closeCard = MutableLiveData<TrumpCard?>()
    val closeCard: LiveData<TrumpCard?>
        get() = _closeCard

    private val backCard = TrumpCard(NUMBER.NONE, PATTERN.CLOVER, MutableLiveData(SIDE.BACK))

    private val _count = MutableLiveData(0)
    val count: LiveData<Int>
        get() = _count

    private var timer: Timer? = null
    private val _time = MutableLiveData(0L)
    val time: LiveData<Long>
        get() = _time

    private val _complete = MutableLiveData(false)
    val complete: LiveData<Boolean>
        get() = _complete

    private var checkCardIndex = 0

    private val _canComplete = MutableLiveData(false)
    val canComplete: LiveData<Boolean>
        get() = _canComplete

    val isChecked = MutableLiveData(false)

    private val recordRepository = RecordRepositoryImpl(application.applicationContext)

    init {
        initCard()
    }

    /**
     * ?????????.
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
        _closeCard.value = backCard
        _count.value = 0
        _complete.value = false
        checkCardIndex = 0
        _canComplete.value = false
        clearTimer()
    }

    /**
     * ??????????????????.
     */
    private fun move(data: SelectData): Boolean {
        L.d(TAG, "#move")
        val column = data.column
        val index = data.index
        val card = data.card
        L.d(TAG, "#move card = $card \n index = $index column = $column")

        if (card.side.value == SIDE.BACK) return false

        var ret = false
        // ???????????????
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
                            ret = true
                            break
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
                        ret = true
                        break
                    }
                    else -> break
                }
            }
        }

        if (!ret) {
            // ???????????????
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
                    ret = true
                    break
                }
            }
        }

        if (ret) {
            countUp()
            startTimer()
        }

        if (isComplete()) {
            saveRecord(true)
            _complete.value = true
            _canComplete.value = false
            stopTimer()
        } else if (isCanComplete()) {
            _canComplete.value = true
        }

        return ret
    }

    /**
     * ??????????????????.
     */
    fun openStock(): Boolean {
        stockIndex++
        return when {
            stockList.size > 0 && stockIndex < stockList.size -> {
                // ????????????
                if (stockIndex >= 0) {
                    stockList[stockIndex].side.value = SIDE.FRONT
                    _openCard.value = stockList[stockIndex]
                    countUp()
                    startTimer()
                }

                if (stockList.size - 1 == stockIndex) {
                    _closeCard.value = null
                } else {
                    _closeCard.value = backCard
                }
                true
            }

            stockList.size == 0 -> {
                _openCard.value = initialCard
                _closeCard.value = null
                false
            }

            stockIndex == stockList.size -> {
                stockIndex = -1
                _openCard.value = initialCard
                _closeCard.value = backCard
                true
            }

            else -> false
        }
    }

    private fun countUp() {
        _count.value = (_count.value ?: 0) + 1
    }

    /**
     * ?????????????????????????????????.
     */
    fun moveStock() {
        if (stockIndex >= 0 && stockList.size > stockIndex) {
            val data = SelectData(stockList[stockIndex], POSITION.STOCK, 0, stockIndex)
            move(data)
        }
    }

    /**
     * ?????????????????????.
     */
    fun moveFound(column: Int) = move(
        SelectData(
            listFound[column].value!!,
            POSITION.FOUNDATION,
            column
        )
    )

    /**
     * ????????????????????????????????????.
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
     * ????????????????????????????????????.
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
     * ??????????????????????????????????????????.
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
     * ???????????????.
     */
    @VisibleForTesting
    fun createStock(list: MutableList<TrumpCard>): MutableList<TrumpCard> =
        list.subList(0, STOCK_CARD_SIZE)

    /**
     * ???????????????.
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
     * ???????????????????????????.
     */
    private fun changeToFront(baseList: MutableList<TrumpCard>, index: Int) {
        if (baseList.size > 0) {
            baseList[index - 1].side.value = SIDE.FRONT
            baseList[index - 1].isLast.value = true
        }
    }

    /**
     * ???????????????.
     */
    fun onItemClick(item: TrumpCard) {
        getSelectData(item)?.let {
            move(it)
        }
    }

    /**
     * ???????????????.
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
     * Restart???????????????.
     */
    fun onRestartClick() {
        if (!isComplete()) {
            saveRecord(false)
        }
        initCard()
    }

    /**
     * ??????????????????.
     */
    private fun startTimer() = timer ?: run {
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    _time.postValue((_time.value ?: 0) + 1)
                }
            }, 1000, 1000)
        }
    }

    /**
     * ???????????????????????????.
     */
    fun restartTimer() {
        if (_time.value != 0L) {
            startTimer()
        }
    }

    /**
     * ??????????????????.
     */
    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    /**
     * ??????????????????.
     */
    private fun clearTimer() {
        stopTimer()
        _time.value = 0
    }

    /**
     * ????????????????????????????????????.
     * ???????????????????????????
     * - ??????????????????????????????52??????
     * - ?????????????????????King?????????
     */
    private fun isComplete(): Boolean {
        if (_count.value!! <= TOTAL_CARD_SIZE) return false

        listFound.forEach {
            it.value?.let { card ->
                if (card.number != NUMBER.KING) return false
            }
        }
        return true
    }

    private fun isCanComplete(): Boolean {
        if (_count.value!! <= 20) return false

        _listLayout.value!!.forEach { list ->
            list.forEach { card ->
                if (card.side.value == SIDE.BACK) return false
            }
        }
        return true
    }

    /**
     * ??????????????????(?????????).
     */
    fun startAutoCompleteAsync() {
        viewModelScope.launch {
            isChecked.value?.let {
                if (it) {
                    setPreference(
                        getApplication<Application>().applicationContext,
                        NO_MORE_CHECKBOX_KEY,
                        true
                    )
                }
            }
            startAutoCompleteSync()
        }
    }

    /**
     * ??????????????????(??????)
     */
    private suspend fun startAutoCompleteSync() {
        L.d(TAG, "startAutoComplete")

        // ???????????????????????????????????????????????????
        val nextCard = getNextCard(checkCardIndex)
        if (nextCard != null) {

            val number = nextCard.first
            val pattern = nextCard.second
            L.d(TAG, "startAutoComplete number = $number pattern = $pattern")

            val canMove = if (isExistOpen(number, pattern)) {
                L.d(TAG, "startAutoComplete -- [1] --")

                // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
                moveStock()
                delay(DELAY_TIME)
                true
            } else {
                val selectData = getExistLayout(number, pattern)
                if (selectData != null) {
                    L.d(TAG, "startAutoComplete -- [2] --")

                    // ???????????????????????????????????????????????????????????????
                    move(selectData)
                    delay(DELAY_TIME)
                    true
                } else {
                    L.d(TAG, "startAutoComplete -- [3] --")
                    false
                }
            }

            if (canMove) {
                L.d(TAG, "startAutoComplete -- [4] --")
                // ???????????????????????????????????????????????????????????????
                checkCardIndex = 0
            } else {
                L.d(TAG, "startAutoComplete -- [5] --")
                // ????????????????????????????????????????????????????????????????????????
                checkCardIndex++

                if (checkCardIndex >= 4) {
                    // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    L.d(TAG, "startAutoComplete -- [6] --")

                    checkCardIndex = 0
                    // ??????????????????????????????????????????
                    val aboveCard = getAboveCardLayout(number, pattern)
                    if (aboveCard != null) {
                        L.d(TAG, "startAutoComplete -- [7] --")

                        // ?????????????????????????????????????????????????????????
                        val ret = move(aboveCard)
                        if (ret) delay(DELAY_TIME)

                        if (!ret) {
                            // ??????????????????????????????????????????????????????????????????
                            L.d(TAG, "startAutoComplete -- [8] --")

                            // ????????????????????????????????????
                            val open = openStock()
                            if (open) delay(DELAY_TIME)
                        }

                    } else {
                        L.d(TAG, "startAutoComplete -- [9] --")

                        // ????????????????????????????????????
                        val open = openStock()
                        if (open) delay(DELAY_TIME)
                    }
                }
            }
            startAutoCompleteSync()
        } else {
            // KING????????????????????????????????????????????????
            checkCardIndex++
            if (checkCardIndex >= 4) {
                L.d(TAG, "startAutoComplete -- [10] --")
                checkCardIndex = 0

                // ????????????????????????????????????
                val open = openStock()
                if (open) delay(DELAY_TIME)
            } else {
                L.d(TAG, "startAutoComplete -- [11] --")
            }
            if (!isComplete()) {
                startAutoCompleteSync()
            }
        }
    }

    /**
     * ??????????????????????????????.
     *
     * @return ?????????????????????????????????
     */
    private fun getAboveCardLayout(number: NUMBER, pattern: PATTERN): SelectData? {
        for ((columnIndex, column) in _listLayout.value!!.withIndex()) {
            var next = false
            for ((index, card) in column.withIndex()) {
                if (next) {
                    return SelectData(card, POSITION.LAYOUT, columnIndex, index)
                }
                if (card.number == number && card.pattern == pattern) {
                    next = true
                }
            }
        }
        return null
    }

    /**
     * ???????????????????????????????????????????????????????????????.
     */
    private fun isExistOpen(number: NUMBER, pattern: PATTERN): Boolean {
        val open = _openCard.value!!
        return open.number == number && open.pattern == pattern
    }

    /**
     * ??????????????????????????????.
     */
    private fun getExistLayout(number: NUMBER, pattern: PATTERN): SelectData? {
        for ((index, column) in _listLayout.value!!.withIndex()) {
            if (column.size >= 1) {
                val last = column.last()
                if (last.number == number && last.pattern == pattern) {
                    return SelectData(last, POSITION.LAYOUT, index, column.size - 1)
                }
            }
        }
        return null
    }

    /**
     * ????????????????????????????????????????????????.
     */
    private fun getNextCard(checkCardIndex: Int): Pair<NUMBER, PATTERN>? {
        var ret: Pair<NUMBER, PATTERN>? = null
        when (checkCardIndex) {
            PATTERN.SPADE.ordinal -> {
                val number = _spadeFound.value!!.number
                if (number != NUMBER.KING) {
                    ret = Pair(NUMBER.getNumber(number.ordinal + 1), PATTERN.SPADE)
                }
            }
            PATTERN.HEART.ordinal -> {
                val number = _heartFound.value!!.number
                if (number != NUMBER.KING) {
                    ret = Pair(NUMBER.getNumber(number.ordinal + 1), PATTERN.HEART)
                }

            }
            PATTERN.CLOVER.ordinal -> {
                val number = _cloverFound.value!!.number
                if (number != NUMBER.KING) {
                    return Pair(NUMBER.getNumber(number.ordinal + 1), PATTERN.CLOVER)
                }
            }
            PATTERN.DIAMOND.ordinal -> {
                val number = _diamondFound.value!!.number
                if (number != NUMBER.KING) {
                    ret = Pair(NUMBER.getNumber(number.ordinal + 1), PATTERN.DIAMOND)
                }
            }
        }
        return ret
    }

    /**
     * DB???????????????.
     */
    @SuppressLint("SimpleDateFormat")
    private fun saveRecord(result: Boolean) {
        L.d(TAG, "#saveRecord count = ${_count.value!!} time = ${_time.value!!}")
        if (_count.value!! > 0) {
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            recordRepository.saveRecord(
                Record(
                    0,
                    result,
                    _count.value!!,
                    _time.value!!,
                    simpleDateFormat.format(Date())
                )
            )
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
        private const val DELAY_TIME = 500L
    }
}

