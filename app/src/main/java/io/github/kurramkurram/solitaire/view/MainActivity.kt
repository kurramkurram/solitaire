package io.github.kurramkurram.solitaire.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var current: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val solitaireFragment = SolitaireFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, SolitaireFragment(), SolitaireFragment.TAG).commit()

        navbar.setOnItemSelectedListener {
            val tag: String
            val fragment = when (it.itemId) {
                R.id.navigation_game -> {
                    tag = SolitaireFragment.TAG
                    solitaireFragment
                }
                R.id.navigation_record -> {
                    tag = RecordFragment.TAG
                    RecordFragment()
                }
                R.id.navigation_settings -> {
                    tag = SettingsFragment.TAG
                    SettingsFragment()
                }
                else -> return@setOnItemSelectedListener false
            }

            supportFragmentManager.beginTransaction().apply {
                current?.let { c -> hide(c) }

                val showFragment = supportFragmentManager.findFragmentByTag(tag)
                if (showFragment == null) {
                    current = fragment
                    add(R.id.container, fragment, tag)
                } else {
                    current = showFragment
                    show(showFragment)
                }
            }.commit()
            true
        }
    }
}