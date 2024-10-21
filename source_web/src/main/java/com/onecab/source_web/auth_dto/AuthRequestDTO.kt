package com.onecab.source_web.auth_dto

import com.google.gson.annotations.SerializedName

data class AuthRequestDTO(
    @SerializedName("login")     val login:      String,
    @SerializedName("hash_sum")  val hashSum:    String
)
