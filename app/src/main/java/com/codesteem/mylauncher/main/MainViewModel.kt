// Import necessary classes and libraries

@HiltViewModel
class MainViewModel @Inject constructor (
    application: Application, // Injected Application instance
    private val mainRepo: MainRepository // MainRepository instance for data operations
): AndroidViewModel(application) {

    // NotificationDao instance for database operations
    private val notificationDao: NotificationDao

    // MutableStateFlow to hold all NotificationEntities
    private val _allNotificationsState = MutableStateFlow<List<NotificationEntity>>(emptyList())
    // Read-only StateFlow for all NotificationEntities
    val allNotificationsState = _allNotificationsState.asStateFlow()

    // MutableStateFlow to hold the result of deleteNotifications()
    private val _deleteNotificationsState = MutableStateFlow<Resource<Int>>(Resource.Initial())
    // Read-only StateFlow for deleteNotifications() result
    val deleteNotificationsState = _deleteNotificationsState.asStateFlow()

    // MutableStateFlow to hold the result of askPerplexity()
    private val _askPerplexityState = MutableStateFlow<Resource<PerplexityResponse>>(Resource.Initial())
    // Read-only StateFlow for askPerplexity() result
    val askPerplexityState = _askPerplexityState.asStateFlow()

    // Initialization block
    init {
        // Get the database instance and initialize the NotificationDao
        val database = NotificationDatabase.getDatabase(application)
        notificationDao = database.notificationDao()

        // Launch a coroutine to collect the flow from mainRepo and update _askPerplexityState
        viewModelScope.launch {
            mainRepo.perplexityFlow.collectLatest { result ->
                // Log the result
                Log.e("viewModelString", result.toString())

                if (result != null) {
                    // Parse the JSON result into a PerplexityResponse object
                    val gson = Gson()
                    val json = gson.fromJson(result, PerplexityResponse::class.java)
                    _askPerplexityState.emit(Resource.Success(json))
                    Log.e("viewModel", json.toString())
                }
            }
        }
    }

    // Function to ask perplexity with a given content
    fun askPerplexity(content: String) = viewModelScope.launch {
        // Emit Loading state
        _askPerplexityState.emit(Resource.Loading())

        // Call askPerplexity() function from mainRepo with the given content
        mainRepo.askPerplexity(PerplexityRequest().copy(messages = listOf(Message(), Message("user", content))))
    }

    // Function to delete all notifications
    fun deleteAllNotifications() = viewModelScope.launch {
        // Delete all notifications from the database
        notificationDao.deleteAllNotifications()

        // Get all notifications from the database and update _allNotificationsState
        getNotifications()
    }

    // Function to delete a notification with a given id
    fun deleteNotification(notificationId: String) = viewModelScope.launch {
        // Delete the notification with the given id from the database
        notificationDao.deleteNotificationById(notificationId)

        // Emit a random integer as a success result for deleteNotifications()
        _deleteNotificationsState.emit(Resource.Success(Random.nextInt()))
    }

    // Function to get prioritized notifications
    fun getPrioritizedNotifications() = viewModelScope.launch {
        // Get prioritized notifications from the database and update _allNotificationsState
        _allNotificationsState.emit(notificationDao.getPrioritizedNotifications())
    }

    // Function to get all notifications
    fun getNotifications() = viewModelScope.launch {
        // Get all notifications from the database and update _allNotificationsState
        _allNotificationsState.emit(notificationDao.getAllNotifications())
    }
}
