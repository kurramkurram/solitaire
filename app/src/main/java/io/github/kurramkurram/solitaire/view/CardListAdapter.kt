package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.github.kurramkurram.solitaire.data.TrumpCard

class CardListAdapter(context: Context, list: List<TrumpCard>) :
    ArrayAdapter<TrumpCard>(context, 0, list) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        val data = getItem(position)
        val text = "${data!!.number.ordinal}_${data.pattern.name[0]}_${data.side.name[0]}"
        view!!.findViewById<TextView>(android.R.id.text1).text = text

        return view
    }
}