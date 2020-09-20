package com.application.seb.binfinder.conrollers.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.application.seb.binfinder.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private const val LOCATION_PERMISSION_CODE = 1


class MapFragment : Fragment(), OnMapReadyCallback {


//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
    private var googleMap : GoogleMap? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fabGlass: FloatingActionButton
    private lateinit var fabHousehold: FloatingActionButton
    private lateinit var fabGreen: FloatingActionButton
    private lateinit var fabPlastic: FloatingActionButton
    private lateinit var fabRecyclingCenter: FloatingActionButton
    private lateinit var fabLandfill: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabContainer : LinearLayout
    private lateinit var constraintLayout : ConstraintLayout
    private lateinit var userLocation : LatLng

    interface OnFragmentInteractionListener {
        fun onFragmentSetUserLocation(userLocation: LatLng?)
    }


//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        fabMenu = rootView.findViewById(R.id.map_fragment_fab_menu)
        fabGlass = rootView.findViewById(R.id.map_fragment_fab_glass)
        fabHousehold = rootView.findViewById(R.id.map_fragment_fab_household_waste)
        fabGreen = rootView.findViewById(R.id.map_fragment_fab_green_waste)
        fabPlastic = rootView.findViewById(R.id.map_fragment_fab_plastic)
        fabRecyclingCenter = rootView.findViewById(R.id.map_fragment_fab_recycling_center)
        fabLandfill = rootView.findViewById(R.id.map_fragment_fab_landfill)
        fabContainer = rootView.findViewById(R.id.map_fragment_fab_container)
        fabAdd = rootView.findViewById(R.id.map_fragment_fab_add)
        constraintLayout = rootView.findViewById(R.id.map_fragment_constraint_layout)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        syncMap()
        configureFabMenu()
        configureFabAdd()

        return rootView
    }


//--------------------------------------------------------------------------------------------------
// Synchronise map with fragment
//--------------------------------------------------------------------------------------------------
    private fun syncMap(){
        try {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this@MapFragment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//--------------------------------------------------------------------------------------------------
// OnMapReady
//--------------------------------------------------------------------------------------------------
    override fun onMapReady(p0: GoogleMap?) {
        Log.d("MapFragment", "OnMapReady")
        googleMap = p0

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ask for location permission
            Log.e("MapFragment", "Location Permission denied")
            return requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)

        }
        else{
            Log.d("MapFragment", "Location Permission allowed")
            googleMap!!.isMyLocationEnabled = true
            googleMap!!.uiSettings.isZoomControlsEnabled = true
            updateUserLocation(false)
        }
    }

//--------------------------------------------------------------------------------------------------
// Get User location
//--------------------------------------------------------------------------------------------------
    private fun updateUserLocation(toStartAddBinFragment: Boolean) {

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient!!.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("User Location ", location.latitude.toString() + " , " + location.longitude)
                            // Send user location to MainActivity
                            userLocation = LatLng(location.latitude, location.longitude)
                            // Move camera to current position
                            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f))
                            if(toStartAddBinFragment){
                                (Objects.requireNonNull(activity) as OnFragmentInteractionListener).onFragmentSetUserLocation(userLocation)

                            }
                        }
                    }
            return
        }
    }



//--------------------------------------------------------------------------------------------------
// Permission ask result
//--------------------------------------------------------------------------------------------------
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MapFragment", "Location permission granted" )
                    googleMap!!.isMyLocationEnabled = true
                    googleMap!!.uiSettings.isZoomControlsEnabled = true
                    updateUserLocation(false)
                }
                else{
                    Log.e("MapFragment", "Location permission denied" )
                }
            }
        }
    }


//--------------------------------------------------------------------------------------------------
// AlertDialog
//--------------------------------------------------------------------------------------------------

    private fun showAlertDialog(){
        updateUserLocation(false)
        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder
                .setTitle(getString(R.string.alert_dialog_add_bin_title))
                .setMessage(getString(R.string.alert_dialog_add_bin_content))
                .setPositiveButton(getString(R.string.alert_dialog_yes_button)) { _: DialogInterface, _: Int ->
                    Log.d("MapFragment", "Alert dialog - click YES button")
                    updateUserLocation(true)
                }
                .setNegativeButton(getString(R.string.alert_dialog_no_button)) { _: DialogInterface, _: Int ->
                    Log.e("MapFragment", "Alert dialog - click NO button")
                }
        val dialogCard: AlertDialog = dialogBuilder.create()
        dialogCard.window!!.setGravity(Gravity.TOP)
        dialogCard.show()
    }

//--------------------------------------------------------------------------------------------------
// Configure FAB
//--------------------------------------------------------------------------------------------------

    private fun configureFabMenu(){
        fabMenu.setOnClickListener {
            if (fabContainer.visibility == View.VISIBLE) {
                fabContainer.visibility = View.GONE
                fabAdd.visibility = View.VISIBLE
                fabMenu.rotation = 90F
            } else {
                fabContainer.visibility = View.VISIBLE
                fabAdd.visibility = View.GONE
                fabMenu.rotation = 0F
            }
        }
    }

    private fun configureFabAdd(){
        fabAdd.setOnClickListener {
            showAlertDialog()
        }
    }

//--------------------------------------------------------------------------------------------------
// Show marker
//--------------------------------------------------------------------------------------------------

    fun showMarker(type : String){



    }

}