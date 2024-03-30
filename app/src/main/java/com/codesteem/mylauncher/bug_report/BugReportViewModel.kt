package com.codesteem.mylauncher.bug_report

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesteem.mylauncher.Resource
import com.codesteem.mylauncher.util.Constants.BUGS
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing bug reports.
 */
class BugReportViewModel: ViewModel() {
    // FirebaseFirestore instance for accessing the Firestore database.
    val db = FirebaseFirestore.getInstance()
    
    // Firebase storage instance for uploading media files.
    val storage = Firebase.storage
    
    // MutableStateFlow to hold the uploading status of media files.
    val uploadingStatus = MutableStateFlow<Resource<Boolean>>(Resource.Initial())

    // MutableLiveData to hold the bug ID.
    private val _bugId = MutableLiveData<Int>()

    // ArrayList to hold the URIs of bug media files.
    var bugMedia = ArrayList<Uri>()

    /**
     * Initialization block that gets the biggest ID in the database.
     */
    init {
        getBiggestIdInTheDb()
    }

    /**
     * Function to upload a media file to Firebase storage and update the corresponding bug document in Firestore.
     */
    private fun addBugMedia(media: Uri) = viewModelScope.launch {
        uploadingStatus.emit(Resource.Loading())
        // Upload the media file to Firebase storage.
        storage.reference
            .child("$BUGS/${_bugId.value}/${media.lastPathSegment}")
            .putFile(media)
            .addOnSuccessListener {
                // Get the download URL of the uploaded file.
                storage.reference.
                child("$BUGS/${_bugId.value}/${media.lastPathSegment}")
                    .downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("images", uri.toString())
                        // Update the bug document in Firestore with the new media URL.
                        db.collection(BUGS)
                            .document(_bugId.value.toString())
                            .update(
                                "media",
                                FieldValue.arrayUnion(uri.toString()),
                                "media",
                                FieldValue.arrayRemove("")
                            )
                        viewModelScope.launch {
                            uploadingStatus.emit(Resource.Success(null))
                        }
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            uploadingStatus.emit(Resource.Error("Failed"))
                        }
                    }
            }.addOnFailureListener {
                viewModelScope.launch {
                    uploadingStatus.emit(Resource.Error("Failed"))
                }
            }
    }

    /**
     * Function to add a new bug report to Firestore with the given title and description.
     */
    fun addBug(
        title : String,
        description : String
    ) = viewModelScope.launch {
        val product = Bug(
            id = _bugId.value.toString(),
            title,
            description
        )
        // Get the latest bug document in the database.
        db.collection(BUGS)
            .orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    if (task.isSuccessful && task.result?.metadata?.isFromCache == false) {
                        if (task.result.isEmpty) {
                            // If the database is empty, set the bug ID to 1.
                            _bugId.value = 1
                            product.id = _bugId.value.toString()
                            // Add the new bug document to Firestore.
                            db.collection(BUGS)
                                .document(_bugId.value.toString())
                                .set(product)
                                .addOnSuccessListener {
                                    if (bugMedia.isEmpty()) {
                                        // Emit Success if no media files are present.
                                        viewModelScope.launch {
                                            uploadingStatus.emit(Resource.Success(true))
                                        }
                                    }
                                }
                            // Upload all media files for the bug report.
                            bugMedia.forEach { media ->
                                addBugMedia(media)
                            }
                        } else {
                            // If the database is not empty, get the biggest ID and increment it.
                            val listOfIds = ArrayList<Int>()
                            task.result.documents.forEach { document ->
                                listOfIds.add(document.id.toInt())
                            }
                            val biggestId = listOfIds.maxOrNull() ?: 0
                            _bugId.value = biggestId + 1
                            product.id = _bugId.value.toString()
                            // Add the new bug document to Firestore.
                            db.collection(BUGS)
                                .document(_bugId.value.toString())
                                .set(product)
                                .addOnSuccessListener {
                                    if (bugMedia.isEmpty()) {
                                        // Emit Success if no media files are present.
                                        viewModelScope.launch {
                                            uploadingStatus.emit(Resource.Success(true))
                                        }
                                    }
                                }
                            // Upload all media files for the bug report.
                            bugMedia.forEach { media ->
                                addBugMedia(media)
                            }
