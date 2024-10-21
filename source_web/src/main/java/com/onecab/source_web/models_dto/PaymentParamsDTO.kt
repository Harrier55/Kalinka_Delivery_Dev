package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class PaymentParamsDTO (
    @SerializedName("SBP" ) var SBP : ArrayList<SBP> = arrayListOf()
)

data class SBP (
    @SerializedName("company_id"      ) var companyId     : String? = null,
    @SerializedName("Username"        ) var Username      : String? = null,
    @SerializedName("Password"        ) var Password      : String? = null,
    @SerializedName("Merchant_ID_SBP" ) var MerchantIDSBP : String? = null
)