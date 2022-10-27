package com.gq.basicm3.basis

import android.app.Application
import com.gq.basicm3.AppContext

open class BasicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialization(this)
    }
}