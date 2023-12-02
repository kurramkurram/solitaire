package io.github.kurramkurram.solitaire

import android.app.Application
import java.io.File

class SolitaireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
    }
}