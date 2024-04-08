
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.codesteem.mylauncher.R
import com.codesteem.mylauncher.models.AppSearchModel

/**
 * Adapter for displaying a list of apps in a RecyclerView.
 * Allows the user to select an app, which will then be launched.
 */
class AppSearchAdapter(
    val activity: Activity,
    val searchBar: CardView,
    val editText: EditText
) : RecyclerView.Adapter<AppSearchAdapter.AppViewHolder>() {
    
    // List of apps to display in the RecyclerView
    private var apps: List<AppSearchModel> = emptyList()
    
    // Interface for listening to app selection events
    private var appSearchListener: AppSearchListener? = null
    
    /**
     * Sets the listener for app selection events.
     * @param listener The listener to set.
     */
    fun setAppSearchListener(listener: AppSearchListener) {
        appSearchListener = listener
    }
    
    /**
     * Creates a new AppViewHolder for the given view.
     * @param parent The ViewGroup in which the new View will be created.
     * @param viewType The view type of the new View.
     * @return A new AppViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_list_item, parent, false)
        return AppViewHolder(itemView)
    }
    
    /**
     * Sets the list of apps to display.
     * @param apps The list of apps to display.
     */
    fun setApps(apps: List<AppSearchModel>) {
        this.apps = apps
        notifyDataSetChanged()
    }
    
    /**
     * Binds the given app to the given AppViewHolder.
     * @param holder The AppViewHolder to bind the app to.
     * @param position The position of the app in the list.
     */
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        if (apps.isNotEmpty()){
            holder.bind(apps[position], activity)
        }
    }
    
    /**
     * Returns the size of the list of apps.
     * @return The size of the list of apps.
     */
    override fun getItemCount(): Int {
        return apps?.size ?: 0
    }
    
    /**
     * ViewHolder for an app in the RecyclerView.
     */
    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        private val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)

        /**
         * Binds the given app to this AppViewHolder.
         * @param app The app to bind.
         * @param activity The Activity to use for launching the app.
         */
        fun bind(appSearchModel: AppSearchModel, activity: Activity) {
            appNameTextView.text = appSearchModel.title
            appIconImageView.setImageDrawable(appSearchModel.userImage)
            itemView.setOnClickListener {
                // Hide the search bar and edit text when an app is selected
                searchBar.visibility = View.GONE
                editText.setText("")
                launchApp(activity, appSearchModel.packageName!!)
                appSearchListener?.onAppSelected()
            }
        }
    }
    
    /**
     * Launches the given app.
     * @param context The context to use for launching the app.
     * @param packageName The package name of the app to launch.
     */
    private fun launchApp(context: Context, packageName: String) {
        // Clear the edit text
        editText.setText("")
        // Get the launch intent for the given package name
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        // If the intent is not null, launch the app
        intent?.let {
            context.startActivity(it)
        } ?: run {
            // If the intent is null
