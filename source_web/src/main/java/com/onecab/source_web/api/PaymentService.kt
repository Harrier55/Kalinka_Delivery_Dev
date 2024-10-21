package com.onecab.source_web.api

import com.google.gson.GsonBuilder
import com.onecab.domain.bank_entity.NewQRRequest
import com.onecab.source_web.payment_dto.NewQRResponseDTO
import com.onecab.source_web.payment_dto.StatusQrdDTO
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://sbp-merchant.chelinvest.ru:19443/"

// нам надо именно генерация динамического QR

class PaymentService {

    fun geyPaymentApi(userName: String, password: String): PaymentApi {
        return getRetrofit(userName, password).create(PaymentApi::class.java)
    }

    private fun getRetrofit(userName: String, password: String): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val credentials = Credentials.basic(userName, password)
                val newRequest = request.newBuilder()
                    .header("Authorization", credentials)
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(client)
            .build()
    }
}

//private const val BASE_URL =
//    "https://sbp-merchant.chelinvest.ru:19443/"
//private const val TEST_BASE_URL =
//    "https://sbp-merchant.chelinvest.ru:19443/api/v1/merchants/MA0000016142/NewQR"
//private const val TEST_BASE_URL_2 =
//    "https://sbp-merchant.chelinvest.ru:19443/api/v1/merchants/MA0000016142/GetStatusQRD"

interface PaymentApi {

    @POST("api/v1/merchants/{merchantId}/NewQR")
    suspend fun getQrCode(
        @Path("merchantId") merchantId: String,
        @Body newQRRequest: NewQRRequest
    ): Response<NewQRResponseDTO>


    @GET("api/v1/merchants/{merchantId}/GetStatusQRD/{qrdIdSbp}")
    suspend fun getStatusQrd(
        @Path("merchantId") merchantId: String,
        @Path("qrdIdSbp") qrdIdSbp: String
    ): Response<StatusQrdDTO>
}