// ViewModel for the main activity
@HiltViewModel
class MainViewModel @Inject constructor (
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    private val notificationDao: NotificationDao
    private val gson = Gson()

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
        val database = NotificationDatabase.getDatabase(application)
        notificationDao = database.notificationDao()

        viewModelScope.launch {
            mainRepository.perplexityFlow.collectLatest { result ->
                if (result != null) {
                    val json = gson.fromJson(result, PerplexityResponse::class.java)
                    _askPerplexityState.emit(Resource.Success(json))
                }
            }
        }
    }

    fun askPerplexity(content: String) = viewModelScope.launch {
        _askPerplexityState.emit(Resource.Loading())
        mainRepository.askPerplexity(PerplexityRequest(listOf(Message(), Message("user", content))))
    }

    fun deleteAllNotifications() = viewModelScope.launch {
        mainRepository.deleteAllNotifications()
        getNotifications()
    }

    fun deleteNotification(notificationId: String) = viewModelScope.launch {
        mainRepository.deleteNotificationById(notificationId)
        _deleteNotificationsState.emit(Resource.Success(Random.nextInt()))
    }

    fun getPrioritizedNotifications() = viewModelScope.launch {
        _allNotificationsState.emit(notificationDao.getPrioritizedNotifications())
    }

    fun getNotifications() = viewModelScope.launch {
        _allNotificationsState.emit(notificationDao.getAllNotifications())
    }
}
