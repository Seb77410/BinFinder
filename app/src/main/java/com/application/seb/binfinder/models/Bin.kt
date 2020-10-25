package com.application.seb.binfinder.models

import com.google.firebase.firestore.GeoPoint


data class Bin(var binId : String? = "",
               var type : String = "",
               var geoLocation : GeoPoint? = GeoPoint(0.0,0.0),
               var addBy_userID : String = "",
               var addBy_userName: String = "",
               var like: Int = 0,
               var dislike: Int = 0
)
