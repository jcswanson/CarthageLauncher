// ViewModel for the main activity
@HiltViewModel
class MainViewModel @Inject constructor (
    application: Application, // Application instance for getting the database instance
    private val mainRepo: MainRepository // Repository for handling data operations
): AndroidViewModel(application) {

    // Notification DAO for database operations
    private val notificationDao: NotificationDao

    // State flow for storing all notifications
    private val _allNotificationsState = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val allNotificationsState = _allNotificationsState.asStateFlow()

    // State flow for storing the result of delete notifications operation
    private val _deleteNotificationsState = MutableStateFlow<Resource<Int>>(Resource.Initial())
    val deleteNotificationsState = _deleteNotificationsState.asStateFlow()

    // State flow for storing the result of asking perplexity operation
    private val _askPerplexityState = MutableStateFlow<Resource<PerplexityResponse>>(Resource.Initial())
    val askPerplexityState = _askPerplexityState.asStateFlow()

    init {
        // Get the database instance and initialize the notification DAO
        val database = NotificationDatabase.getDatabase(application)
        notificationDao = database.notificationDao()

        // Launch a coroutine to collect the perplexity flow from the repository
        viewModelScope.launch {
            mainRepo.perplexityFlow.collectLatest { result ->
                Log.e("viewModelString", result.toString())
                if (result != null) {
                    // Parse the JSON result and emit the success state
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
        _askPerplexityState.emit(Resource.Loading()) // Emit loading state before making the API call
        mainRepo.askPerplexity(PerplexityRequest().copy(messages = listOf(Message(), Message("user", content)))) // Make the API call
    }

    // Function to delete all notifications
    fun deleteAllNotifications() = viewModelScope.launch {
        notificationDao.deleteAllNotifications() // Delete all notifications from the database
        getNotifications() // Fetch all notifications after deletion
    }

    // Function to delete a notification with a given id
    fun deleteNotification(notificationId: String) = viewModelScope.launch {
        notificationDao.deleteNotificationById(notificationId) // Delete the notification with the given id
        _deleteNotificationsState.emit(Resource.Success(Random.nextInt())) // Emit success state with a random integer
    }

    // Function to get prioritized notifications
    fun getPrioritizedNotifications() = viewModelScope.launch {
        _allNotificationsState.emit(notificationDao.getPrioritizedNotifications()) // Emit the prioritized notifications
    }

    // Function to get all notifications
    fun getNotifications() = viewModelScope.launch {
        _allNotificationsState.emit(notificationDao.getAllNotifications()) // Emit all notifications from the database
    }
}
