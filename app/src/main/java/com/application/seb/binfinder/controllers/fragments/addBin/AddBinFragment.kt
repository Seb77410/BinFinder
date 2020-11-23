package com.application.seb.binfinder.controllers.fragments.addBin

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.application.seb.binfinder.R
import com.application.seb.binfinder.models.Bin
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.Utils
import com.ckdroid.geofirequery.setLocation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import java.util.*

    private const val TAG = "AddBinFragment"
    private const val PARAM = "user_location"
    private const val REQUEST_IMAGE_CAPTURE = 1

class AddBinFragment : Fragment() {
//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
    private var location: String? = null
    private lateinit var fabSave: FloatingActionButton
    private lateinit var imageButton: ImageButton
    private var userTakePhoto = false
    private lateinit var radioGroup: RadioGroup
    private lateinit var addBinFragmentViewModel: AddBinFragmentViewModel

    interface OnFragmentInteractionListener { fun binSaved(binType: String) }

//--------------------------------------------------------------------------------------------------
// Constructor
//--------------------------------------------------------------------------------------------------
    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                    AddBinFragment().apply {
                        arguments = Bundle().apply {
                            putString(PARAM, param1)
                        }
                    }
    }


//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            location = it.getString(PARAM)
            Log.d(TAG, " Argument $PARAM = $location")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_add_bin, container, false)

        fabSave = rootView.findViewById(R.id.add_bin_fragment_save_button)
        imageButton = rootView.findViewById(R.id.add_bin_fragment_imageView)
        radioGroup = rootView.findViewById(R.id.add_bin_fragment_radioGroup)
        addBinFragmentViewModel = AddBinFragmentViewModel()

        configureImageButton()
        configureSaveButton()
        return rootView
    }


//--------------------------------------------------------------------------------------------------
// Configure FAB save button
//--------------------------------------------------------------------------------------------------

    private fun configureSaveButton(){
        fabSave.setOnClickListener {
            if (!userTakePhoto) {showErrorDialog()}
            else {showSaveConfirmDialog()}
        }
    }

    private fun showErrorDialog(){
        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder
                .setTitle(getString(R.string.alert_dialog_no_photo_title))
                .setMessage(getString(R.string.alert_dialog_no_photo_content))
                .setPositiveButton(getString(R.string.alert_dialog_ok_button)){ _: DialogInterface, _:Int ->
                    Log.d(TAG, "No photo alert dialog - click YES button")
                }
                .show()
    }

    private fun showSaveConfirmDialog(){
        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder
                .setTitle(getString(R.string.alert_dialog_save_location_title))
                .setMessage(getString(R.string.alert_dialog_save_location_content))
                .setPositiveButton(getString(R.string.alert_dialog_yes_button)) { _: DialogInterface, _: Int ->
                    Log.d(TAG, "Saving alert dialog - click YES button")
                    saveLocation()
                }
                .setNegativeButton(getString(R.string.alert_dialog_no_button)) { _: DialogInterface, _: Int ->
                    Log.d(TAG, "Saving alert dialog - click NO button")
                }
                .show()
    }

    private fun saveLocation() {
        // Get bin type
        val radioButtonID: Int = radioGroup.checkedRadioButtonId
        val radioButton: RadioButton = radioGroup.findViewById(radioButtonID)
        val binType = radioButton.text.toString()
        Log.d(TAG, "saveLocation() -> bin type = $binType")
        // Get user id and user name
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userName = FirebaseAuth.getInstance().currentUser!!.displayName
        // Create Bin
        val bin = Bin(null, binType, Utils.convertStringToGeoPoint(location!!), userId, userName!!)
        addBinFragmentViewModel.saveBinToFirebase(bin)
                .addOnSuccessListener { document ->
                    Log.d(TAG, "Bin create successful")
                    document.setLocation(bin.geoLocation!!.latitude, bin.geoLocation!!.longitude)

                    addBinFragmentViewModel.savePhotoToFirebase(imageButton, document.id)
                            .addOnSuccessListener {
                                // Start notification
                                Utils.startNotification(getString(R.string.notification_title_bin_save), getString(R.string.notification_content_bin_save), context!!)
                                // Show location on MapFragment
                                (Objects.requireNonNull(activity) as OnFragmentInteractionListener).binSaved(binType)
                                Log.d(TAG, "Photo upload successful")
                            }
                            .addOnFailureListener {Log.e(TAG, "Photo upload failed")}
                }
                .addOnFailureListener {Log.e(TAG, "Bin create failed")}
    }

//--------------------------------------------------------------------------------------------------
// Camera
//--------------------------------------------------------------------------------------------------

    private fun configureImageButton(){
        imageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get(Constants.DATA) as Bitmap
            imageButton.setImageBitmap(imageBitmap)
            userTakePhoto = true
        }
    }
}