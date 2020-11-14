package com.application.seb.binfinder.repositories

import android.net.Uri
import com.application.seb.binfinder.models.CleanEvent
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

private const val CLEAN_EVENT_COLLECTION_REFERENCE = "CleanEvents"
private const val LIST_LIMIT: Long = 20
private const val EVENT_DATE = "eventDate"
private const val CREATOR_ID = "createBy_userId"
private const val PARTICIPANTS = "participants"
private const val EVENT_ID = "eventId"
private const val COMMENTS = "comments"


class CleanEventRepository {

//--------------------------------------------------------------------------------------------------
// References
//--------------------------------------------------------------------------------------------------
    private fun cleanEventCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(CLEAN_EVENT_COLLECTION_REFERENCE)
    }

    fun photoReference(cleanEventId: String): StorageReference {
        return Firebase.storage.reference.child("$CLEAN_EVENT_COLLECTION_REFERENCE/$cleanEventId")
    }


//--------------------------------------------------------------------------------------------------
// Create
//--------------------------------------------------------------------------------------------------
    fun createCleanEvent(createDate: String, createUserName: String , createUserId: String,date: Int, participants: MutableList<String>?, description: String ,title: String, address: String): Task<DocumentReference> {
        val cleanEvent = CleanEvent(null, createDate, createUserName, createUserId,date, participants, description, title, address)
        return cleanEventCollection().add(cleanEvent)
    }


    fun uploadPhoto(cleanEventId: String, data: ByteArray): UploadTask {
        return photoReference(cleanEventId).putBytes(data)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getPhoto(cleanEventId: String): Task<Uri> {
        return photoReference(cleanEventId).downloadUrl
    }

    fun getCleanEventByDate(currentFormatDate: String): Task<QuerySnapshot> {
        return cleanEventCollection().whereGreaterThanOrEqualTo(EVENT_DATE, currentFormatDate.toInt()).limit(LIST_LIMIT).orderBy("eventDate", Query.Direction.ASCENDING).get()
    }

    fun getCleanEventByCreateUserId(userId: String): Task<QuerySnapshot> {
        return cleanEventCollection().whereEqualTo(CREATOR_ID, userId).limit(LIST_LIMIT).get()
    }

    fun getParticipateCleanEvent(userId: String): Task<QuerySnapshot> {
        return cleanEventCollection().whereArrayContains( PARTICIPANTS, userId).limit(LIST_LIMIT).get()
    }

//--------------------------------------------------------------------------------------------------
// Update
//--------------------------------------------------------------------------------------------------


    fun updateSomeEventData(eventId: String, data: Map<String, Any> ): Task<Void> {
        return cleanEventCollection().document(eventId).update(data)
    }
    fun addParticipantToCleanEvent(eventId: String, participantId: String): Task<Nothing> {
        val eventRef = cleanEventCollection().document(eventId)

        return FirebaseFirestore.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(eventRef)

            val newLikeNumber: MutableList<String> = snapshot.get(PARTICIPANTS) as MutableList<String>

            newLikeNumber.add(participantId)
            transaction.update(eventRef, PARTICIPANTS, newLikeNumber)

            // Success
            null
        }
    }

    fun removeParticipantToCleanEvent(eventId: String, participantId: String): Task<Nothing> {
        val eventRef = cleanEventCollection().document(eventId)

        return FirebaseFirestore.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(eventRef)

            val newLikeNumber: MutableList<String> = snapshot.get(PARTICIPANTS) as MutableList<String>

            newLikeNumber.remove(participantId)
            transaction.update(eventRef, PARTICIPANTS, newLikeNumber)

            // Success
            null
        }
    }

    fun updateCleanEventId(eventId: String): Task<Nothing> {
        val eventRef = cleanEventCollection().document(eventId)

        return FirebaseFirestore.getInstance().runTransaction { transaction ->
            transaction.update(eventRef, EVENT_ID , eventId)
            // Success
            null
        }
    }

    fun updateCommentsList(eventId: String, commentId: String): Task<Nothing> {
        val eventRef = cleanEventCollection().document(eventId)

        return FirebaseFirestore.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(eventRef)

            val newCommentsList: MutableList<String> = snapshot.get(COMMENTS) as MutableList<String>

            newCommentsList.add(0,commentId)
            transaction.update(eventRef, COMMENTS, newCommentsList)

            // Success
            null
        }    }

//--------------------------------------------------------------------------------------------------
// Delete
//--------------------------------------------------------------------------------------------------

    fun deleteCleanEvent(eventId: String): Task<Void> {
        return cleanEventCollection().document(eventId).delete()
    }

    fun deletePhoto(eventId: String): Task<Void> {
        return photoReference(eventId).delete()
    }

}