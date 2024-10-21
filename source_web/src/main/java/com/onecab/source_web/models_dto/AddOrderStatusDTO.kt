package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class AddOrderStatusDTO(
    @SerializedName("order_id")          val order_id:       String = "",
    @SerializedName("delivery_date")     val delivery_date:  String = "", //дата д/б в таком формате 20240912070000
    @SerializedName("delivery_note")     val delivery_note:  String = "",
    @SerializedName("status")            val status:         String = "",
)
