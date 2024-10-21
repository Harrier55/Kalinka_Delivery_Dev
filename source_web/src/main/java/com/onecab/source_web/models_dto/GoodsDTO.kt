package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class GoodsDTO (

    @SerializedName("commodity_id"   ) var commodityId   : String? = null,
    @SerializedName("commodity_name" ) var commodityName : String? = null,
    @SerializedName("unit_name"      ) var unitName      : String? = null,
    @SerializedName("quantity"       ) var quantity      : Double? = null,
    @SerializedName("price"          ) var price         : Double? = null,
    @SerializedName("amount"         ) var amount        : Double? = null,
    @SerializedName("vat_amount"     ) var vatAmount     : Double? = null

)