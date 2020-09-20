package com.application.seb.binfinder.repositories

import android.net.Uri
import com.application.seb.binfinder.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val USER_COLLECTION_REFERENCE = "Users"

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
    fun createUser(userId: String, username: String, urlPicture: Uri?): Task<Void?>? {
        val userToCreate = User(userId, username, urlPicture)
        return usersCollection()!!.document(userId).set(userToCreate)
    }

//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getUser(userId: String): Task<DocumentSnapshot?>? {
        return usersCollection()!!.document(userId).get()
    }

}