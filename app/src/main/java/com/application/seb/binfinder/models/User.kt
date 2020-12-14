package com.application.seb.binfinder.models


data class User(val userId: String = "",
                var userName: String = "",
                var photo: String? = null,
                var likedBinsList: MutableMap<String, String>? = mutableMapOf()
) {}