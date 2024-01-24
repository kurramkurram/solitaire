package io.github.kurramkurram.solitaire

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class SolitaireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
    }
}
