package com.onecab.source_web.payment_dto

import com.google.gson.annotations.SerializedName


data class StatusQrdDTO (

    @SerializedName("Payment_Status"  ) var PaymentStatus : String? = null,
    @SerializedName("Payment_Id_Bank" ) var PaymentIdBank : String? = null,
    @SerializedName("TimeStamp"       ) var TimeStamp     : String? = null,
    @SerializedName("Payer_Id_SBP"    ) var PayerIdSBP    : String? = null

)