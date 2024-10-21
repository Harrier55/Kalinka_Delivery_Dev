package com.onecab.data

import android.util.Log
import com.onecab.domain.bank_entity.ImageData
import com.onecab.domain.bank_entity.NewQRRequest
import com.onecab.domain.bank_entity.NewQrResponse
import com.onecab.domain.bank_entity.PayResultResponse
import com.onecab.domain.bank_entity.StatusQrdEntity
import com.onecab.domain.entity.PaymentParamsEntity
import com.onecab.domain.enums.getErrorName
import com.onecab.domain.repository.PaymentRepository
import com.onecab.source_web.api.PaymentService
import com.onecab.source_web.payment_dto.NewQRResponseDTO
import com.onecab.source_web.payment_dto.StatusQrdDTO
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "PaymentRepositoryImpl"

@Singleton
class PaymentRepositoryImpl @Inject constructor(
) : PaymentRepository {

    override suspend fun getNewQr(
        paymentAmount: Double,
        paymentParams: PaymentParamsEntity
    ): PayResultResponse<NewQrResponse> {

        try {
            val merchantId = paymentParams.merchantIDSBP
            val userName = paymentParams.username
            val password = paymentParams.password

            val paymentService = PaymentService().geyPaymentApi(userName, password)

            val imageData = ImageData(
                mediaType = "image/png",
                width = 600,
                height = 600
            )

            val newQRRequest = NewQRRequest(
                Merchant_ID_SBP = merchantId,
                QR_Type = "02",
                Payment_Amount = (paymentAmount * 100).toInt(),
                qrTtl = paymentParams.qrTtl,
                image = imageData
            )

            val response = paymentService.getQrCode(
                merchantId = merchantId,
                newQRRequest = newQRRequest
            )

            Log.d(TAG, ">>>> newQr response = $response")

            return when (response.code()) {
                200 -> {
                    PayResultResponse.Success(
                        data = response.body()!!.toNewQRResponse()
                    )
                }

                400 -> {
                    PayResultResponse.Error(
                        httpCode = response.code(),
                        message = "Неправильно сформирован запрос на формирование QR - кода"
                    )
                }

                401 -> {
                    PayResultResponse.Error(
                        httpCode = response.code(),
                        message = "Ошибка авторизации на сервере банка"
                    )
                }

                else -> {
                    PayResultResponse.Error(
                        httpCode = response.code(),
                        message = getErrorName(code = response.code()) ?: response.message()
                    )
                }
            }
        } catch (e: Exception) {
            return PayResultResponse.Error(
                httpCode = 0,
                message = e.message!!
            )
        }
    }

    override suspend fun getStatusQrd(
        qrdIdSbp: String,
        paymentParams: PaymentParamsEntity,
    ): PayResultResponse<StatusQrdEntity> {
        try {

            val paymentService = PaymentService()
                .geyPaymentApi(
                    userName = paymentParams.username,
                    password = paymentParams.password
                )

            val response = paymentService.getStatusQrd(
                merchantId = paymentParams.merchantIDSBP,
                qrdIdSbp = qrdIdSbp
            )

            Log.d(TAG, "getStatusQrd: $response")

            return when (response.code()) {
                200 -> {
                    PayResultResponse.Success(
                        data = response.body()?.toStatusQrdEntity() ?: StatusQrdEntity()
                    )
                }

                else -> {
                    PayResultResponse.Error(
                        httpCode = response.code(),
                        message = getErrorName(code = response.code()) ?: response.message()
                    )
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return PayResultResponse.Error(
                httpCode = 0,
                message = e.message!!
            )
        }
    }

    /*------ Extentions --------------------*/

    private fun StatusQrdDTO.toStatusQrdEntity(): StatusQrdEntity {
        return StatusQrdEntity(
            paymentStatus = this.PaymentStatus ?: "",
            paymentIdBank = this.PaymentIdBank ?: "",
            timeStamp = this.TimeStamp ?: "",
            payerIdSBP = this.PayerIdSBP ?: ""
        )
    }

    private fun NewQRResponseDTO.toNewQRResponse(): NewQrResponse {
        return NewQrResponse(
            QRDIDSBP = this.QRDIDSBP,
            payload = this.Payload,
            imageContent = this.image?.content
        )
    }
}


