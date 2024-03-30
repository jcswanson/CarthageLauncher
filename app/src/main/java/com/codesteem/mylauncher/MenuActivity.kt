package com.codesteem.mylauncher

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codesteem.mylauncher.adapter.AppSearchAdapter
import com.codesteem.mylauncher.databinding.ActivityMenuBinding
import com.codesteem.mylauncher.databinding.DialogLayoutBinding
import com.codesteem.mylauncher.databinding.NotificationLayoutBinding
import com.codesteem.mylauncher.gesture.DefaultItemClickListener
import com.codesteem.mylauncher.gesture.GestureAdapter
import com.codesteem.mylauncher.gesture.GestureManager
import com.codesteem.mylauncher.gesture.RecyclerItemTouchListener
import com.codesteem.mylauncher.main.MainViewModel
import com.codesteem.mylauncher.models.AppInfo
import com.codesteem.mylauncher.models.AppSearchModel
import com.codesteem.mylauncher.models.NotificationsModel
import com.codesteem.mylauncher.test.Month
import com.codesteem.mylauncher.test.MonthHeader
import com.codesteem.mylauncher.test.MonthItem
import com.codesteem.mylauncher.test.MonthsAdapter
import com.codesteem.mylauncher.util.MySharedPreferences
import com.codesteem.mylauncher.util.MySharedPreferences.getPrioritized
import com.codesteem.mylauncher.util.MySharedPreferences.getStringList
import com.codesteem.mylauncher.util.MySharedPreferences.getStringListAll
import com.codesteem.mylauncher.util.MySharedPreferences.isAppsBallonShown
import com.codesteem.mylauncher.util.MySharedPreferences.isPrioritized
import com.codesteem.mylauncher.util.MySharedPreferences.modifyPair
import com.codesteem.mylauncher.util.MySharedPreferences.saveAppsShownBallon
import com.codesteem.mylauncher.util.NotificationListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.showAlignBottom
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MenuActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener{
    // Class variable declarations

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialization of variables and views

        // Registering receivers

        // Setting up the barcode scanner

        // Setting up the apps RecyclerView

        // Setting up the search functionality

        // Setting up the QR code scanner

        // Setting up the gesture manager

        // Setting up the notification listener

