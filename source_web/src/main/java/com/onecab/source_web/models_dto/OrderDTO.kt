package com.onecab.source_web.models_dto

import com.google.gson.annotations.SerializedName

data class OrderDTO (

    @SerializedName("purchaser_id"          ) var purchaserId          : String?          = null,
    @SerializedName("purchaser_name"        ) var purchaserName        : String?          = null,
    @SerializedName("purchaser_phonenumber" ) var purchaserPhonenumber : String?          = null,
    @SerializedName("purchaser_email"       ) var purchaserEmail       : String?          = null,
    @SerializedName("order_id"              ) var orderId              : String?          = null,
    @SerializedName("order_number"          ) var orderNumber          : String?          = null,
    @SerializedName("order_date"            ) var orderDate            : String?          = null,
    @SerializedName("invoice_id"            ) var invoiceId            : String?          = null,
    @SerializedName("invoicerecord_id"      ) var invoicerecordId      : String?          = null,
    @SerializedName("extra"                 ) var extra                : String?          = null,
    @SerializedName("delivery_date"         ) var deliveryDate         : String?          = null,
    @SerializedName("delivery_address"      ) var deliveryAddress      : String?          = null,
    @SerializedName("payment_date"          ) var paymentDate          : String?          = null,
    @SerializedName("payment_amount"        ) var paymentAmount        : Double?          = null,
    @SerializedName("invoice_barcode"       ) var invoiceBarcode       : String?          = null,
    @SerializedName("company_id"            ) var companyId            : String?          = null,
    @SerializedName("goods"                 ) var goods                : ArrayList<GoodsDTO> = arrayListOf(),

    @SerializedName("status"                ) var status               : String?          = null,

)
