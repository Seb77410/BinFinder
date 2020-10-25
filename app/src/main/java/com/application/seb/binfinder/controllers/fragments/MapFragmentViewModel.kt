package com.application.seb.binfinder.controllers.fragments

import androidx.lifecycle.ViewModel
import com.application.seb.binfinder.repositories.BinRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class MapFragmentViewModel : ViewModel() {

    private var binRepository = BinRepository ()

    fun getBinWithType(type : String): Task<QuerySnapshot> {
        return binRepository.getBinByType(type)
    }
}