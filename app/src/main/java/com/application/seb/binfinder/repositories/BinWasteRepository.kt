package com.application.seb.binfinder.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val BIN_WASTE_COLLECTION_REFERENCE = "BinWaste"



class BinWastesRepository {

//--------------------------------------------------------------------------------------------------
// References
//--------------------------------------------------------------------------------------------------
    private fun binWastesCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection(BIN_WASTE_COLLECTION_REFERENCE)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getBinWastesByID(binWasteId: String): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(binWasteId).get()
    }

}