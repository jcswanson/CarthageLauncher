package com.codesteem.mylauncher

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.util.Base64
import android.view.LayoutInflater
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.viewbinding.ViewBinding
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.util.NotificationListener
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

// Define an abstract base class for activities with ViewBinding support
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    // Lateinit property for ViewBinding instance
    protected lateinit var binding: VB

    // Called when the activity is starting.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewBinding instance
        binding = getViewBinding()

        // Set the content view using the inflated layout
        setContentView(binding.root)
    }

    // Abstract method to get the ViewBinding instance
    abstract fun getViewBinding(): VB
}
