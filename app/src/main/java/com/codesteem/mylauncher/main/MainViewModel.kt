package com.codesteem.mylauncher.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.codesteem.mylauncher.Resource
import com.codesteem.mylauncher.data.remote.Message
import com.codesteem.mylauncher.data.remote.PerplexityRequest
import com.codesteem.mylauncher.data.remote.models.PerplexityResponse
import com.codesteem.mylauncher.database.NotificationDao
import com.codesteem.mylauncher.database.NotificationDatabase
import com.codesteem.mylauncher.database.NotificationEntity
import com.codesteem.mylauncher.repo.MainRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.sse.EventSourceListener
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor (
    application: Application,
    private val mainRepo: MainRepository
): AndroidViewModel(application) {
    private val notificationDao: NotificationDao

    private val _allNotificationsState = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val allNotificationsState = _allNotificationsState.asStateFlow()

    private val _deleteNotificationsState = MutableStateFlow<Resource<Int>>(Resource.Initial())
    val deleteNotificationsState = _deleteNotificationsState.asStateFlow()

    private val _askPerplexityState = MutableStateFlow<Resource<PerplexityResponse>>(Resource.Initial())
    val askPerplexityState = _askPerplexityState.asStateFlow()

    init {
        val database = NotificationDatabase.getDatabase(application)
        notificationDao = database.notificationDao()
        viewModelScope.launch {
            mainRepo.perplexityFlow.collect { result ->
                Log.e("viewModelString", result.toString())
                if (result != null) {
                    val gson = Gson()
                    val json = gson.fromJson(result, PerplexityResponse::class.java)
                    _askPerplexityState.emit(Resource.Success(json))
                    Log.e("viewModel", json.toString())
                }
            }
        }
    }

    fun askPerplexity(content: String) = viewModelScope.launch {
        _askPerplexityState.emit(Resource.Loading())
        mainRepo.askPerplexity(PerplexityRequest().copy(messages = listOf(Message(), Message("user", content))))

    }

    fun deleteAllNotifications() = viewModelScope.launch {
        notificationDao.deleteAllNotifications()
        getNotifications()
    }

    fun deleteNotification(notificationId: String) = viewModelScope.launch {
        notificationDao.deleteNotificationById(notificationId)
        _deleteNotificationsState.emit(Resource.Success(Random.nextInt()))
    }

    fun getPrioritizedNotifications() = viewModelScope.launch {
        _allNotificationsState.emit(notificationDao.getPrioritizedNotifications())
    }

    fun getNotifications() = viewModelScope.launch {
        _allNotificationsState.emit(notificationDao.getAllNotifications())
    }
}