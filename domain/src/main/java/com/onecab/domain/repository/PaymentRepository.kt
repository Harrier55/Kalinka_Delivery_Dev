package com.onecab.domain.repository

import com.onecab.domain.bank_entity.NewQrResponse
import com.onecab.domain.bank_entity.PayResultResponse
import com.onecab.domain.bank_entity.StatusQrdEntity
import com.onecab.domain.entity.PaymentParamsEntity

interface PaymentRepository {

    suspend fun getNewQr(
        paymentAmount: Double,
        paymentParams: PaymentParamsEntity
    ): PayResultResponse<NewQrResponse>

    suspend fun getStatusQrd(
        qrdIdSbp: String,
        paymentParams: PaymentParamsEntity,
    ): PayResultResponse<StatusQrdEntity>
}