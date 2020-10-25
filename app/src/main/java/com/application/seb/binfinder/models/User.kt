package com.application.seb.binfinder.models

import android.net.Uri

data class User(val userId: String = "",
                var userName: String = "",
                var photo: Uri? = null,
                var likedBinsList: MutableMap<String, String>? = mutableMapOf()
) {}