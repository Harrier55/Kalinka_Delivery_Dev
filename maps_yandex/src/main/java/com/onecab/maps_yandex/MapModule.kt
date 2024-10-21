package com.onecab.maps_yandex

import android.content.Context
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    private const val MAPKIT_API_KEY = "81d5d4e5-58fb-4f81-a0e5-7b0858ec79da"

    @Singleton
    @Provides
    fun provideMapView(
        @ApplicationContext context: Context
    ): MapView {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(context)
        return MapView(context)
    }
}