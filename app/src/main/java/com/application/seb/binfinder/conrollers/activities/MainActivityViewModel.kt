package com.application.seb.binfinder.conrollers.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import com.application.seb.binfinder.models.User
import com.application.seb.binfinder.repositories.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class MainActivityViewModel : ViewModel() {

    private val userRepository = UserRepository()

    fun saveUser(user: User) {
        userRepository.createUser(user.userId, user.userName, user.photo)!!
                .addOnSuccessListener {
                    Log.d("MainActivity", "user create successful")
                }
    }

    fun getUser(userId: String): Task<DocumentSnapshot?>? = userRepository.getUser(userId)
    
}