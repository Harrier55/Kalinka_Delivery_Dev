package com.onecab.domain.repository

import com.onecab.domain.entity.DebtEntity

interface DebtRepository {

    suspend fun getDebtListFromServer(orderId: String): Result<List<DebtEntity>>
    fun getCashDebtList(): List<DebtEntity>
}