package com.application.seb.binfinder.repositories

import android.location.Location
import com.application.seb.binfinder.models.Bin
import com.application.seb.binfinder.utils.Constants
import com.ckdroid.geofirequery.GeoQuery
import com.ckdroid.geofirequery.model.Distance
import com.ckdroid.geofirequery.utils.BoundingBoxUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage


private const val BIN_COLLECTION_REFERENCE = "Bins"
private const val LOCATION = "myLocation"
private const val MAX_DISTANCE_SEARCH = 10000.0
private const val MAX_LOCATION_NUMBER: Long = 5

class BinRepository {


//--------------------------------------------------------------------------------------------------
// References
//--------------------------------------------------------------------------------------------------
    private fun binCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection(BIN_COLLECTION_REFERENCE)
    }

    fun photoReference(binId: String): StorageReference {
        return Firebase.storage.reference.child("$BIN_COLLECTION_REFERENCE/$binId")
    }



//--------------------------------------------------------------------------------------------------
// Create
//--------------------------------------------------------------------------------------------------
    fun createBin(type: String, location: GeoPoint?, addBy_userID: String, addBy_userName: String): Task<DocumentReference> {
        val binToCreate = Bin(null, type, location, addBy_userID, addBy_userName)
        return binCollection()!!.add(binToCreate)
    }

    fun uploadPhoto(binId: String, data: ByteArray): UploadTask {
        return photoReference(binId).putBytes(data)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getBinById(binId: String): Task<DocumentSnapshot> {
        return binCollection()!!.document(binId).get()
    }

    fun getBinByDistance(lat: Double, long: Double, type : String): GeoQuery {
        val centerLocation = Location(LOCATION)
        centerLocation.latitude = lat
        centerLocation.longitude = long
        val distanceForRadius = Distance(MAX_DISTANCE_SEARCH, BoundingBoxUtils.DistanceUnit.KILOMETERS)

        return GeoQuery()
                .collection(BIN_COLLECTION_REFERENCE)
                .whereEqualTo(Constants.CLEAN_EVENT_TYPE,type)
                .whereNearToLocation(centerLocation, distanceForRadius)
                .limit(MAX_LOCATION_NUMBER)
    }

//--------------------------------------------------------------------------------------------------
// Update
//--------------------------------------------------------------------------------------------------

    fun updateBinLike(binId: String, incrementValue: Int): Task<Nothing> {
        val binRef = binCollection()!!.document(binId)

        return FirebaseFirestore.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(binRef)

            val newLikeNumber = snapshot.getDouble(Constants.BIN_LIKE)!! + incrementValue
            transaction.update(binRef, Constants.BIN_LIKE, newLikeNumber)

            // Success
            null
        }
    }


}