package com.lauwba.bmtbima.utils

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs

class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        //instace untuk EasyPrefs nya
        Prefs.Builder()
            .setContext(baseContext)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}