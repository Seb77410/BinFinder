package com.application.seb.binfinder

import android.util.Log
import com.application.seb.binfinder.utils.Converters
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UnitTests {

    @Test
    fun latLngConvertersTest() {
        val location = LatLng(0.0,0.0)
        val gSon = Gson()
        val sLocation = gSon.toJson(location)

        Assert.assertEquals(sLocation, Converters.convertLatLngToString(location))
        Assert.assertEquals(location, Converters.convertStringToLatLng(sLocation))

    }

}