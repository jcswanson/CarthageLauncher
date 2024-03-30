// AppsAdapter class holds the list of apps and handles the display and selection of apps
class AppsAdapter(
    private val context: Context, // Context for accessing system services
    private val appList: List<String>, // List of app package names
    private val selectionChangeListener: SelectionChangeListener // Listener for selection changes
) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    // appCounters map stores the counter for each app
    private val appCounters = mutableMapOf<String, Int>()

    // selectedApps set stores the currently selected apps
    private var isSelectionMode = false
    private var selectedApps = mutableSetOf<String>()

    // Initialize appCounters with initial values
    init {
        appList.forEach { appCounters[it] = 0 }
    }

    // onCreateViewHolder method creates a ViewHolder for a new item view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item_app layout and return a new ViewHolder instance
        // ...
    }

    // onBindViewHolder method binds the data for a specific position in the RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageName = appList[position]

        // Load the app logo using Glide
        Glide.with(context).load(appLogo).into(holder.appLogoImageView)

        // Update the counter TextView visibility and text
        val countR = appCounters[packageName]
        holder.tvCounter.visibility = if (countR != null && countR != 0) View.VISIBLE else View.GONE
        holder.tvCounter.text = countR?.toString() ?: "0"

        // Set the checkBox visibility based on the selection mode
        holder.checkBox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE

        // Set the checkBox checked state based on the selectedApps set
        holder.checkBox.isChecked = selectedApps.contains(packageName)

        // Set onLongClickListener for entering selection mode and toggling selection
        holder.itemView.setOnLongClickListener {
            isSelectionMode = true
            notifyDataSetChanged()
            true
        }

        // Set onClickListener for launching the app or toggling selection
        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(packageName, holder.checkBox)
            } else {
                launchApp(packageName)
            }
        }

        // Set onCheckedChangeListener for toggling selection
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedApps.add(packageName)
            } else {
                selectedApps.remove(packageName)
            }
            selectionChangeListener.onSelectionChanged(selectedApps)
        }
    }

    // toggleSelection method toggles the selection state of an app
    private fun toggleSelection(packageName: String, checkBox: CheckBox) {
        if (selectedApps.contains(packageName)) {
            selectedApps.remove(packageName)
            checkBox.isChecked = false
        } else {
            selectedApps.add(packageName)
            checkBox.isChecked = true
        }
        selectionChangeListener.onSelectionChanged(selectedApps)
    }

    // getSelectedApps method returns the list of currently selected apps
    fun getSelectedApps(): List<String> {
        return selectedApps.toList()
    }

    // getItemViewType method returns the view type for the current position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    // exitSelectionMode method exits the selection mode and returns the list of selected apps
    fun exitSelectionMode(): List<String> {
        isSelectionMode = false
        notifyDataSetChanged()
        return selectedApps.toList()
    }

    // launchApp method launches the app with the given packageName
    private fun launchApp(packageName: String) {
        // ...
    }

    // ViewHolder class holds the views for an item in the RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ...
    }
}
