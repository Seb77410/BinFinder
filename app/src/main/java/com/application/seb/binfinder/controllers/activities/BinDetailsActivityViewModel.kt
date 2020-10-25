package com.application.seb.binfinder.controllers.activities

import androidx.lifecycle.ViewModel
import com.application.seb.binfinder.models.User
import com.application.seb.binfinder.repositories.BinRepository
import com.application.seb.binfinder.repositories.BinWastesRepository
import com.application.seb.binfinder.repositories.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

class BinDetailsActivityViewModel: ViewModel() {

//--------------------------------------------------------------------------------------------------
// Data
//--------------------------------------------------------------------------------------------------
    private var binRepository = BinRepository ()
    private var userRepository = UserRepository()
    private var binWastesRepository = BinWastesRepository()


//--------------------------------------------------------------------------------------------------
// Get
//--------------------------------------------------------------------------------------------------
    fun getBinData(binId: String): Task<DocumentSnapshot> {return binRepository.getBinById(binId)}
    fun getBinWasteById(binWasteId: String): Task<DocumentSnapshot> {return binWastesRepository.getBinWastesByID(binWasteId)}
    fun getUser(): Task<DocumentSnapshot?>? {return userRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid)}

//--------------------------------------------------------------------------------------------------
// Update
//--------------------------------------------------------------------------------------------------
    fun updateBinLike(binId: String, incrementValue: Int): Task<Nothing> {return binRepository.updateBinLike(binId, incrementValue)}

    fun updateUser(user: User): Task<Void> {return userRepository.updateUserLikedBinsList(user)}
}