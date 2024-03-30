import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesteem.mylauncher.Resource
import com.codesteem.mylauncher.util.Constants.BUGS
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BugReportViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage
    private val _bugId = MutableLiveData<Int>()
    var bugMedia = ArrayList<Uri>()
    private val _uploadingStatus = MutableStateFlow<Resource<Boolean>>(Resource.Initial())
    val uploadingStatus: Flow<Resource<Boolean>> = _uploadingStatus

    init {
        getBiggestIdInTheDb()
    }

    private fun addBugMedia(media: Uri) = viewModelScope.launch {
        _uploadingStatus.emit(Resource.Loading())
        val mediaRef = storage.reference.child("$BUGS/${_bugId.value}/${media.lastPathSegment}").apply {
            putFile(media).addOnSuccessListener {
                downloadUrl.addOnSuccessListener { uri ->
                    Log.e("images", uri.toString())
                    db.collection(BUGS)
                        .document(_bugId.value.toString())
                        .update(
                            "media",
                            FieldValue.arrayUnion(uri.toString()),
                            "media",
                            FieldValue.arrayRemove("")
                        )
                    _uploadingStatus.emit(Resource.Success(null))
                }.addOnFailureListener {
                    _uploadingStatus.emit(Resource.Error("Failed"))
                }
            }.addOnFailureListener {
                _uploadingStatus.emit(Resource.Error("Failed"))
            }
        }
    }

    fun addBug(title: String, description: String) = viewModelScope.launch {
        val product = Bug(
            id = _bugId.value.toString(),
            title,
            description
        )
        db.collection(BUGS)
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && !task.result.metadata.isFromCache) {
                    _bugId.value = if (task.result.isEmpty) {
                        1
                    } else {
                        val listOfIds = task.result.map { it.id.toInt() }
                        listOfIds.maxOrNull() ?: 0 + 1
                    }
                    product.id = _bugId.value.toString()
                    db.collection(BUGS)
                        .document(_bugId.value.toString())
                        .set(product)
                        .addOnSuccessListener {
                            if (bugMedia.isEmpty()) {
                                _uploadingStatus.emit(Resource.Success(true))
                            } else {
                                bugMedia.forEach { media ->
                                    addBugMedia(media)
                                }
                            }
                        }
                }
            }
    }

    private fun getBiggestIdInTheDb() {
        db.collection(BUGS)
            .orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && !task.result.metadata.isFromCache) {
                    task.result.forEach { document ->
                        _bugId.value = document.id.toInt()
                    }
                }
            }
    }
}
