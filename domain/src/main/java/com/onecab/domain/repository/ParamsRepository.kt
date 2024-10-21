package com.onecab.domain.repository

import com.onecab.domain.entity.PaymentParamsEntity

interface ParamsRepository {

    suspend fun getParamsFromServer(token: String): Result<List<PaymentParamsEntity>>

    fun saveParamsToCash(params: List<PaymentParamsEntity>)

    fun getPaymentParamsByCompanyId(companyId: String): PaymentParamsEntity?
}