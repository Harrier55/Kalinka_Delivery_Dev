package com.onecab.di

import com.onecab.crashlytics.CrashlyticsManagerImpl
import com.onecab.domain.crashlytics.CrashlyticsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CrashlyticsModule {

    @Provides
    @Singleton
    fun provideCrashlytics(): CrashlyticsManager = CrashlyticsManagerImpl()
}
