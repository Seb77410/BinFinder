package com.application.seb.binfinder.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val BIN_CONTENT_COLLECTION_REFERENCE = "BinWaste"



class BinContentRepository {

//--------------------------------------------------------------------------------------------------
// References
//--------------------------------------------------------------------------------------------------
    private fun binContentCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection(BIN_CONTENT_COLLECTION_REFERENCE)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getBinContentByID(binWasteId: String): Task<DocumentSnapshot> {
        return binContentCollection()!!.document(binWasteId).get()
    }

}