package com.application.seb.binfinder.models

import com.google.gson.annotations.SerializedName

data class GeocodeResponse (
        @SerializedName("plus_code") val plus_code : PlusCode,
        @SerializedName("results") val results : List<Results>,
        @SerializedName("status") val status : String
)

data class Results (

        @SerializedName("address_components") val address_components : List<AddressComponents>,
        @SerializedName("formatted_address") val formatted_address : String,
        @SerializedName("geometry") val geometry : Geometry,
        @SerializedName("place_id") val place_id : String,
        @SerializedName("plus_code") val plus_code : PlusCode,
        @SerializedName("types") val types : List<String>
)

data class Geometry (

        @SerializedName("location") val location : Location,
        @SerializedName("location_type") val location_type : String,
        @SerializedName("viewport") val viewport : Viewport
)

data class Location (

        @SerializedName("lat") val lat : Double,
        @SerializedName("lng") val lng : Double
)

data class Northeast (

        @SerializedName("lat") val lat : Double,
        @SerializedName("lng") val lng : Double
)

data class PlusCode (

        @SerializedName("compound_code") val compound_code : String,
        @SerializedName("global_code") val global_code : String
)

data class Southwest (

        @SerializedName("lat") val lat : Double,
        @SerializedName("lng") val lng : Double
)

data class Viewport (

        @SerializedName("northeast") val northeast : Northeast,
        @SerializedName("southwest") val southwest : Southwest
)

data class AddressComponents (

        @SerializedName("long_name") val long_name : String,
        @SerializedName("short_name") val short_name : String,
        @SerializedName("types") val types : List<String>
)