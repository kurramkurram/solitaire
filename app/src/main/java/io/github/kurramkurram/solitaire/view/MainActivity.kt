package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.github.kurramkurram.solitaire.BuildConfig
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.ActivityMainBinding
import io.github.kurramkurram.solitaire.viewmodel.MainViewModel

/**
 * ベースのActivity.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var current: Fragment? = null
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private lateinit var binding: ActivityMainBinding

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volumeControlStream = AudioManager.STREAM_MUSIC

        val solitaireFragment = SolitaireFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, SolitaireFragment(), SolitaireFragment.TAG).commit()

        binding.navbar.setOnItemSelectedListener {
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
                R.id.navigation_movie -> {
                    tag = PlayMovieFragment.TAG
                    PlayMovieFragment()
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

        if (!BuildConfig.IS_PRO) {
            binding.adView.apply {
                visibility = View.VISIBLE
                val adRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.startMusic()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.releaseMusic()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
