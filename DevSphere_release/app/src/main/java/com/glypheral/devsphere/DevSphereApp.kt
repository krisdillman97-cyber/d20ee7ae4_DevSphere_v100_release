package com.glypheral.devsphere

import android.app.Application
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.ThemeManager

class DevSphereApp : Application() {
    companion object {
        lateinit var instance: DevSphereApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ThemeManager.applyTheme(this)
        SessionManager.init(this)
    }
}
