package com.application.seb.binfinder.controllers.activities.addCleanEvent

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.application.seb.binfinder.BuildConfig
import com.application.seb.binfinder.R
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.models.GeocodeResponse
import com.application.seb.binfinder.repositories.CleanEventRepository
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.GlideApp
import com.application.seb.binfinder.utils.Service
import com.application.seb.binfinder.utils.Utils
import com.ckdroid.geofirequery.setLocation
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val REQUEST_IMAGE_CAPTURE = 1
private const val TAG = "AddCleanEventActivity"


class AddCleanEventActivity : AppCompatActivity() {


//--------------------------------------------------------------------------------------------------
// For Data
//--------------------------------------------------------------------------------------------------
    private lateinit var toolbar: Toolbar
    private lateinit var imageButton: ImageButton
    private lateinit var titleLayout: TextInputLayout
    private lateinit var dateLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var saveButton: ExtendedFloatingActionButton

    private lateinit var event: CleanEvent
    private var title: String? = null
    private var eventDate: Int = 0
    private var address: String? = null
    private var description: String? = null
    private var userTakePhoto = false
    private var model = AddCleanEventActivityViewModel()

    private var activityIsForEventEdit = false


//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_clean_event)

        toolbar = findViewById(R.id.add_clean_event_activity_toolbar)
        imageButton = findViewById(R.id.add_clean_event_activity_image)
        titleLayout = findViewById(R.id.add_clean_event_activity_title)
        dateLayout = findViewById(R.id.add_clean_event_activity_date)
        addressLayout = findViewById(R.id.add_clean_event_activity_address)
        descriptionLayout = findViewById(R.id.add_clean_event_activity_description)
        saveButton = findViewById(R.id.add_clean_event_activity_save_fab)

        getArgs()
        configureToolbar()
        configureImageButton()
        configureTitleView()
        configureDateButton()
        configureAddressView()
        configureDescriptionView()
        configureDateView()
        configureSaveButton()
    }

//--------------------------------------------------------------------------------------------------
// Get args
//--------------------------------------------------------------------------------------------------
    private fun getArgs(){
        val sEvent = intent.getStringExtra(Constants.ARGS_EVENT)
        if(sEvent != null){
            event = Utils.convertStringToCleanEvent(sEvent)
            activityIsForEventEdit = true
        }
    }


//--------------------------------------------------------------------------------------------------
// Toolbar
//--------------------------------------------------------------------------------------------------

    private fun configureToolbar() {
        // Set action bar
        setSupportActionBar(toolbar)
        if(activityIsForEventEdit){ toolbar.title = getString(R.string.add_clean_event_activity_name)}
        //Set back stack
        val upArrow = ResourcesCompat.getDrawable(this.resources, R.drawable.ic_arrow_back_white_24dp, null)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(upArrow)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }


//--------------------------------------------------------------------------------------------------
// Image Button
//--------------------------------------------------------------------------------------------------

    private fun configureImageButton(){
        imageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        if(activityIsForEventEdit){
            GlideApp.with(applicationContext)
                    .load((CleanEventRepository().photoReference(event.eventId!!)))
                    .centerCrop()
                    .into(imageButton)
            userTakePhoto = true
        }

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get(Constants.DATA) as Bitmap
            imageButton.setImageBitmap(imageBitmap)
            userTakePhoto = true
        }
    }

//--------------------------------------------------------------------------------------------------
// Edit Text view
//--------------------------------------------------------------------------------------------------
    private fun configureTitleView(){ if(activityIsForEventEdit) {titleLayout.editText!!.setText(event.title)} }
    private fun configureAddressView(){ if(activityIsForEventEdit) {addressLayout.editText!!.setText(event.address)}}
    private fun configureDescriptionView(){ if(activityIsForEventEdit) { descriptionLayout.editText!!.setText(event.description)}}
    private fun configureDateView(){if(activityIsForEventEdit){ dateLayout.editText!!.setText(Utils.convertDataDateToFormatString(event.eventDate))
        eventDate = event.eventDate
        Log.d(TAG, "configureDateView() -> eventDate = $eventDate")
    }}


//--------------------------------------------------------------------------------------------------
// Date Button
//--------------------------------------------------------------------------------------------------
    private fun configureDateButton(){
        dateLayout.editText!!.setOnClickListener {
            showDatePicker(Calendar.getInstance())
        }
}

    private fun showDatePicker(calendar: Calendar) {
            val datePickerDialog = DatePickerDialog(this, R.style.TimePickerTheme, { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
               val date = String.format("%s %s %s %s %s", Utils.convertDateIntToString(dayOfMonth), "/", Utils.convertDateIntToString(month + 1), "/", year)
                dateLayout.editText!!.setText(date)
                eventDate = "$year${Utils.convertDateIntToString(month+1)}${Utils.convertDateIntToString(dayOfMonth)}".toInt()
                Log.d(TAG, "showDatePicker() -> eventDate = $eventDate")
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                calendar[Calendar.MONTH] = month
                calendar[Calendar.YEAR] = year
            }, Calendar.getInstance()[Calendar.YEAR],
                    Calendar.getInstance()[Calendar.MONTH],
                    Calendar.getInstance()[Calendar.DAY_OF_MONTH])
            datePickerDialog.show()
    }

//--------------------------------------------------------------------------------------------------
// Save Button
//--------------------------------------------------------------------------------------------------

    private fun configureSaveButton(){

        saveButton.setOnClickListener{
            if(checkForErrors()){
                when(activityIsForEventEdit){
                    true -> { updateCleanEvent() }
                    false -> { saveCleanEventInstance() }
                }
            }else{
                Log.e(TAG, "checkForError not passed")
            }
        }
    }


    private fun saveCleanEventInstance(){
        val createDate = Utils.convertCalendarToFormatString(Calendar.getInstance())
        val createBy = FirebaseAuth.getInstance().currentUser!!.displayName!!
        val participants : MutableList<String> = mutableListOf()
        participants.add(FirebaseAuth.getInstance().currentUser!!.uid)

        convertAddressToLatLng()?.enqueue(object: Callback<GeocodeResponse?> {
            override fun onResponse(call: Call<GeocodeResponse?>, response: Response<GeocodeResponse?>) {
                if (response.body()!!.status == "OK") {
                    val lat = response.body()!!.results[0].geometry.location.lat
                    val lng = response.body()!!.results[0].geometry.location.lng
                    model.saveCleanEventToFireStore(createDate.toString(), createBy, FirebaseAuth.getInstance().uid!!, eventDate, participants, description.toString(), title.toString(), address.toString())
                            .addOnSuccessListener { doc ->
                                model.updateCleanEventId(doc.id).addOnSuccessListener {
                                    doc.setLocation(lat, lng).addOnSuccessListener {
                                        model.savePhotoToFirebase(imageButton, doc.id)
                                                .addOnSuccessListener {
                                                    Log.d(TAG, "Clean Event photo Save")
                                                    Utils.startNotification(getString(R.string.notification_title_clean_event_save), getString(R.string.notification_content_clean_event_save), applicationContext)
                                                }
                                    }
                                }
                                Log.d(TAG, "Clean Event Save")
                            }
                } else {
                    addressLayout.error = getString(R.string.invalid_address)
                    Log.e(TAG, "error during convert address to latlng")}
            }

            override fun onFailure(call: Call<GeocodeResponse?>, t: Throwable) {Log.e(TAG, "geocode request error : " + t.message!! )}
        })
    }

    private fun updateCleanEvent(){
        Log.d(TAG, "updateCleanEvent()")
        model.updateCleanEventForEdit(event, getTextFromInputLayout(titleLayout)!!,eventDate, getTextFromInputLayout(addressLayout)!!, getTextFromInputLayout(descriptionLayout)!!, imageButton, this)
    }

    private fun convertAddressToLatLng(): Call<GeocodeResponse?>? {
        // Data for Geocode API request
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.GEOCODE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val geocodeService = retrofit.create(Service::class.java)


        return geocodeService.getAddressToLatLng(address.toString(), BuildConfig.GEOCODING_API_KEY)

    }


//--------------------------------------------------------------------------------------------------
// Error check
//--------------------------------------------------------------------------------------------------

    private fun checkForErrors(): Boolean {
        getEventData()
        val photoError = !isPhotoError()
        val dataError = !isDataError()
        return photoError && dataError
    }

    private fun getTextFromInputLayout(inputLayout: TextInputLayout): String? = inputLayout.editText!!.text.toString()

    private fun getEventData(){
        title = getTextFromInputLayout(titleLayout)
        Log.d(TAG, "title value = $title")
        Log.d(TAG, "title error = ${checkForLayoutValueError(title)}")
        address = getTextFromInputLayout(addressLayout)
        Log.d(TAG, "address value = $address")
        Log.d(TAG, "address error = ${checkForLayoutValueError(address)}")
        description = getTextFromInputLayout(descriptionLayout)
        Log.d(TAG, "description value = $description")
        Log.d(TAG, "description error = ${checkForLayoutValueError(description)}")
    }

    private fun checkForLayoutValueError(value: String?): Boolean = value.isNullOrEmpty()

    private fun isDataError(): Boolean{

            return if (!checkForLayoutValueError(title) && eventDate > 0 && !checkForLayoutValueError(address) && !checkForLayoutValueError(description)) { false }
            else {

                if (checkForLayoutValueError(title)) { titleLayout.error = getString(R.string.required) }
                else { titleLayout.isErrorEnabled = false }

                if (eventDate == 0) { dateLayout.error = getString(R.string.required) }
                else { dateLayout.isErrorEnabled = false }

                if (checkForLayoutValueError(address)) { addressLayout.error = getString(R.string.required) }
                else { addressLayout.isErrorEnabled = false }

                if (checkForLayoutValueError(description)) { descriptionLayout.error = getString(R.string.required) }
                else { descriptionLayout.isErrorEnabled = false }

                true
        }
    }

    private fun isPhotoError(): Boolean{
        return if(!userTakePhoto){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder
                    .setTitle(getString(R.string.alert_dialog_no_photo_title))
                    .setMessage(getString(R.string.alert_dialog_no_photo_content))
                    .setPositiveButton(getString(R.string.alert_dialog_ok_button)){ _: DialogInterface, _:Int ->
                        Log.d(TAG, "No photo alert dialog - click YES button")
                    }
                    .show()
            true
        }
        else{ false }
    }


}