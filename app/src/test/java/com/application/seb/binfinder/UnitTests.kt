package com.application.seb.binfinder

import com.application.seb.binfinder.utils.Utils
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

class UnitTests {

    @Test
    fun latLngConvertersTest() {
        val location = LatLng(0.0,0.0)
        val gSon = Gson()
        val sLocation = gSon.toJson(location)

        Assert.assertEquals(sLocation, Utils.convertLatLngToString(location))
        Assert.assertEquals(location, Utils.convertStringToLatLng(sLocation))

    }



}