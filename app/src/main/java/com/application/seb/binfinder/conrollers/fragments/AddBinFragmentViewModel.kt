package com.application.seb.binfinder.conrollers.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageButton
import androidx.lifecycle.ViewModel
import com.application.seb.binfinder.models.Bin
import com.application.seb.binfinder.repositories.BinRepository
import java.io.ByteArrayOutputStream

class AddBinFragmentViewModel : ViewModel() {


    private var binRepository = BinRepository()
    private var photoData = ByteArray(0)


    fun saveBinToFirebase(imageButton: ImageButton, bin: Bin) {
        binRepository.createBin(bin.type, bin.location, bin.addBy_userID, bin.addBy_userName)
                .addOnSuccessListener { document ->
                    Log.d("AddBinFragment", "Bin create successful")
                    savePhotoToFirebase(imageButton, document.id)
                }
                .addOnFailureListener {
                    Log.e("AddBinFragment", "Bin create failed")
                }
    }

    private fun savePhotoToFirebase(imageButton: ImageButton, binId: String) {
        uploadBitmap(imageButton)
        binRepository.uploadPhoto(binId, photoData)
                .addOnSuccessListener {
                    Log.d("AddBinFragment", "Photo upload successful")
                }
                .addOnFailureListener {
                    Log.e("AddBinFragment", "Photo upload failed")
                }
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