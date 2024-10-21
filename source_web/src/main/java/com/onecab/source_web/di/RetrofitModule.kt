package com.onecab.source_web.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.onecab.source_web.api.KalinkaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

/**
    К реализации есть некоторые пояснения

    * - на данный момент мы используем небезопасное соединение http, поэтому в манифест
        внесены исключения, они прописаны в файле @xml/network_security_config

    ** - Retrofit не может распарсить ответ сервера, потому что JSON не
        соответствует стандартному формату. Неправильный формат JSON:
         Сервер может возвращать JSON, который не соответствует стандартам
         как решение - включил setLenient(true) в GsonConverterFactory
         Это временное решение
 */

// образец адреса  "http://1c.kalinka74.ru/CRM_AB2/hs/ab_delivery/"

private const val PREF_NAME = "appPreference"
private const val SERVER_ADDRESS = "address"
private const val DEFAULT_URL = "http://1c.kalinka74.ru/CRM_AB2"
private const val HTTP = "http://"

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    fun provideApiService(retrofit: Retrofit): KalinkaApiService {
        return retrofit.create(KalinkaApiService::class.java)
    }

    @Provides
    fun provideRetrofit(@Named("baseUrl") baseUrl: String): Retrofit {
        Log.d("RetrofitModule", "--- start RetrofitModule__baseUrl = ${"$HTTP$baseUrl/"}")
        return Retrofit.Builder()
            .baseUrl("$HTTP$baseUrl/")                            // См. пояснение *
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create())// См. пояснение **
            )
            .build()
    }

    // Предоставляет BASE URL из сохраненного значения в Preferences
    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(@ApplicationContext context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(SERVER_ADDRESS, DEFAULT_URL) ?: "Адрес сервера отсутствует"
    }
}


