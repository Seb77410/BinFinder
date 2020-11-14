package com.application.seb.binfinder.models

data class CleanEvent(var eventId: String? = "",
                      var createDate: String = "",
                      var createBy_userName: String = "",
                      var createBy_userId: String = "",
                      var eventDate: Int = 0,
                      var participants: MutableList<String>? = null,
                      var description: String = "",
                      var title: String = "",
                      var address: String = "",
                      var comments: MutableList<String>? = mutableListOf()
                      )