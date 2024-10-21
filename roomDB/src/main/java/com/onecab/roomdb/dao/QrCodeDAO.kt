package com.onecab.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onecab.roomdb.entity.QrCodeDBO

@Dao
interface QrCodeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQrCode(qrCodeDBO: QrCodeDBO)

    @Query("SELECT * FROM qr_codes WHERE date = :currentDate")
    suspend fun getListQrCodes(currentDate: String): List<QrCodeDBO>

    // обновить информацию о статусе QR кода
    @Query("UPDATE qr_codes SET status = :newStatus WHERE qrd_id_sbp = :qrdIdSbp")
    suspend fun updateStatus(qrdIdSbp: String, newStatus: String)
}