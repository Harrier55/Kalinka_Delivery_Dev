package com.onecab.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.onecab.roomdb.entity.ModifiedOrdersDBO
import com.onecab.roomdb.entity.UnshippedOrdersDBO


@Dao
interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGoods(modifiedOrdersDBO: ModifiedOrdersDBO):Long

    @Query("SELECT * FROM orders")
    suspend fun getGoods():List<ModifiedOrdersDBO>

    @Delete
    suspend fun deleteGoods(modifiedOrdersDBO: ModifiedOrdersDBO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUnshippedOrder(unshippedOrdersDBO: UnshippedOrdersDBO): Long

    @Query("SELECT * FROM unshipped_orders")
    suspend fun getUnshippedOrders(): List<UnshippedOrdersDBO>

    @Update
    suspend fun updateUnshippedOrder(unshippedOrdersDBO: UnshippedOrdersDBO)

    @Delete
    suspend fun deleteUnshippedOrder(unshippedOrdersDBO: UnshippedOrdersDBO)
}