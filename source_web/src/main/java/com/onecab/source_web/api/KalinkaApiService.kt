package com.onecab.source_web.api

import com.onecab.source_web.models_dto.AddOrderPayDTO
import com.onecab.source_web.models_dto.AddOrderPayRequestDTO
import com.onecab.source_web.models_dto.AddOrderStatusDTO
import com.onecab.source_web.models_dto.OrderDTO
import com.onecab.source_web.models_dto.OrderDebtDTO
import com.onecab.source_web.models_dto.PaymentParamsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface KalinkaApiService {

    // получение списка заказов
    @GET("hs/ab_delivery/getOrders")
    suspend fun getOrders(
        @Header("token") token: String,
        @Query("date") date: String
    ): Array<OrderDTO>

    // получить задолженность
    @GET("hs/ab_delivery/getDebt")
    suspend fun getDebt(
        @Header("token") token: String,
        @Query("order_id") orderId: String
    ): Array<OrderDebtDTO>

    // отправить заказ, как оплаченный
    @POST("hs/ab_delivery/addOrderPay")
    suspend fun addOrderPay(
        @Header("token") token: String,
        @Body addOrderPayDTO: AddOrderPayDTO
    ): Response<Void>

    // оправим заказ на сервер, как оплаченный
    @POST("hs/ab_delivery/addOrderStatus")
    suspend fun addOrderStatus(
        @Header("token") token: String,
        @Body addOrderStatusDTO: AddOrderStatusDTO
    ): Response<Void>

    // отправить на сервер результат получения QR кода
    @POST("hs/ab_delivery/addOrderPayRequest")
    suspend fun addOrderPayRequest(
        @Header("token") token: String,
        @Body addOrderPayRequestDTO: AddOrderPayRequestDTO
    ): Response<Void>

    // получение параметров для настройки оплаты
    @GET("hs/ab_delivery/getParams")
    suspend fun getParams(
        @Header("token") token: String,
    ): Array<PaymentParamsDTO>
}