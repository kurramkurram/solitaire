package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.kurramkurram.solitaire.databinding.ActivityAppBinding

/**
 * アプリケーションプライバシーポリシー画面.
 */
class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webview.loadUrl("file:///android_asset/app.html")
    }
}
