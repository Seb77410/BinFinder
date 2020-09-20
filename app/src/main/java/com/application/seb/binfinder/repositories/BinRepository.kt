package com.application.seb.binfinder.repositories

import com.application.seb.binfinder.models.Bin
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

private const val BIN_COLLECTION_REFERENCE = "Bins"


class BinRepository {


//--------------------------------------------------------------------------------------------------
// References
//--------------------------------------------------------------------------------------------------
    private fun binCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection(BIN_COLLECTION_REFERENCE)
    }

    private fun photoCollection(binId: String): StorageReference {
        return Firebase.storage.getReference(binId)
    }

//--------------------------------------------------------------------------------------------------
// Create
//--------------------------------------------------------------------------------------------------
    fun createBin(type : String, location : LatLng, addBy_userID : String, addBy_userName: String): Task<DocumentReference> {
        val binToCreate = Bin(null, type, location, addBy_userID, addBy_userName)
        return binCollection()!!.add(binToCreate)
    }

    fun uploadPhoto(binId: String, data: ByteArray): UploadTask {
        return photoCollection(binId).putBytes(data)

    }



}