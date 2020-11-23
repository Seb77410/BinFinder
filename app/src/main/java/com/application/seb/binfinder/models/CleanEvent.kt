package com.application.seb.binfinder.models

import com.google.firebase.firestore.GeoPoint

data class CleanEvent(var eventId: String? = "",
                      var createDate: String = "",
                      var createBy_userName: String = "",
                      var createBy_userId: String = "",
                      var eventDate: Int = 0,
                      var participants: MutableList<String>? = null,
                      var description: String = "",
                      var title: String = "",
                      var address: String = "",
                      var geoLocation : GeoPoint? = GeoPoint(0.0,0.0),
                      var comments: MutableList<String>? = mutableListOf(),
                      )