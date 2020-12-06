package com.application.seb.binfinder.controllers.activities

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.application.seb.binfinder.BuildConfig
import com.application.seb.binfinder.R
import com.application.seb.binfinder.models.Bin
import com.application.seb.binfinder.models.BinContent
import com.application.seb.binfinder.models.GeocodeResponse
import com.application.seb.binfinder.models.User
import com.application.seb.binfinder.repositories.BinRepository
import com.application.seb.binfinder.repositories.BinContentRepository
import com.application.seb.binfinder.repositories.UserRepository
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.GlideApp
import com.application.seb.binfinder.utils.Service
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
private lateinit var mToolbar :Toolbar
private lateinit var binPhoto : ImageView
private var binId : String? = null
private lateinit var bin: Bin
private lateinit var likeButton: Button
private lateinit var dislikeButton: Button
private var likeButtonIsCheck: Boolean = false
private var dislikeButtonIsCheck: Boolean = false
private lateinit var user : User
private lateinit var likeButtonDrawable: Drawable
private lateinit var dislikeButtonDrawable: Drawable
private lateinit var addressTextView: TextView
private lateinit var addressTitle: TextView
private lateinit var addressComment: TextView
private lateinit var wasteTitle: TextView
private lateinit var wasteContent: LinearLayout


private const val TAG = "BinActivityDetails"


//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
class BinDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bin_details)

        likeButton = findViewById(R.id.bin_details_activity_like_button)
        dislikeButton = findViewById(R.id.bin_details_activity_dislike_button)
        likeButtonDrawable = DrawableCompat.wrap(likeButton.compoundDrawables[1])
        dislikeButtonDrawable = DrawableCompat.wrap(dislikeButton.compoundDrawables[1])
        addressTextView = findViewById(R.id.bin_details_activity_address_content)
        addressTitle = findViewById(R.id.bin_details_activity_address_title)
        addressComment = findViewById(R.id.bin_details_activity_bin_comment_content)
        wasteTitle = findViewById(R.id.bin_details_activity_bin_waste_title)
        wasteContent = findViewById(R.id.bin_details_activity_bin_waste_content)

        getArgs()
        getBinData()
    }

//--------------------------------------------------------------------------------------------------
// Get Data
//--------------------------------------------------------------------------------------------------

    private fun getArgs(){
        val bundle = intent.extras
        if (bundle != null) {
            binId = bundle.getString(Constants.BIN_ID)
        }
    }

    private fun getBinData(){
        BinRepository().getBinById(binId!!).addOnSuccessListener { mBin ->
            bin = mBin.toObject(Bin::class.java)!!
            getUserData()
        }
    }

    private fun getUserData(){
        UserRepository().getUser(FirebaseAuth.getInstance().currentUser!!.uid)!!.addOnSuccessListener { document ->
            user = document!!.toObject(User::class.java)!!

            configureToolbar()
            setPhotoContent()
            setLikeButton()
            setDislikeButton()
            setDefaultButtonsColor()
            setAddress()
            setBinWasteContent()
        }
    }
//--------------------------------------------------------------------------------------------------
// Toolbar
//--------------------------------------------------------------------------------------------------

   private fun configureToolbar(){
       mToolbar = findViewById(R.id.bin_details_activity_appbar)
       configureBackStack()
       configureTitle()
   }

    private fun configureTitle(){
        mToolbar.title = bin.type}


    /**
     * This method configure toolbar navigation_drawer_background stack
     */
    private fun configureBackStack() {
        setSupportActionBar(mToolbar)
        // Set navigation_drawer_background stack
        val upArrow = ResourcesCompat.getDrawable(this.resources, R.drawable.ic_arrow_back_white_24dp, null)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(upArrow)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

//--------------------------------------------------------------------------------------------------
// Load photo
//--------------------------------------------------------------------------------------------------
    private fun setPhotoContent(){
        binPhoto = findViewById(R.id.bin_details_activity_photo)

        GlideApp.with(applicationContext)
                .load((BinRepository().photoReference(binId!!)))
                .into(binPhoto)
    }

//--------------------------------------------------------------------------------------------------
//  Configure Like / Dislike buttons
//--------------------------------------------------------------------------------------------------

    private fun setLikeButton(){
        likeButton.setOnClickListener {
            if(!likeButtonIsCheck){
                // Update button check data
                likeButtonIsCheck = true
                dislikeButtonIsCheck = false
                // Update bin data
                BinRepository().updateBinLike(binId!!, 1)
                // Update User data
                if(user.likedBinsList == null){ user.likedBinsList = mutableMapOf() }
                user.likedBinsList!![binId!!] = Constants.BIN_LIKE
                UserRepository().updateUserLikedBinsList(user)
                // Update likes buttons colors
                DrawableCompat.setTint(likeButtonDrawable, Color.GREEN)
                DrawableCompat.setTint(dislikeButtonDrawable,  ContextCompat.getColor(applicationContext, R.color.grey) )
            }
        }
    }

    private fun setDislikeButton(){
        dislikeButton.setOnClickListener {
            if (!dislikeButtonIsCheck) {
                // Update button check data
                dislikeButtonIsCheck = true
                likeButtonIsCheck = false
                // Update bin data
                BinRepository().updateBinLike(binId!!, -1)
                // Update User data
                if(user.likedBinsList == null){ user.likedBinsList = mutableMapOf() }
                user.likedBinsList!![binId!!] = Constants.BIN_DISLIKE
                UserRepository().updateUserLikedBinsList(user)
                // Update likes buttons colors
                DrawableCompat.setTint(dislikeButtonDrawable, Color.RED)
                DrawableCompat.setTint(likeButtonDrawable,  ContextCompat.getColor(applicationContext, R.color.grey))
            }
        }
    }

    private fun setDefaultButtonsColor(){
        if (user.likedBinsList != null && user.likedBinsList!!.containsKey(binId.toString())){

            if (user.likedBinsList!!.getValue(binId.toString()) == Constants.BIN_LIKE){
                // Update button check data
                likeButtonIsCheck = true
                dislikeButtonIsCheck = false
                // Update likes buttons colors
                DrawableCompat.setTint(likeButtonDrawable, Color.GREEN)
                DrawableCompat.setTint(dislikeButtonDrawable, ContextCompat.getColor(applicationContext, R.color.grey))
            }else{
                // Update button check data
                dislikeButtonIsCheck = true
                likeButtonIsCheck = false
                // Update likes buttons colors
                DrawableCompat.setTint(dislikeButtonDrawable, Color.RED)
                DrawableCompat.setTint(likeButtonDrawable,  ContextCompat.getColor(applicationContext, R.color.grey))
            }
        }
    }

//--------------------------------------------------------------------------------------------------
// Address
//--------------------------------------------------------------------------------------------------

    private fun setAddress(){
        // Data for Geocode API request
        val formatBinLatLng : String = bin.geoLocation!!.latitude.toString() +","+ bin.geoLocation!!.longitude.toString()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.GEOCODE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val geocodeService = retrofit.create(Service::class.java)
        val requestsGeocode = geocodeService.getLatLngToAddress(formatBinLatLng, BuildConfig.GEOCODING_API_KEY)

        // Execute Geocode API request
        requestsGeocode!!.enqueue(object: Callback<GeocodeResponse?>{
            override fun onResponse(call: Call<GeocodeResponse?>, response: Response<GeocodeResponse?>) {
                if(response.body()!!.status=="OK"){
                    addressTitle.text = resources.getString(R.string.address)
                    addressTextView.text = response.body()!!.results[0].formatted_address
                }
                else{ addressTextView.text = formatBinLatLng
                    addressTitle.text = resources.getString(R.string.coordinates)
                }
            }

            override fun onFailure(call: Call<GeocodeResponse?>, t: Throwable) {
                Log.e(TAG, "geocode request error : " + t.message!! )
                addressTextView.text = formatBinLatLng
                addressTitle.text = resources.getString(R.string.coordinates)
            }
        })
    }

//--------------------------------------------------------------------------------------------------
// Bin waste content
//--------------------------------------------------------------------------------------------------

    private fun setBinWasteContent(){

        BinContentRepository().getBinContentByID(bin.type).addOnSuccessListener { document ->
            val binWaste: BinContent = document.toObject(BinContent::class.java)!!
            addressComment.text = binWaste.comment

            if(binWaste.wastes != null){
                for(waste in binWaste.wastes!!){
                    val textView = TextView(baseContext)
                    textView.text = String.format("%s %s", "- ", waste)
                    wasteContent.addView(textView)
                }
            }
            else{
                wasteTitle.visibility = View.GONE
                wasteContent.visibility = View.GONE
            }
        }
    }
}







