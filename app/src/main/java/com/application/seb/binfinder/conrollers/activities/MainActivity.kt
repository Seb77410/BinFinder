package com.application.seb.binfinder.conrollers.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.application.seb.binfinder.conrollers.fragments.MapFragment
import com.application.seb.binfinder.R
import com.application.seb.binfinder.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//--------------------------------------------------------------------------------------------------
// For data
//--------------------------------------------------------------------------------------------------
val db = FirebaseFirestore.getInstance()


//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createUserAndConfigureView()
        showFragmentMap()

    }

//--------------------------------------------------------------------------------------------------
// Create User and Configure View
//--------------------------------------------------------------------------------------------------

    private fun createUserAndConfigureView(){
        val user = User(FirebaseAuth.getInstance().currentUser!!.uid,
                FirebaseAuth.getInstance().currentUser!!.displayName!!,
                FirebaseAuth.getInstance().currentUser?.photoUrl)

        FirebaseFirestore.getInstance().collection("Users").document(user.userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var document = task.result

                if (document == null) {
                    Log.e("SignIn", "User document is NULL")
                }
                else {
                    Log.d("SignIn", "User document is NOT NULL")
                    if (document.exists()) {
                        // Configure view
                        // TODO : configureView
                        Log.d("SignIn", "User document already exist")
                    }
                    else {
                        saveUser(user)
                        Log.d("SignIn", "User CREATE")
                    }
                }
            }
            else {
                Log.e("SignIn", "User NOT CREATE")
            }
        }
    }


    private fun saveUser(user: User){
        db.collection("Users").document(user.userId).set(user).addOnSuccessListener {
            Log.e("MainActivity", "Utilisateur bien enregirté")
            }
    }

//--------------------------------------------------------------------------------------------------
// Show fragment
//--------------------------------------------------------------------------------------------------

    private fun showFragmentMap(){
        Log.e("ActivityMain", "showFragment()")
        var map = MapFragment()
        // Update fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_frameLayout, map)
                .commit()

    }
}