package com.application.seb.binfinder

import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.utils.Utils
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test
import java.util.*

class UtilsTests {

    @Test
    fun latLngConvertersTest() {
        val location = LatLng(0.0,0.0)
        val gSon = Gson()
        val sLocation = gSon.toJson(location)

        Assert.assertEquals(sLocation, Utils.convertLatLngToString(location))
        Assert.assertEquals(location, Utils.convertStringToLatLng(sLocation))
    }

    @Test
    fun geoPointConvertersTest() {

        val geoPoint = GeoPoint(0.0,0.0)
        val gSon = Gson()
        val sLocation = gSon.toJson(geoPoint)

        Assert.assertEquals(sLocation, Utils.convertGeoPointToString(geoPoint))
        Assert.assertEquals(geoPoint, Utils.convertStringToGeoPoint(sLocation))

    }

    @Test
    fun cleanEventConvertersTest() {

        val event = CleanEvent("1",
                "20201110",
                "Seb",
                "2",
                20201231,
                null,
                "description",
                "title",
                "address",
                GeoPoint(0.0, 0.0),
                mutableListOf()
        )
        val gSon = Gson()
        val sEvent = gSon.toJson(event)

        Assert.assertEquals(sEvent, Utils.convertCleanEventToString(event))
        Assert.assertEquals(event, Utils.convertStringToCleanEvent(sEvent))

    }

    @Test
    fun convertCalendarToFormatStringTest(){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2020)
        calendar.set(Calendar.MONTH, 11)
        calendar.set(Calendar.DAY_OF_MONTH, 15)
        val sCalendar = Utils.convertCalendarToFormatString(calendar)

        Assert.assertEquals(sCalendar, "20201215")
    }

    @Test
    fun convertDateIntToStringTest(){
        val date = 8
        val date2 = 25
        val sDate = Utils.convertDateIntToString(date)
        val sDate2 = Utils.convertDateIntToString(date2)

        Assert.assertEquals(sDate, "08")
        Assert.assertEquals(sDate2, "25")
    }

    @Test
    fun convertDataDateToFormatString(){
        val date = 20201125
        val sDate = Utils.convertDataDateToFormatString(date)

        Assert.assertEquals(sDate, "25 / 11 / 2020")

    }


}