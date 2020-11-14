package com.application.seb.binfinder.controllers.fragments.addBin

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageButton
import androidx.lifecycle.ViewModel
import com.application.seb.binfinder.models.Bin
import com.application.seb.binfinder.repositories.BinRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class AddBinFragmentViewModel : ViewModel() {


    private var binRepository = BinRepository()
    private var photoData = ByteArray(0)


    fun saveBinToFirebase(bin: Bin): Task<DocumentReference> {
        return binRepository.createBin(bin.type, bin.geoLocation, bin.addBy_userID, bin.addBy_userName)

    }

    fun savePhotoToFirebase(imageButton: ImageButton, binId: String): UploadTask {
        uploadBitmap(imageButton)
        return binRepository.uploadPhoto(binId, photoData)
    }

    private fun uploadBitmap(imageButton: ImageButton) {
        imageButton.isDrawingCacheEnabled = true
        imageButton.buildDrawingCache()
        val bitmap = (imageButton.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        photoData = baos.toByteArray()

    }
}