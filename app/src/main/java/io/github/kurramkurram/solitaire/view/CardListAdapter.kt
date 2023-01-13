package io.github.kurramkurram.solitaire.view

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.SIDE

class CardListAdapter(context: Context, list: List<TrumpCard>) :
    ArrayAdapter<TrumpCard>(context, 0, list) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        val data = getItem(position)
        val text = "${data!!.number.ordinal} \n ${data.pattern.name[0]}"
        view!!.findViewById<TextView>(android.R.id.text1).apply {
            this.text = text
            val backgroundColor: Int
            val textColor: Int
            when (data.side) {
                SIDE.FRONT -> {
                    backgroundColor = if (data.pattern.ordinal % 2 != 0) {
                        android.R.color.holo_red_light
                    } else {
                        android.R.color.white
                    }
                    textColor = android.R.color.black
                }
                SIDE.BACK -> {
                    backgroundColor = android.R.color.darker_gray
                    textColor = android.R.color.darker_gray
                }
            }

            parent as ListView
            val height = if (parent.count - 1 == position) {
                200
            } else {
                50
            }
            val param = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height)
            view.layoutParams = param

            setBackgroundColor(resources.getColor(backgroundColor, null))
            setTextColor(resources.getColor(textColor, null))
        }

        return view
    }

    companion object {
        private const val TAG = "CardListAdapter"
    }
}