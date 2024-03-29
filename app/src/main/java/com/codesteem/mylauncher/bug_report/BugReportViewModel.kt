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

class BugReportViewModel: ViewModel() {
    val db = FirebaseFirestore.getInstance()
    val storage = Firebase.storage
    val uploadingStatus = MutableStateFlow<Resource<Boolean>>(Resource.Initial())

    private val _bugId = MutableLiveData<Int>()
    var bugMedia = ArrayList<Uri>()

    init {
        getBiggestIdInTheDb()
    }

    private fun addBugMedia(media: Uri) = viewModelScope.launch {
        uploadingStatus.emit(Resource.Loading())
        storage.reference
            .child("$BUGS/${_bugId.value}/${media.lastPathSegment}")
            .putFile(media)
            .addOnSuccessListener {
                storage.reference.
                child("$BUGS/${_bugId.value}/${media.lastPathSegment}")
                    .downloadUrl
                    .addOnSuccessListener {
                        Log.e("images", it.toString())
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


    fun addBug(
        title : String,
        description : String
    ) = viewModelScope.launch {
        val product = Bug(
            id = _bugId.value.toString(),
            title,
            description
        )
        db.collection(BUGS)
            .orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnCompleteListener {
                viewModelScope.launch {
                    if (it.isSuccessful && it.result?.metadata?.isFromCache == false) {
                        if (it.result.isEmpty) {
                            _bugId.value = 1
                            product.id = _bugId.value.toString()
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
                            bugMedia.forEach { media ->
                                addBugMedia(media)
                            }
                        } else {
                            it.result
                            for (every in it.result!!) {
                                _bugId.value = biggestId +1
                                product.id = _bugId.value.toString()
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

    private var biggestId = 0
    private fun getBiggestIdInTheDb() = viewModelScope.launch {
        val listOfIds = ArrayList<Int>()
        db.collection(BUGS)
            .get()
            .addOnSuccessListener {
                it.documents.forEach { document ->
                    listOfIds.add(document.id.toInt())
                }
                biggestId = listOfIds.maxOrNull() ?: 0
            }
    }
}