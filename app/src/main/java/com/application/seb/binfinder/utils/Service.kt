package com.application.seb.binfinder.utils

import com.application.seb.binfinder.models.GeocodeResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

//--------------------------------------------------------------------------------------------------
// Geocode request
//--------------------------------------------------------------------------------------------------

    @GET(Constants.GEOCODE_JSON_FORMAT)
    fun getLatLngToAddress(
            @Query(Constants.GEOCODE_LATLNG_QUERY) latLng: String?,
            @Query(Constants.GEOCODE_KEY_QUERY) apiKey: String?)
            : Call<GeocodeResponse?>?

}