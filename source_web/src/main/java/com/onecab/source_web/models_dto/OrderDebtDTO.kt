package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class OrderDebtDTO (

    @SerializedName("account_document_number" ) var accountDocumentNumber : String? = null,
    @SerializedName("amount"                  ) var amount                : Double?    = null,
    @SerializedName("status"                  ) var status                : String? = null

)
