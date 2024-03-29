package com.codesteem.mylauncher

import android.app.Application
import com.codesteem.mylauncher.database.NotificationDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LauncherApplication: Application() {
    val notificationDatabase: NotificationDatabase by lazy {
        NotificationDatabase.getDatabase(this)
    }
    override fun onCreate() {
        super.onCreate()
        ThemeSettings.getInstance(this)?.refreshTheme(baseContext)
    }

}