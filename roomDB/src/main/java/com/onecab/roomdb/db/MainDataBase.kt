package com.onecab.roomdb.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.onecab.roomdb.dao.OrderDAO
import com.onecab.roomdb.dao.QrCodeDAO
import com.onecab.roomdb.entity.ModifiedOrdersDBO
import com.onecab.roomdb.entity.QrCodeDBO
import com.onecab.roomdb.entity.UnshippedOrdersDBO

@Database(
    entities = [
    ModifiedOrdersDBO::class,
    UnshippedOrdersDBO::class,
    QrCodeDBO::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MainDataBase: RoomDatabase() {
    abstract fun orderDao(): OrderDAO
    abstract fun qrCodeDao(): QrCodeDAO
}