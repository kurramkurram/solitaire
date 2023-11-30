package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.NUMBER
import io.github.kurramkurram.solitaire.util.PATTERN
import io.github.kurramkurram.solitaire.util.SIDE
import org.junit.Test
import org.mockito.Mockito

class SolitaireViewModelTest {

    @Test
    fun testCreateLayout() {
        val initList = mutableListOf<TrumpCard>()
        PATTERN.values().forEach { pattern ->
            NUMBER.values().forEach { number ->
                initList.add(TrumpCard(number, pattern, MutableLiveData(SIDE.BACK)))
            }
        }

        val solitaireViewModel = SolitaireViewModel(Mockito.mock(Application::class.java))
        val list = solitaireViewModel.createLayout(initList)
        L.d(TAG, "#testCreateLayout = $list")
    }


    companion object {
        private const val TAG = "SolitaireViewModelTest"
    }
}