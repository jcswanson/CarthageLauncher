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

abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
    }

    abstract fun getViewBinding(): VB

}