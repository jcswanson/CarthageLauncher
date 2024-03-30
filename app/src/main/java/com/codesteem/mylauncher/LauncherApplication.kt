package com.codesteem.mylauncher

import android.app.Application
import androidx.room.Room
import com.codesteem.mylauncher.database.NotificationDatabase
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for the MyLauncher app. This class extends the Android Application class
 * and is used to initialize the Hilt dependency injection framework and set up the
 * NotificationDatabase singleton.
 */
@HiltAndroidApp
class LauncherApplication : Application() {

    /**

