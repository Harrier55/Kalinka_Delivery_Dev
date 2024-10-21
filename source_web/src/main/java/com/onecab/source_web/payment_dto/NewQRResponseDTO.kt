package com.onecab.source_web.payment_dto

import com.google.gson.annotations.SerializedName

data class NewQRResponseDTO(
    @SerializedName("QRD_ID_SBP" ) var QRDIDSBP : String? = null,
    @SerializedName("Payload"    ) var Payload  : String? = null,
    @SerializedName("image"      ) var image    : ImageQr?  = ImageQr()
)

data class ImageQr (
    @SerializedName("content" ) var content : String? = null
)