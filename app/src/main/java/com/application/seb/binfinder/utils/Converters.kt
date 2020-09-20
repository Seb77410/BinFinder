package com.application.seb.binfinder.utils

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    companion object {

        @JvmStatic
        fun convertLatLngToString(location: LatLng): String {
            val gSon = Gson()
            return gSon.toJson(location)
        }

        @JvmStatic
        fun convertStringToLatLng(location: String): LatLng{
            val propertyType = object : TypeToken<LatLng>() {}.type
            return Gson().fromJson(location, propertyType)

        }
    }
}