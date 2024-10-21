package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class AddOrderPayRequestDTO (

    @SerializedName("order_id"         ) var orderId         : String? = null,
    @SerializedName("operation_date"   ) var operationDate   : String? = null,
    @SerializedName("operation_amount" ) var operationAmount : Double? = null,
    @SerializedName("qrd_id_sbp"       ) var qrdIdSbp        : String? = null,
    @SerializedName("sbp_request"      ) var sbpRequest      : String? = null,
    @SerializedName("sbp_result"       ) var sbpResult       : String? = null

)