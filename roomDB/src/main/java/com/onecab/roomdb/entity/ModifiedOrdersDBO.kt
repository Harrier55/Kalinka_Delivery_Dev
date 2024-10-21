package com.onecab.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "orders", primaryKeys = ["order_id", "commodity_id"])
data class ModifiedOrdersDBO(
    @ColumnInfo(name = "order_id")          val orderId: String,
    @ColumnInfo(name = "commodity_id")      val commodityId: String,
    @ColumnInfo(name = "quantity")          val quantity: Double,
    @ColumnInfo(name = "modify_date")       val modifyDate: String,
    @ColumnInfo(name = "amount")            val amount: Double,
    @ColumnInfo(name = "vat_amount")        val vatAmount: Double,
)