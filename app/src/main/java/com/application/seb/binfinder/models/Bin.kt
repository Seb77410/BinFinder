package com.application.seb.binfinder.models

import com.google.android.gms.maps.model.LatLng

class Bin(val binId : String?, var type : String, var location : LatLng, var addBy_userID : String, var addBy_userName: String)