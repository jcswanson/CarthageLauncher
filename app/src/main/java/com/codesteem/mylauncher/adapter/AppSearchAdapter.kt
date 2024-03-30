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

interface AppSearchListener {
    fun onAppSelected()
}

class AppSearchAdapter(
    private val activity: Activity,
    private val searchBar: CardView,
    private val editText: EditText,
    private val appSearchListener: AppSearchListener
) : RecyclerView.Adapter<AppSearchAdapter.AppViewHolder>() {

    private var apps: List<AppSearchModel> = emptyList()

    fun setApps(apps: List<AppSearchModel>) {
        this.apps = apps
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_list_item, parent, false)
        return AppViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        if (apps.isNotEmpty()) {
            holder.bind(apps[position])
        }
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        private val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)

        fun bind(appSearchModel: AppSearchModel) {
            appNameTextView.text = appSearchModel.title
            appIconImageView.setImageDrawable(appSearchModel.userImage)
            itemView.setOnClickListener {
                searchBar.visibility = View.GONE
                launchApp(activity, appSearchModel.packageName!!)
                appSearchListener.onAppSelected()
            }
        }
    }

    private fun launchApp(context: Context, packageName: String) {
        context.packageManager.getLaunchIntentForPackage(packageName)?.also { intent ->
            context.startActivity(intent)
        }
    }
}
