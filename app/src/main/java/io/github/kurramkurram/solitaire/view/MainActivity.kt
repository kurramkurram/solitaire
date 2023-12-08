package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var current: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder().build()
                    )
                )
                .build()
        )
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
                    tag = BaseRecordFragment.TAG
                    BaseRecordFragment()
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

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}