package com.onecab.data

import android.util.Log
import com.onecab.domain.entity.QrCodeCheckerEntity
import com.onecab.domain.repository.CheckQrCodeRepository
import com.onecab.roomdb.dao.QrCodeDAO
import com.onecab.roomdb.entity.QrCodeDBO
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "CheckQrCodeRepositoryImpl"

@Singleton
class CheckQrCodeRepositoryImpl @Inject constructor(
    private val qrCodeDAO: QrCodeDAO
) : CheckQrCodeRepository {

    override suspend fun saveCode(qrCodeCheckerEntity: QrCodeCheckerEntity) {
        try {
            qrCodeDAO.saveQrCode(
                qrCodeDBO = qrCodeCheckerEntity.toQrCodeDBO()
            )
            Log.d(TAG, "-- Запись QR кода в базу успешна")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override suspend fun getCode(currentDate: String): List<QrCodeCheckerEntity> {
        try {
            val list = qrCodeDAO
                .getListQrCodes(currentDate = currentDate)
                .map { it.toQrCodeCheckerEntity() }

            Log.d(TAG, "-- Список QR кодов из базы = $list")
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    // обновить статус QR кода
    override suspend fun updateStatus(qrdIdSbp: String, newStatus: String) {
        try {
            qrCodeDAO.updateStatus(qrdIdSbp, newStatus)
            Log.d(TAG, "-- updateStatus: Статус QR кода обновлен")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun QrCodeCheckerEntity.toQrCodeDBO(): QrCodeDBO {
        return QrCodeDBO(
            id = 0,
            qrdIdSbp = this.qrdIdSbp,
            orderId = this.orderId,
            orderAmount = this.paymentAmount,
            date = this.date,
            dateTime = this.dateTime,
            image = this.image,
            status = this.status,
        )
    }

    private fun QrCodeDBO.toQrCodeCheckerEntity(): QrCodeCheckerEntity {
        return QrCodeCheckerEntity(
            qrdIdSbp = this.qrdIdSbp,
            orderId = this.orderId,
            paymentAmount = this.orderAmount,
            date = this.date,
            dateTime = this.dateTime,
            image = this.image,
            status = this.status,
        )
    }
}


