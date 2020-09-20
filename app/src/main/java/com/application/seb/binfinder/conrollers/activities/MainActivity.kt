package com.application.seb.binfinder.conrollers.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.application.seb.binfinder.conrollers.fragments.MapFragment
import com.application.seb.binfinder.R
import com.application.seb.binfinder.conrollers.fragments.AddBinFragment
import com.application.seb.binfinder.models.User
import com.application.seb.binfinder.utils.Converters
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
private lateinit var mapButton: FloatingActionButton
private lateinit var currentFragment: Fragment
private var viewModel = MainActivityViewModel()

//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity(), MapFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton = findViewById(R.id.activity_main_map_button)

        createUserAndConfigureView()

    }

//--------------------------------------------------------------------------------------------------
// Create User and Configure View
//--------------------------------------------------------------------------------------------------
    private fun createUserAndConfigureView(){
        val user = User(FirebaseAuth.getInstance().currentUser!!.uid,
                FirebaseAuth.getInstance().currentUser!!.displayName!!,
                FirebaseAuth.getInstance().currentUser?.photoUrl)

    viewModel.getUser(user.userId)!!
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result

                if (document == null) {
                    Log.e("SignIn", "User document is NULL")
                }
                else {
                    Log.d("SignIn", "User document is NOT NULL")
                    if (document.exists()) {
                        // Configure view
                        configureMapButton()
                        showMapFragment()
                        Log.d("SignIn", "User document already exist")
                    }
                    else {
                        viewModel.saveUser(user)
                        configureMapButton()
                        showMapFragment()
                        Log.d("SignIn", "User CREATE")
                    }
                }
            }
            else {
                Log.e("SignIn", "User NOT CREATE")
            }
        }
    }


//--------------------------------------------------------------------------------------------------
// Show fragment
//--------------------------------------------------------------------------------------------------
    private fun showMapFragment(){
        Log.d("ActivityMain", "showMapFragment()")
        currentFragment = MapFragment()
        // Update fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_frameLayout, currentFragment)
                .commit()
    }

    private fun showAddBinFragment(location: LatLng){
        Log.d("ActivityMain", "showAddBinFragment()")
        currentFragment = AddBinFragment.newInstance(Converters.convertLatLngToString(location))
        // Update fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_frameLayout, currentFragment)
                .commit()
    }

//--------------------------------------------------------------------------------------------------
// Map button
//--------------------------------------------------------------------------------------------------
    private fun configureMapButton(){
        mapButton.setOnClickListener{
            showMapFragment()
        }
    }

    override fun onFragmentSetUserLocation(userLocation: LatLng?) {
        showAddBinFragment(userLocation!!)
    }

}