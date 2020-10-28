package com.application.seb.binfinder.controllers.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.fragments.AddBinFragment
import com.application.seb.binfinder.controllers.fragments.MapFragment
import com.application.seb.binfinder.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import java.util.*

//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
private lateinit var mapButton: FloatingActionButton
private lateinit var currentFragment: Fragment
private lateinit var navigationView:NavigationView
private lateinit var drawerLayout: DrawerLayout
private lateinit var appBar: BottomAppBar
private lateinit var drawerUserName : TextView
private lateinit var drawerUserPhoto : ImageView
private lateinit var drawerUserEmail : TextView

//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity(), MapFragment.OnFragmentInteractionListener, AddBinFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton = findViewById(R.id.activity_main_map_button)
        navigationView = findViewById(R.id.activity_main_nav_view)
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        appBar = findViewById(R.id.bottomAppBar)
        drawerUserName = navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_name)
        drawerUserPhoto = navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_photo)
        drawerUserEmail= navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_email)


        configureMapButton()
        showMapFragment(null)
        configureDrawerLayout()
        onclickMenu()
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


//--------------------------------------------------------------------------------------------------
// For Navigation Drawer
//--------------------------------------------------------------------------------------------------
    private fun setDrawerUserInfos() {

    // Set user name
    val userName = Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!.displayName
    drawerUserName.text = userName

    // Set user email
    val userEmail = FirebaseAuth.getInstance().currentUser!!.email
    drawerUserEmail.text = userEmail

    // Set user photo
    val uri = FirebaseAuth.getInstance().currentUser!!.photoUrl
    if (uri != null) {
        val userPhotoUrl = uri.toString()
        Log.d("Drawer menu", "User photo url : $userPhotoUrl")
        Glide
                .with(applicationContext)
                .load(userPhotoUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(drawerUserPhoto)
    } else {
        Glide
                .with(applicationContext)
                .load(R.drawable.no_image)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(drawerUserPhoto)
        }

    }


    /**
     * This method configure Drawer Layout and add burger button on the left of toolbar
     */
    private fun configureDrawerLayout() {
        // Glue drawer menu to MainActivity toolbar
        val toggle = ActionBarDrawerToggle(this, drawerLayout, appBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        // Add listener to the menu drawer
        drawerLayout.addDrawerListener(toggle)
        // Add animation on drawer menu button when Open/close
        toggle.syncState()
        // Allow user tu click on Menu drawer item button
        navigationView.setNavigationItemSelectedListener(this)
        // Show user data
        setDrawerUserInfos()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activity_main_drawer_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }

            R.id.activity_main_drawer_my_clean_events -> {
                Log.e("main activity", "click button clean event")
                // TODO : start clean event fragment
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

//--------------------------------------------------------------------------------------------------
// Menu Inflate
//--------------------------------------------------------------------------------------------------

    private fun onclickMenu() {
        appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_main_clean_events -> {
                    Log.e("MainActivity", "click on button clean event")
                    // TODO : start clean event fragment
                    true
                }
                else -> false
            }
        }
    }
}