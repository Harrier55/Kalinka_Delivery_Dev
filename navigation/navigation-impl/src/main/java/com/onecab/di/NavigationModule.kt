package com.onecab.di

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.onecab.navigation_api.NavigationService
import com.onecab.navigation_impl.NavigationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideComposeNavHostController(@ApplicationContext context: Context): NavHostController {
        return NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }
    }

    // управление боковой шторкой для навигации
    @Provides
    @Singleton
    fun provideDrawerState(): DrawerState {
        return DrawerState(initialValue = DrawerValue.Closed)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationImplModules {

    @Binds
    @Singleton
    abstract fun provideNavigationImpl(
        navigationService: NavigationServiceImpl
    ): NavigationService
}