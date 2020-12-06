package com.application.seb.binfinder.controllers.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
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
import com.application.seb.binfinder.controllers.activities.BinDetailsActivity
import com.application.seb.binfinder.controllers.activities.CleanEventDetailsActivity
import com.application.seb.binfinder.models.Bin
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.repositories.BinRepository
import com.application.seb.binfinder.repositories.CleanEventRepository
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.GeoPoint
import java.util.*

private const val TAG = "MapFragment"
private const val LOCATION_PERMISSION_CODE = 1
private const val PARAM = "binType"

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


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
    private lateinit var fabCleanEvent: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabContainer : LinearLayout
    private lateinit var constraintLayout : ConstraintLayout
    private lateinit var userLocation : GeoPoint
    private var locationsList : MutableList<Any>? = mutableListOf()
    private var markersList : MutableList<Marker>? = mutableListOf()


    interface OnFragmentInteractionListener {
        fun onFragmentSetUserLocation(userLocation: GeoPoint)
    }

//--------------------------------------------------------------------------------------------------
// Constructor
//--------------------------------------------------------------------------------------------------
    companion object {
        @JvmStatic
        fun newInstance(param1: String?) =
                MapFragment().apply {
                    arguments = Bundle().apply {
                        putString(PARAM, param1)
                    }
                }
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
        fabCleanEvent = rootView.findViewById(R.id.map_fragment_fab_clean_events)
        fabContainer = rootView.findViewById(R.id.map_fragment_fab_container)
        fabAdd = rootView.findViewById(R.id.map_fragment_fab_add)
        constraintLayout = rootView.findViewById(R.id.map_fragment_constraint_layout)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        syncMap()
        configureFabMenu()
        configureFabAdd()
        configureBinsFabClick()

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
        Log.d(TAG, "OnMapReady")
        googleMap = p0

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ask for location permission
            Log.e(TAG, "Location Permission denied")
            return requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)

        }
        else{
            Log.d(TAG, "Location Permission allowed")
            googleMap!!.isMyLocationEnabled = true
            googleMap!!.uiSettings.isZoomControlsEnabled = true
            googleMap!!.setOnMarkerClickListener(this)

            updateUserLocation(toStartAddBinFragment = false, toInitialiseView = true)
        }
    }

//--------------------------------------------------------------------------------------------------
// Get User location
//--------------------------------------------------------------------------------------------------
    private fun updateUserLocation(toStartAddBinFragment: Boolean, toInitialiseView: Boolean) {

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient!!.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            Log.d(TAG, "User Location = " + location.latitude.toString() + " , " + location.longitude)
                            // Send user location to MainActivity
                            userLocation = GeoPoint(location.latitude, location.longitude)
                            // Move camera to current position
                            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 12f))
                            if(toStartAddBinFragment){ (Objects.requireNonNull(activity) as OnFragmentInteractionListener).onFragmentSetUserLocation(userLocation) }
                            if (toInitialiseView){showSavedLocation() }

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
                    Log.d(TAG, "Location permission granted")
                    googleMap!!.isMyLocationEnabled = true
                    googleMap!!.uiSettings.isZoomControlsEnabled = true
                    googleMap!!.setOnMarkerClickListener(this)

                    updateUserLocation(toStartAddBinFragment = false, toInitialiseView = true)
                }
                else{
                    Log.e(TAG, "Location permission denied")
                }
            }
        }
    }


//--------------------------------------------------------------------------------------------------
// AlertDialog
//--------------------------------------------------------------------------------------------------

    private fun showAlertDialog(){
        updateUserLocation(toStartAddBinFragment = false, toInitialiseView = false)
        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder
                .setTitle(getString(R.string.alert_dialog_add_bin_title))
                .setMessage(getString(R.string.alert_dialog_add_bin_content))
                .setPositiveButton(getString(R.string.alert_dialog_yes_button)) { _: DialogInterface, _: Int ->
                    Log.d(TAG, "Alert dialog - click YES button")
                    updateUserLocation(toStartAddBinFragment = true, toInitialiseView = false)
                }
                .setNegativeButton(getString(R.string.alert_dialog_no_button)) { _: DialogInterface, _: Int ->
                    Log.e(TAG, "Alert dialog - click NO button")
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
        fabAdd.setOnClickListener {showAlertDialog()}
    }

    private fun configureBinsFabClick(){
        fabGlass.setOnClickListener {getSelectedBinsList(getString(R.string.glass))}
        fabGreen.setOnClickListener {getSelectedBinsList(getString(R.string.green_waste))}
        fabHousehold.setOnClickListener {getSelectedBinsList(getString(R.string.household_wast))}
        fabPlastic.setOnClickListener {getSelectedBinsList(getString(R.string.recycling_bin))}
        fabCleanEvent.setOnClickListener {getCleanEventsListList()}
        fabRecyclingCenter.setOnClickListener {getSelectedBinsList(getString(R.string.recycling_center))}
    }

//--------------------------------------------------------------------------------------------------
// Marker
//--------------------------------------------------------------------------------------------------

    private fun getSelectedBinsList(type: String){
        val binRepository = BinRepository()

        binRepository.getBinByDistance(userLocation.latitude, userLocation.longitude ,type).addSnapshotListener { _, mutableList, mutableList2 ->
            Log.d(TAG, "Bins found = ${mutableList.size} and list2 = ${mutableList2.size} for type = $type")
            // Clear data
            locationsList!!.clear()
            removeAllMarker()
            // Add marker for asked bins
            for (document in mutableList){
                val bin = document.toObject(Bin::class.java)
                bin!!.binId = document.id
                locationsList!!.add(bin)
                showMarker()
                }
            }
    }

    private fun getCleanEventsListList(){
        val cleanEventRepository = CleanEventRepository()

        cleanEventRepository.getCleanEventsByDistance(userLocation.latitude, userLocation.longitude).addSnapshotListener { _, mutableList, mutableList2 ->
            Log.d(TAG, "Clean Events found = ${mutableList.size} and list2 = ${mutableList2.size} for type Clean Events")
            // Clear data
            locationsList!!.clear()
            removeAllMarker()
            // Add marker for asked bins
            for (document in mutableList){
                val cleanEvent = document.toObject(CleanEvent::class.java)
                cleanEvent!!.eventId = document.id
                locationsList!!.add(cleanEvent)
                showMarker()
            }
        }
    }


    private fun showMarker(){
        for(location in locationsList!!){
            when(location){
                is Bin -> {
                    // Set marker options
                    val latLng = LatLng(location.geoLocation!!.latitude ,location.geoLocation!!.longitude)
                    val markerOptions = MarkerOptions().position(latLng)
                    val mMarker: Marker = googleMap!!.addMarker(markerOptions)
                    mMarker.title = location.type
                    mMarker.tag = location.binId
                    markersList!!.add(mMarker)
                    Log.d(TAG, "showMarker() for Bin id = " + location.binId)
                }
                is CleanEvent -> {
                    // Set marker options
                    val latLng = LatLng(location.geoLocation!!.latitude ,location.geoLocation!!.longitude)
                    val markerOptions = MarkerOptions().position(latLng)
                    val mMarker: Marker = googleMap!!.addMarker(markerOptions)
                    mMarker.title = "Clean Event"
                    mMarker.tag = location.eventId
                    markersList!!.add(mMarker)
                    Log.d(TAG, "showMarker() for Event id = " + location.eventId)
                }
            }




        }
    }

    private fun removeAllMarker(){
        for(marker in markersList!!){marker.remove()}
        markersList!!.clear()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        Log.d(TAG, "marker tag = " + p0!!.tag.toString())
        if (p0.tag != null) {
            if(p0.title == "Clean Event") {
                // Set marker place Id into string value
                val intent = Intent(activity, CleanEventDetailsActivity::class.java)
                CleanEventRepository().getCleanEventById(p0.tag.toString())
                        .addOnSuccessListener {
                            val cleanEvent = it.toObject(CleanEvent::class.java)
                            intent.putExtra(Constants.ARGS_EVENT, Utils.convertCleanEventToString(cleanEvent!!))
                            startActivity(intent)
                        }
            }
            else{
                // Set marker place Id into string value
                val intent = Intent(activity, BinDetailsActivity::class.java)
                intent.putExtra(Constants.BIN_ID, p0.tag.toString())
                startActivity(intent)
            }
        }
        return true
    }


//--------------------------------------------------------------------------------------------------
// Default view
//--------------------------------------------------------------------------------------------------

    private fun showSavedLocation(){
        val binType = getArgs()
        if(!binType.isNullOrEmpty()){getSelectedBinsList(binType)}
        else{getSelectedBinsList(getString(R.string.household_wast))}
    }

    private fun getArgs():String?{
    var binType : String? = null
        arguments?.let {
            binType = it.getString(PARAM)
            Log.d(TAG, " Argument $PARAM = $binType")
        }
        return binType
    }
}