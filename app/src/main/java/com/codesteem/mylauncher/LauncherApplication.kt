package com.codesteem.mylauncher

import android.app.Application
import com.codesteem.mylauncher.database.NotificationDatabase
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for the MyLauncher app. This class extends the Android Application class
 * and is used to initialize the Hilt dependency injection framework and set up the
 * NotificationDatabase singleton.
 */
@HiltAndroidApp
class LauncherApplication: Application() {

    /**
     * Lazy initialization of the NotificationDatabase singleton. This ensures that the database
     * instance is only created when it is first accessed.
     */
    val notificationDatabase: NotificationDatabase by lazy {
        NotificationDatabase.getDatabase(this) // 'this' refers to the application context
    }

    /**
     * Called when the application is starting, after the process has been created. We initialize
     * the ThemeSettings singleton here and refresh the theme based on the current context.
     */
    override fun onCreate() {
        super.onCreate()
        ThemeSettings.getInstance(this)?.refreshTheme(baseContext)
    }
}
