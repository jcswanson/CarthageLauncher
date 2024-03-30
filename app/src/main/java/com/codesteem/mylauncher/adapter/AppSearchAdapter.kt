
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
 * Adapter for displaying apps in the app search dialog.
 *
 * @property activity The activity context.
 * @property searchBar The CardView containing the search bar.
 * @property editText The EditText for entering search queries.
 */
class AppSearchAdapter(
    val activity: Activity,
    val searchBar: CardView,
    val editText: EditText
) : RecyclerView.Adapter<AppSearchAdapter.AppViewHolder>() {

    // List of apps to display in the RecyclerView.
    private var apps: List<AppSearchModel> = emptyList()

    // Listener for app selection events.
    private var appSearchListener: AppSearchListener? = null

    /**
     * Sets the listener for app selection events.
     *
     * @param listener The listener to set.
     */
    fun setAppSearchListener(listener: AppSearchListener) {
        appSearchListener = listener
    }

    /**
     * Creates a new AppViewHolder instance for a given item view.
     *
     * @param parent The ViewGroup in which the new View will be created.
     * @param viewType The view type of the new View.
     * @return A new AppViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_list_item, parent, false)
        return AppViewHolder(itemView)
    }

    /**
     * Sets the list of apps to display and notifies the adapter of the data set change.
     *
     * @param apps The list of apps to display.
     */
    fun setApps(apps: List<AppSearchModel>) {
        this.apps = apps
        notifyDataSetChanged()
    }

    /**
     * Binds the given app to the AppViewHolder at the specified position.
     *
     * @param holder The AppViewHolder to bind the app to.
     * @param position The position of the app in the apps list.
     */
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        if (apps.isNotEmpty()) {
            holder.bind(apps[position], activity)
        }
    }

    /**
     * Returns the size of the apps list.
     *
     * @return The size of the apps list.
     */
    override fun getItemCount(): Int {
        return apps.size
    }

    /**
     * Inner class representing a single app view holder.
     *
     * @property itemView The root view of the app item.
     * @property appNameTextView The TextView displaying the app name.
     * @property appIconImageView The ImageView displaying the app icon.
     */
    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        private val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)

        /**
         * Binds the given app to the AppViewHolder.
         *
         * @param appSearchModel The app to bind.
         * @param activity The activity context.
         */
        fun bind(appSearchModel: AppSearchModel, activity: Activity) {
            appNameTextView.text = appSearchModel.title
            appIconImageView.setImageDrawable(appSearchModel.userImage)
            itemView.setOnClickListener {
                // Hide the search bar when an app is clicked.
                searchBar.visibility = View.GONE
                // Launch the selected app.
                launchApp(activity, appSearchModel.packageName!!)
                // Notify the listener that an app has been selected.
                appSearchListener?.onAppSelected()
            }
        }
    }

    /**
     * Launches the app with the given package name.
     *
     * @param context The context to launch the app from.
     * @param packageName The package name of the app to
