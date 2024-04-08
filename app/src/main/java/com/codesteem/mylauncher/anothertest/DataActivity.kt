/**
 * This activity displays a list of installed apps in three RecyclerViews.
 */
class DataActivity : AppCompatActivity(), Listener {

    private lateinit var tvEmptyListTop: TextView // TextView for displaying an empty list message at the top RecyclerView
    private lateinit var tvEmptyListMid: TextView // TextView for displaying an empty list message at the mid RecyclerView
    private lateinit var tvEmptyListBottom: TextView // TextView for displaying an empty list message at the bottom RecyclerView
    private lateinit var rvTop: RecyclerView // RecyclerView for displaying the top row of installed apps
    private lateinit var rvBottom: RecyclerView // RecyclerView for displaying the bottom row of installed apps
    private lateinit var rvMid: RecyclerView // RecyclerView for displaying the middle row of installed apps
    private var installedAppsList: List<Drawable> = mutableListOf<Drawable>() // List of installed apps' icons

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        // Initializing RecyclerViews and TextViews
        rvTop = findViewById<RecyclerView>(R.id.rvTop)
        rvBottom = findViewById<RecyclerView>(R.id.rvBottom)
        rvMid = findViewById<RecyclerView>(R.id.rvMid)
        tvEmptyListTop = findViewById<TextView>(R.id.tvEmptyListTop)
        tvEmptyListBottom = findViewById<TextView>(R.id.tvEmptyListBottom)
        tvEmptyListMid = findViewById<TextView>(R.id.tvEmptyListMid)

        // Setting up the top RecyclerView
        setTopRecyclerView()

        // Setting up the bottom RecyclerView
        setBottomRecyclerView()

        // Setting up the mid RecyclerView
        setMidRecyclerView()

        // Hiding empty list messages initially
        tvEmptyListTop.setVisibility(View.GONE)
        tvEmptyListBottom.setVisibility(View.GONE)
        tvEmptyListMid.setVisibility(View.GONE)
    }

    private fun setTopRecyclerView() {
        // Making RecyclerView layout changes
        rvTop.setHasFixedSize(true)
        rvTop.isNestedScrollingEnabled = false

        // Setting GridLayoutManager with 4 columns for the top RecyclerView
        rvTop.layoutManager = GridLayoutManager(this, 4)

        // Fetching the list of all installed apps
        installedAppsList = getAllInstalledApps()

        // Creating a list for the top RecyclerView
        val topList: MutableList<Drawable> = ArrayList()

        // Adding installed apps' icons to the topList
        topList.addAll(installedAppsList)

        // Creating an adapter for the top RecyclerView
        val topListAdapter = MainAdapter(topList, this, applicationContext)

        // Setting the adapter for the top RecyclerView
        rvTop.adapter = topListAdapter

        // Setting OnDragListener for the topListAdapter
//        tvEmptyListTop.setOnDragListener(topListAdapter.dragInstance)
//        rvTop.setOnDragListener(topListAdapter.dragInstance)
    }

    // Similarly commented methods for setting up the bottom and mid RecyclerViews
    private fun setBottomRecyclerView() {
        // ...
    }

    private fun setMidRecyclerView() {
        // ...
    }

    // Method for fetching the list of all installed apps
    private fun getAllInstalledApps(): List<Drawable> {
        val packageManager = packageManager
        val installedApps = mutableListOf<Drawable>()

        // Fetching the list of all installed apps
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        // Iterating through the installed apps
        for (appInfo in apps) {
            // Checking if the app has a launch intent
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                // Adding the app's icon to the installedApps list
                val appLogo = packageManager.getApplicationIcon(appInfo)
                installedApps.add(appLogo)
            }
        }

        return installedApps
    }

    // Overridden methods for setting empty list messages visibility
    override fun setEmptyListTop(visibility: Boolean) {
        tvEmptyListTop.visibility = if (visibility) View.VISIBLE else View.GONE
        rvTop.visibility = if (visibility) View.GONE else View.VISIBLE
    }

    override fun setEmptyListBottom(visibility: Boolean) {
        tvEmptyListBottom.visibility = if (visibility) View.VISIBLE else View.GONE
        rvBottom.visibility = if (visibility) View.GONE else View.VISIBLE
    }

    override fun setEmptyListMid(visibility: Boolean) {
        tvEmptyListMid.visibility = if (visibility) View.VISIBLE else View.GONE
        rvMid.visibility = if (visibility) View.GONE else View.VISIBLE
    }
}
