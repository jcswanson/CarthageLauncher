/**
 * This activity displays a list of installed apps in three RecyclerViews.
 */
class DataActivity : AppCompatActivity(), Listener {

    private lateinit var tvEmptyListTop: TextView
    private lateinit var tvEmptyListMid: TextView
    private lateinit var tvEmptyListBottom: TextView
    private lateinit var rvTop: RecyclerView
    private lateinit var rvBottom: RecyclerView
    private lateinit var rvMid: RecyclerView
    private val installedAppsList = mutableListOf<InstalledApp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        initializeViews()
        setUpRecyclerViews()
        setEmptyListViewsVisibility(false)
        fetchInstalledApps()
    }

    private fun initializeViews() {
        tvEmptyListTop = findViewById(R.id.tvEmptyListTop)
        tvEmptyListBottom = findViewById(R.id.tvEmptyListBottom)
        tvEmptyListMid = findViewById(R.id.tvEmptyListMid)
        rvTop = findViewById(R.id.rvTop)
        rvBottom = findViewById(R.id.rvBottom)
        rvMid = findViewById(R.id.rvMid)
    }

    private fun setUpRecyclerViews() {
        setUpTopRecyclerView()
        setUpBottomRecyclerView()
        setUpMidRecyclerView()
    }

    private fun setUpTopRecyclerView() {
        rvTop.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(this@DataActivity, 4)
            adapter = MainAdapter(getTopApps(), this@DataActivity, applicationContext)
        }
    }

    private fun setUpBottomRecyclerView() {
        // ...
    }

    private fun setUpMidRecyclerView() {
        // ...
    }

    private fun fetchInstalledApps() {
        installedAppsList.clear()
        val packageManager = packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        apps.forEach { appInfo ->
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                installedAppsList.add(InstalledApp(appInfo, packageManager.getApplicationIcon(appInfo)))
            }
        }

        setEmptyListViewsVisibility(installedAppsList.isEmpty())
        updateRecyclerViews()
    }

    private fun getTopApps(): List<InstalledApp> {
        return installedAppsList.take(10)
    }

    private fun updateRecyclerViews() {
        rvTop.adapter?.notifyDataSetChanged()
        rvBottom.adapter?.notifyDataSetChanged()
        rvMid.adapter?.notifyDataSetChanged()
    }

    private fun setEmptyListViewsVisibility(visibility: Boolean) {
        tvEmptyListTop.visibility = if (visibility) View.VISIBLE else View.GONE
        tvEmptyListBottom.visibility = if (visibility) View.VISIBLE else View.GONE
        tvEmptyListMid.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun setEmptyListTop(visibility: Boolean) {
        setEmptyListViewsVisibility(visibility)
    }

    override fun setEmptyListBottom(visibility: Boolean) {
        setEmptyListViewsVisibility(visibility)
    }

    override fun setEmptyListMid(visibility: Boolean) {
        setEmptyListViewsVisibility(visibility)
    }
}

data class InstalledApp(val appInfo: ApplicationInfo, val appLogo: Drawable)

interface Listener {
    fun setEmptyListTop(visibility: Boolean)
    fun setEmptyListBottom(visibility: Boolean)
    fun setEmptyListMid(visibility: Boolean)
}
