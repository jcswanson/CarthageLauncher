import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor

class BugReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBugReportBinding
    private lateinit var viewModel: BugReportViewModel
    private lateinit var loadingDialog: Dialog
    private var mediaItems: MutableList<Uri>? = null
    private var compressedVideos: MutableList<Uri> = mutableListOf()
    private var player: SimpleExoPlayer? = null
    private var playerView: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return BugReportViewModel() as T
            }
        }).get(BugReportViewModel::class.java)

        loadingDialog = LoadingDialog(this)

        binding.back.setOnClickListener { finish() }
        binding.attachBtn.setOnClickListener { pickMultipleMediaItems.launch(5) }
        binding.sendBtn.setOnClickListener {
            if (mediaItems.isNullOrEmpty()) {
                submit()
            } else {
                showConfirmDialog()
            }
        }

        viewModel.uploadingStatus.observe(this, Observer {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        })

        viewModel.loadingPercent.observe(this, Observer {
            loadingDialog.setMessage("Loading $it%")
        })

        registerReceiver(notificationReceiver, IntentFilter("ACTION_NOTIFICATION_RECEIVED"))
        registerReceiver(screenStateChangedReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableFullScreenMode()
        }
        hideSystemUI()

        mediaItems = mutableListOf()
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle notification
        }
    }

    private val screenStateChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle screen state change
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
        unregisterReceiver(screenStateChangedReceiver)
        player?.release()
    }

    private fun showNotificationAlert(
        activity: Activity,
        title: String?,
        content: String?,
        icon: ByteArray?,
        isReply: Boolean,
        actions: Array<Notification.Action>,
        msgId: String?
    ) {
        // Show notification alert
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        // Convert byte array to bitmap
        return null
    }

    private fun listenToUploading() {
        // Listen for uploading status changes
    }

    private fun submit() {
        // Submit bug report
    }

    private fun showConfirmDialog() {
        // Show confirmation dialog
    }

    private val pickMultipleMediaItems =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) {
            // Handle picked media items
            mediaItems = it.toMutableList()
        }

    private fun queryName(resolver: ContentResolver, uri: Uri): String {
        // Query display name of file
        return ""
    }

    private fun videoCompressor(list: List<Uri>) {
        // Compress videos
    }

    private fun isVideo(uri: Uri): Boolean {
        // Check if URI points to a video file
        return false
    }

    private fun isImage(uri: Uri): Boolean {
        // Check if URI points to an image file
        return false
    }

    private fun hideSystemUI() {
        // Hide system UI
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun enableFullScreenMode() {
        // Enable fullscreen mode
    }

    fun playMedia(uri: Uri) {
        if (player == null) {
            player = SimpleExoPlayer.Builder(this).build()
            playerView = PlayerView(this)
            playerView?.player = player
            binding.mediaContainer.addView(playerView, ConstraintLayout.LayoutParams(
                ConstraintLayout
