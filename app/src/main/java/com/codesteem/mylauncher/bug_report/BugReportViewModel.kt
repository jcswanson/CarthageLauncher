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
import kotlinx.coroutines.tasks.await

/**
 * ViewModel for managing bug reports.
 */
class BugReportViewModel : ViewModel() {
    // Firebase Firestore instance
    val db = FirebaseFirestore.getInstance()
    // Firebase Storage instance
    val storage = Firebase.storage
    // State flow to manage uploading status
    val uploadingStatus = MutableStateFlow<Resource<Boolean>>(Resource.Initial())

    // Mutable LiveData to hold the bug ID
    private val _bugId = MutableLiveData<Int>()
    // ArrayList to hold bug media URIs
    var bugMedia = ArrayList<Uri>()

    /**
     * Initializes the ViewModel and fetches the biggest ID from the database.
     */
    init {
        getBiggestIdInTheDb()
    }

    /**
     * Uploads a single bug media to Firebase Storage and updates the corresponding document in Firebase Firestore.
     *
     * @param media The Uri of the media to be uploaded.
     */
    private fun addBugMedia(media: Uri) = viewModelScope.launch {
        uploadingStatus.emit(Resource.Loading())
        // Reference to the media in Firebase Storage
        storage.reference
            .child("$BUGS/${_bugId.value}/${media.lastPathSegment}")
            .putFile(media)
            .addOnSuccessListener {
                // Download URL of the uploaded media
                storage.reference.
                child("$BUGS/${_bugId.value}/${media.lastPathSegment}")
                    .downloadUrl
                    .addOnSuccessListener {
                        Log.e("images", it.toString())
                        // Update the Firestore document with the new media URL
                        db.collection(BUGS)
                            .document(_bugId.value.toString())
                            .update(
                                "media",
                                FieldValue.arrayUnion(it.toString()),
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
     * Adds a new bug report to Firebase Firestore with the given title and description.
     *
     * @param title The title of the bug report.
     * @param description The description of the bug report.
     */
    fun addBug(
        title: String,
        description: String
    ) = viewModelScope.launch {
        val product = Bug(
            id = _bugId.value.toString(),
            title,
            description
        )
        // Fetch the latest bug ID from the database
        db.collection(BUGS)
            .orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnCompleteListener {
                viewModelScope.launch {
                    if (it.isSuccessful && it.result?.metadata?.isFromCache == false) {
                        if (it.result.isEmpty) {
                            // If the database is empty, set the bug ID to 1
                            _bugId.value = 1
                            product.id = _bugId.value.toString()
                            // Add the new bug report to the database
                            db.collection(BUGS)
                                .document(_bugId.value.toString())
                                .set(product)
                                .addOnSuccessListener {
                                    if (bugMedia.isEmpty()) {
                                        viewModelScope.launch {
                                            uploadingStatus.emit(Resource.Success(true))
                                        }
                                    }
                                }
                            // Upload all bug media to Firebase Storage
                            bugMedia.forEach { media ->
                                addBugMedia(media)
                            }
                        } else {
                            // If the database is not empty, find the biggest ID and increment it by 1
                            it.result
                            for (every in it.result!!) {
                                _bugId.value = biggestId + 1
                                product.id = _bugId.value.toString()
                                // Add the new bug report to the database
                                db.collection(BUGS)
                                    .document(_bugId.value.toString())
                                    .set(product)
                                    .addOnSuccessListener {
                                        if (bugMedia.isEmpty()) {
                                            viewModelScope.launch {
                                                uploadingStatus.emit(Resource.Success(true))
                                            }
                                        }
                                    }
                                // Upload all bug media to Firebase Storage
                                bugMedia.forEach { media ->
                                    addBugMedia(media)
                                }
                            }
                        }

                    }
                }
            }.addOnCanceledListener {
                Log.e("cancel", "")
            }
    }

   
