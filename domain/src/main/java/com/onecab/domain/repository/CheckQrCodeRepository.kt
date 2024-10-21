package com.onecab.domain.repository

import com.onecab.domain.entity.QrCodeCheckerEntity

interface CheckQrCodeRepository {

    suspend fun saveCode(qrCodeCheckerEntity: QrCodeCheckerEntity)
    suspend fun getCode(currentDate: String): List<QrCodeCheckerEntity>
    suspend fun updateStatus(qrdIdSbp: String, newStatus: String)
}