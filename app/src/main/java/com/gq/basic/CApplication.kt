package com.gq.basic

import com.gq.basicm3.basis.BasicApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CApplication: BasicApplication() {
    override fun onCreate() {
        super.onCreate()
    }
}