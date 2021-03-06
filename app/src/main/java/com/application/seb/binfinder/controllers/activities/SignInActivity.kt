package com.application.seb.binfinder.controllers.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.application.seb.binfinder.R
import com.application.seb.binfinder.repositories.UserRepository
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "SignInActivity"
private const val RC_SIGN_IN = 1

//--------------------------------------------------------------------------------------------------
// On Create
//--------------------------------------------------------------------------------------------------

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, FirebaseAuth.getInstance().currentUser.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            this.startSignInActivity()
        }

    }

//--------------------------------------------------------------------------------------------------
// Sign In method
//--------------------------------------------------------------------------------------------------

    private fun startSignInActivity() {
        val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
        )

        val builder = AuthUI.getInstance().createSignInIntentBuilder()

        val customLayout = AuthMethodPickerLayout
                .Builder(R.layout.activity_signin_auth)
                .setGoogleButtonId(R.id.sign_in_activity_google_button)
                .setEmailButtonId(R.id.sign_in_activity_mail_button)
                .build()

        startActivityForResult(
                builder.setAuthMethodPickerLayout(customLayout)
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setTheme(R.style.firebaseTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN)
    }

//--------------------------------------------------------------------------------------------------
// Sign In result
//--------------------------------------------------------------------------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                createUser()
            } else when {
                response == null -> {
                    Log.e(TAG, "Error : Auth cancel")
                    startSignInActivity()
                }
                response.error!!.errorCode == ErrorCodes.NO_NETWORK -> {
                    Log.e(TAG, "Error : internet is OFF")
                    startSignInActivity()
                }
                response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR -> {
                    Log.e(TAG, "Unknown error")
                    startSignInActivity()
                }
            }
        } else {
            startSignInActivity()
        }
    }

//--------------------------------------------------------------------------------------------------
// Create User
//--------------------------------------------------------------------------------------------------

    private fun createUser(){
        val userRepository = UserRepository()
        userRepository.getUser(FirebaseAuth.getInstance().currentUser!!.uid)!!
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val document = task.result
                        if (document == null) {Log.e(TAG, "User document is NULL")}
                        else {
                            Log.d(TAG, "User document is NOT NULL")
                            if (document.exists()) {
                                Log.d(TAG, "User document already exist")
                                startMainActivity()
                            }
                            else {
                                Log.d(TAG, "User CREATE")
                                userRepository
                                        .createUser(FirebaseAuth.getInstance().currentUser!!.uid,
                                            FirebaseAuth.getInstance().currentUser!!.displayName!!,
                                            FirebaseAuth.getInstance().currentUser?.photoUrl.toString() )!!
                                        .addOnSuccessListener {
                                            startMainActivity()
                                        }
                            }
                        }
                    }

                    else {
                        Log.e(TAG, "ERROR to get currentUser")
                    }
                }
    }


//--------------------------------------------------------------------------------------------------
// Intent
//--------------------------------------------------------------------------------------------------
    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}

