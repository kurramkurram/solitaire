package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel

class SolitaireFragment : Fragment() {

    private val solitaireViewModel by viewModels<SolitaireViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_solitaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.d(TAG, "#onViewCreated")
    }

    companion object {
        private const val TAG = "SolitaireFragment"
    }
}