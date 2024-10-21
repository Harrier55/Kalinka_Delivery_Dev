package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class AddOrderPayDTO(
    @SerializedName("order_id")          val order_id:           String = "",
    @SerializedName("pay_date")          val pay_date:           String = "",  // дата д/б в таком формате 20240912070000
    @SerializedName("pay_amount")        val pay_amount:         Double = 0.0,
    @SerializedName("check_destination") val check_destination:  String = "",
    @SerializedName("check_way")         val check_way:          String = "",
    @SerializedName("pay_api_info")      val pay_api_info:       String = "",
    @SerializedName("pay_sr_info")       val pay_sr_info:        String = "",
    @SerializedName("adjustments_data")  val adjustments_data:   String = "",
)
