package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel

class SolitaireFragment : Fragment() {

    private val solitaireViewModel by viewModels<SolitaireViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}