package com.onecab.data

import android.util.Log
import com.onecab.domain.entity.PaymentParamsEntity
import com.onecab.domain.repository.ParamsRepository
import com.onecab.source_web.api.KalinkaApiService
import com.onecab.source_web.models_dto.SBP
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ParamsRepositoryImpl"

/*
    CRUD для параметров настройки, приходящих с метода getParams
 */

@Singleton
class ParamsRepositoryImpl @Inject constructor(
    private val kalinkaApiService: KalinkaApiService
) : ParamsRepository {

    private var cashPaymentParamsList = mutableListOf<PaymentParamsEntity>()

    override suspend fun getParamsFromServer(token: String): Result<List<PaymentParamsEntity>> {

        try {
            val response = kalinkaApiService.getParams(token = token)
            Log.d(TAG, "---getParamsFromServer response = $response")
            val resultParamsList = mutableListOf<PaymentParamsEntity>()

            response.forEach { paymentParamsDTO ->
                paymentParamsDTO.SBP.forEach { sbp ->
                    resultParamsList.add(sbp.toPaymentParamsEntity())
                }
            }
            Log.d(TAG, "---getParamsFromServer ResultList = $resultParamsList")
            return Result.success(resultParamsList)

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override fun saveParamsToCash(params: List<PaymentParamsEntity>) {
        cashPaymentParamsList = params.toMutableList()
    }

    override fun getPaymentParamsByCompanyId(companyId: String): PaymentParamsEntity? {
        cashPaymentParamsList.forEach { paymentParamsEntity ->
            if(paymentParamsEntity.companyId == companyId) return paymentParamsEntity
        }
        return null
    }

    private fun SBP.toPaymentParamsEntity(): PaymentParamsEntity {
        return PaymentParamsEntity(
            companyId = this.companyId ?: "",
            username = this.Username ?: "",
            password = this.Password ?: "",
            merchantIDSBP = this.MerchantIDSBP ?: "",
            qrTtl = 120
        )
    }
}