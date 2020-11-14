package com.application.seb.binfinder.controllers.activities.addCleanEvent

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageButton
import androidx.lifecycle.ViewModel
import com.application.seb.binfinder.R
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.repositories.CleanEventRepository
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

private const val PHOTO_QUALITY = 100

class AddCleanEventActivityViewModel: ViewModel() {

    private var cleanEventRepository = CleanEventRepository()
    private var photoData = ByteArray(0)


    fun saveCleanEventToFireStore(createDate: String, createUserName: String , createUserId: String, date: Int, participants: MutableList<String>?, description: String ,title: String, address: String): Task<DocumentReference> {
        return cleanEventRepository.createCleanEvent(createDate, createUserName, createUserId, date, participants, description, title, address)
    }

    fun updateCleanEventId(eventId: String): Task<Nothing> {
        return cleanEventRepository.updateCleanEventId(eventId)
    }


    fun savePhotoToFirebase(imageButton: ImageButton, cleanEventId: String): UploadTask {
        uploadBitmap(imageButton)
        return cleanEventRepository.uploadPhoto(cleanEventId, photoData)
    }

    private fun uploadBitmap(imageButton: ImageButton) {
        imageButton.isDrawingCacheEnabled = true
        imageButton.buildDrawingCache()
        val bitmap = (imageButton.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, PHOTO_QUALITY, baos)
        photoData = baos.toByteArray()

    }

    fun updateCleanEventForEdit(currentCleanEvent: CleanEvent, newTitle: String, newEventDate: Int, newAddress: String, newDescription: String, photoContainer: ImageButton, context: Context): Task<Void> {
        val data: MutableMap<String, Any> = mutableMapOf()
        if(newTitle != currentCleanEvent.title){ data[Constants.CLEAN_EVENT_TITLE] = newTitle }
        if(newEventDate != currentCleanEvent.eventDate){data[Constants.CLEAN_EVENT_EVENT_DATE] = newEventDate }
        if(newAddress != currentCleanEvent.address){data[Constants.CLEAN_EVENT_ADDRESS] = newAddress }
        if(newDescription != currentCleanEvent.description){data[Constants.CLEAN_EVENT_DESCRIPTION] = newDescription }

        return cleanEventRepository.updateSomeEventData(currentCleanEvent.eventId!!, data).addOnSuccessListener {
            uploadBitmap(photoContainer)
            cleanEventRepository.deletePhoto(currentCleanEvent.eventId!!).addOnSuccessListener {
                cleanEventRepository.uploadPhoto(currentCleanEvent.eventId!!, photoData)
                        .addOnSuccessListener {
                            Utils.startNotification(context.getString(R.string.notification_title_clean_event_update),context.getString(R.string.notification_title_clean_event_update), context)
                        }
            }

        }

    }
}