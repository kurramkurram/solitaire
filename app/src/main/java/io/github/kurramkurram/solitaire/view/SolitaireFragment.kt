package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.POSITION
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.android.synthetic.main.fragment_solitaire.*

class SolitaireFragment : Fragment(), OnItemClickListener, OnClickListener {

    private val solitaireViewModel by viewModels<SolitaireViewModel>()

    private lateinit var layoutList: MutableList<ListView>
    private lateinit var foundLayoutList: MutableList<TextView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        solitaireViewModel.initCard()
        return inflater.inflate(R.layout.fragment_solitaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.d(TAG, "#onViewCreated")

        // found
        foundLayoutList = mutableListOf(
            found0,
            found1,
            found2,
            found3
        )
        updateFound()
        for (v in foundLayoutList) {
            v.setOnClickListener(this@SolitaireFragment)
        }

        // stock
        stock_back.apply {
            setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
            setOnClickListener(this@SolitaireFragment)
        }

        stock_front.apply {
            setOnClickListener(this@SolitaireFragment)
        }

        // layout
        layoutList = mutableListOf(
            listView0,
            listView1,
            listView2,
            listView3,
            listView4,
            listView5,
            listView6
        )

        for ((index, list) in layoutList.withIndex()) {
            list.apply {
                adapter = solitaireViewModel.getAdapter(requireContext())[index]
                onItemClickListener = this@SolitaireFragment
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        when (val listView = parent as ListView) {
            listView0, listView1, listView2, listView3, listView4, listView5, listView6 -> {
                val card = listView.getItemAtPosition(position) as TrumpCard
                val ret = solitaireViewModel.move(
                    SolitaireViewModel.SelectData(
                        card,
                        POSITION.LAYOUT,
                        layoutList.indexOf(listView),
                        position
                    )
                )
                L.d(TAG, "#onItemClick view = ${listView.id} ret = $ret")
                if (ret) {
                    updateFound()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            stock_back -> {
                solitaireViewModel.openStock()
                updateStock()
            }
            stock_front -> {
                solitaireViewModel.moveStock()
                updateStock()
                updateFound()
            }

            found0, found1, found2, found3 -> {
                val ret = solitaireViewModel.moveFound(foundLayoutList.indexOf(v))
                if (ret) {
                    updateFound()
                }
            }
        }
    }

    private fun updateStock() {
        val index = solitaireViewModel.stockIndex
        if (index >= 0) {
            val card = solitaireViewModel.stockList[index]
            val name = dataToDummyString(card)
            stock_front.apply {
                text = name
                val backgroundColor = if (card.pattern.ordinal % 2 != 0) {
                    android.R.color.holo_red_light
                } else {
                    android.R.color.white
                }
                setBackgroundColor(resources.getColor(backgroundColor, null))
            }
        } else {
            stock_front.apply {
                text = "empty"
                setBackgroundColor(resources.getColor(android.R.color.white, null))
            }
        }
    }

    private fun updateFound() {
        for ((index, found) in foundLayoutList.withIndex()) {
            found.apply {
                val last = solitaireViewModel.foundList[index]
                if (last.isNotEmpty()) {
                    val card = last[last.size - 1]
                    val name = dataToDummyString(card)
                    L.d(TAG, "#updateFound name = $name")
                    text = name
                }
            }
        }
    }

    private fun dataToDummyString(data: TrumpCard): String {
        return data.number.ordinal.toString() + data.pattern.name[0]
    }

    companion object {
        private const val TAG = "SolitaireFragment"
    }
}