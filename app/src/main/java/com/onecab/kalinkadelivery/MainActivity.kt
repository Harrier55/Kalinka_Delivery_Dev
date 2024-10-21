package com.onecab.kalinkadelivery

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.repository.RegisterRepository
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navController: NavHostController

    @Inject
    lateinit var drawerState: DrawerState

    @Inject
    lateinit var navigationService: NavigationService

    @Inject
    lateinit var registerRepository: RegisterRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KalinkaDeliveryTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationDrawer(
                        navHostController = navController,
                        drawerState = drawerState,
                        navigationService = navigationService,
                        registerRepository = registerRepository,
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        val destinationRoute = navController.previousBackStackEntry?.destination?.route

        if (
            destinationRoute == Screens.SplashScreen.route ||
            destinationRoute == Screens.AuthorizationScreen.route
        ) {
            showCloseDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showCloseDialog() {
        AlertDialog.Builder(this)
            .setMessage("Хотите свернуть приложение ?")
            .setPositiveButton("Да") { _, _ -> moveTaskToBack(true) }
            .setNegativeButton("Нет") { _, _ -> /*Nothing*/}
            .show()
    }
}

