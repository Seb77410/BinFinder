package com.application.seb.binfinder.repositories

import com.application.seb.binfinder.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val USER_COLLECTION_REFERENCE = "Users"
private const val USER_LIKED_BIN_LIST = "likedBinsList"


class UserRepository {

//--------------------------------------------------------------------------------------------------
// Reference
//--------------------------------------------------------------------------------------------------
    private fun usersCollection(): CollectionReference? {
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION_REFERENCE)
    }

//--------------------------------------------------------------------------------------------------
// Create
//--------------------------------------------------------------------------------------------------
    fun createUser(userId: String, username: String, urlPicture: String?): Task<Void?>? {
        val userToCreate = User(userId, username, urlPicture, null)
        return usersCollection()!!.document(userId).set(userToCreate)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getUser(userId: String): Task<DocumentSnapshot?>? {
        return usersCollection()!!.document(userId).get()
    }

//--------------------------------------------------------------------------------------------------
// Update
//--------------------------------------------------------------------------------------------------
    fun updateUserLikedBinsList(user: User): Task<Void> {
        return usersCollection()!!.document(user.userId).update(USER_LIKED_BIN_LIST, user.likedBinsList)
    }

}