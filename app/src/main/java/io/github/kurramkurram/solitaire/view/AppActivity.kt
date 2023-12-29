package io.github.kurramkurram.solitaire.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.activity_app.*

/**
 * アプリケーションプライバシーポリシー画面.
 */
class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        webview.loadUrl("file:///android_asset/app.html")
    }
}