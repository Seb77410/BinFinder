package com.application.seb.binfinder.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val BIN_WASTE_COLLECTION_REFERENCE = "BinWaste"
private const val TAG = "BinWasteRepository"

private const val GLASS_BIN_WASTE_DOCUMENT_REFERENCE = "Glass"
private const val GREEN_WASTE_BIN_WASTE_DOCUMENT_REFERENCE = "Green waste"
private const val HOUSEHOLD_WASTE_BIN_WASTE_DOCUMENT_REFERENCE = "Household wast"
private const val RECYCLING_BIN_WASTE_DOCUMENT_REFERENCE = "Recycling bin"
private const val RECYCLING_CENTER_WASTE_DOCUMENT_REFERENCE = "Recycling center"
private const val WILD_DEPOSIT_WASTE_DOCUMENT_REFERENCE = "Wild deposit"

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

    fun getGlassBinWaste(): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(GLASS_BIN_WASTE_DOCUMENT_REFERENCE).get()
    }

    fun getGreenWastesBinWaste(): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(GREEN_WASTE_BIN_WASTE_DOCUMENT_REFERENCE).get()
    }

    fun getHouseholdWasteBinWaste(): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(HOUSEHOLD_WASTE_BIN_WASTE_DOCUMENT_REFERENCE).get()
    }

    fun getRecyclingBinWaste(): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(RECYCLING_BIN_WASTE_DOCUMENT_REFERENCE).get()
    }

    fun getRecyclingCenterWaste(): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(RECYCLING_CENTER_WASTE_DOCUMENT_REFERENCE).get()
    }

    fun getWildDepositWaste(): Task<DocumentSnapshot> {
        return binWastesCollection()!!.document(WILD_DEPOSIT_WASTE_DOCUMENT_REFERENCE).get()
    }

}