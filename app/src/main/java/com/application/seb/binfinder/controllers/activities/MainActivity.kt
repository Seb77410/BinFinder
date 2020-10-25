package com.application.seb.binfinder.controllers.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.application.seb.binfinder.controllers.fragments.MapFragment
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.fragments.AddBinFragment
import com.application.seb.binfinder.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.GeoPoint

//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
private lateinit var mapButton: FloatingActionButton
private lateinit var currentFragment: Fragment

//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity(), MapFragment.OnFragmentInteractionListener, AddBinFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton = findViewById(R.id.activity_main_map_button)

        configureMapButton()
        showMapFragment(null)
    }


//--------------------------------------------------------------------------------------------------
// Show fragment
//--------------------------------------------------------------------------------------------------
    private fun showMapFragment(binType: String?){
        Log.d("ActivityMain", "showMapFragment()")
        currentFragment = MapFragment.newInstance(binType)
        // Update fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_frameLayout, currentFragment)
                .commit()
    }

    private fun showAddBinFragment(location: GeoPoint){
        Log.d("ActivityMain", "showAddBinFragment()")
        currentFragment = AddBinFragment.newInstance(Utils.convertGeoPointToString(location))
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
            showMapFragment(null)
        }
    }

    override fun onFragmentSetUserLocation(userLocation: GeoPoint) {
        showAddBinFragment(userLocation)
    }

    override fun binSaved(binType: String) {
        showMapFragment(binType)
    }

}