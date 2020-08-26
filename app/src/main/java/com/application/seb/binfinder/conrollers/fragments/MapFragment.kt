package com.application.seb.binfinder.conrollers.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

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
    private lateinit var fabContainer : LinearLayout

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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        configureFabMenu()

        // Configure view
        try {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this@MapFragment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rootView
    }


//--------------------------------------------------------------------------------------------------
// OnMapReady
//--------------------------------------------------------------------------------------------------
    override fun onMapReady(p0: GoogleMap?) {
        Log.e("MapFragment", "OnMapReady")

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ask for location permission
            Log.e("MapFragment", "Location Permission denied")
            return requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1)

        }
        else{
            Log.e("MapFragment", "Location Permission allowed")
            googleMap = p0
            googleMap!!.isMyLocationEnabled = true
            googleMap!!.setOnMapLongClickListener(this)
            googleMap!!.uiSettings.isZoomControlsEnabled = true

            updateUserLocation()
        }


    }

//--------------------------------------------------------------------------------------------------
// Get User location
//--------------------------------------------------------------------------------------------------
    private fun updateUserLocation() {

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient!!.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("User Location ", location.latitude.toString() + " , " + location.longitude)
                            // Send user location to MainActivity
                            val latLng = LatLng(location.latitude, location.longitude)
                            // Move camera to current position
                            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
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

        if(requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap!!.isMyLocationEnabled = true
                    googleMap!!.setOnMapLongClickListener(this)
                    googleMap!!.uiSettings.isZoomControlsEnabled = true
                    updateUserLocation()
                }
            }
        }
    }



//--------------------------------------------------------------------------------------------------
// Map Long Click
//--------------------------------------------------------------------------------------------------

    override fun onMapLongClick(p0: LatLng?) {
        Log.e("MapFragment", "Long touch on lat : " + p0!!.latitude + " long : " + p0.longitude)
        showAlertDialog()
    }

//--------------------------------------------------------------------------------------------------
// AlertDialog
//--------------------------------------------------------------------------------------------------

    private fun showAlertDialog(){
        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder
                .setTitle("Add bin place")
                .setMessage("Are you sure you want to add a bin location ?")
                .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    Log.e("MapFragment", "Alert dialog click YES button")
                    // TODO : start ADD BIN ACTIVITY
                }
                .setNegativeButton("No") { _: DialogInterface, _: Int ->
                    Log.e("MapFragment", "Alert dialog click NO button")
                }
                .show()
    }

//--------------------------------------------------------------------------------------------------
// Configure FAB
//--------------------------------------------------------------------------------------------------

    private fun configureFabMenu(){
        fabMenu.setOnClickListener {
            if (fabContainer.visibility == View.VISIBLE) {
                fabContainer.visibility = View.GONE
                fabMenu.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_filter_vertical))
            } else {
                fabContainer.visibility = View.VISIBLE
                fabMenu.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_filter_black_48dp))
            }
        }
    }
}