package com.onecab.source_web.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.onecab.source_web.auth_dto.AuthRequestDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Класс предназначен для проверки адреса сервера и регистрации пользователя
 * url адрес получаем динамически
 * метод ping() - проверит, отвечает ли сервер, если да - сервер вернет pong
 * метод login() - для регистрации пользователя, возвращает токен
 *
 * Выбрал динамическое получение адреса, т.к пользователь может менять адрес в настройках
 * при успешной авторизации этот адрес запишется в Preferences
 * и при штатной работе приложения будет доставаться из него
 */

private const val HTTP = "http://"

class RegisterService {

    fun getRegisterServiceApi(inputUrl: String): RegisterApi {
        return getRetrofit(url = inputUrl).create(RegisterApi::class.java)
    }

    private fun getRetrofit(url: String): Retrofit {
        Log.i("RegisterService", "-- получен динамический URL : ${"$HTTP$url/"} ")
        return Retrofit.Builder()
            .baseUrl("$HTTP$url/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }
}

interface RegisterApi {

    @GET("hs/ab_delivery/ping")
    suspend fun ping(): Response<String>

    @POST("hs/ab_delivery/auth")
    suspend fun login(
        @Body authRequestDto: AuthRequestDTO
    ): String


}