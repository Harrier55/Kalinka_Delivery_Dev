package com.onecab.navigation_impl

import androidx.compose.material3.DrawerState
import androidx.navigation.NavHostController
import com.onecab.navigation_api.NavigationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationServiceImpl @Inject constructor(
    private val navController: NavHostController,
    private val drawerState: DrawerState
) : NavigationService {

    override fun onClickArrowBack() {
        navController.popBackStack()
    }

    override fun navigateWithoutArgument(route: String) {
        navController.navigate(route = route)
    }

    override fun navigateWithIntArgument(route: String, nameValue: String, argumentValue: Int) {
        navController.navigate(route = "${route}/${argumentValue}")
    }

    override fun navigateWithStringArgument(
        route: String,
        nameValue: String,
        argumentValue: String
    ) {
        val validAddress = if (argumentValue.isBlank()) {
            "empty" // Или другое значение по умолчанию
        } else {
            argumentValue
        }

        navController.navigate(route = "${route}/${validAddress}")
    }

    /*
    - popUpTo("screen1")  указывается маршрут, до которого нужно очистить стек.
   - inclusive = true  говорит о том, что  и сам маршрут "screen1" также будет удален из стека.
     */
    override fun navigateWithStringArgumentInclusivePopUp(
        route: String,
        popUpTo: String, // это screen1
        orderId: String,
        invoiceBarcode: String
    ) {
        navController.navigate(
            route = "${route}/${orderId}/${invoiceBarcode}"
        ) {
            popUpTo(popUpTo) { inclusive = true }
        }
    }

    override fun navigateToOrderInfoScreen(
        route: String,
        orderId: String,
        invoiceBarcode: String
    ) {
        navController.navigate(
            route = "${route}/${orderId}/${invoiceBarcode}"
        )
    }

    override suspend fun openNavigationDrawer() {
        drawerState.open()
    }

    override suspend fun closeNavigationDrawer() {
        drawerState.close()
    }
}