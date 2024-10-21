package com.onecab.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_codes")
data class QrCodeDBO(
    @PrimaryKey(autoGenerate = true)        val id: Int = 0,
    @ColumnInfo(name = "qrd_id_sbp")        val qrdIdSbp: String,
    @ColumnInfo(name = "order_id")          val orderId: String,
    @ColumnInfo(name = "order_amount")      val orderAmount: Double,
    @ColumnInfo(name = "date")              val date: String,       // 08.10.2024 (Pattern 1)
    @ColumnInfo(name = "date_time")         val dateTime: String,   // yyyy-MM-dd'T'HH:mm:ss
    @ColumnInfo(name = "image")             val image: String,
    @ColumnInfo(name = "status")            val status: String,
)
