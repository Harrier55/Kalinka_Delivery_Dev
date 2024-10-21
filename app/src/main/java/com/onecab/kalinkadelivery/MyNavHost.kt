package com.onecab.kalinkadelivery

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.onecab.feature_qr_scaner.scanner_screen.QrScanningScreen
import com.onecab.features.navigation_route.Screens
import com.onecab.features.screens.authtorization.AuthorizationScreen
import com.onecab.features.screens.authtorization.AuthorizationServerScreen
import com.onecab.features.screens.complete_orders.CompletedOrdersScreen
import com.onecab.features.screens.complete_orders_content.CompleteOrdersContentScreen
import com.onecab.features.screens.complete_orders_info.CompleteOrdersInfoScreen
import com.onecab.features.screens.order_debt.OrderDebtScreen
import com.onecab.features.screens.order_for_payment.OrderForPaymentScreen
import com.onecab.features.screens.order_info.OrderInfoScreen
import com.onecab.features.screens.order_list_main.OrdersListScreen
import com.onecab.features.screens.orders_content.OrdersContentScreen
import com.onecab.features.screens.report.ReportScreen
import com.onecab.features.screens.setting.SettingScreen
import com.onecab.features.screens.setting_auth.SettingAuthScreen
import com.onecab.features.screens.splash.SplashScreen
import com.onecab.features.screens.unshipped_orders.UnshippedOrdersScreen


@Composable
internal fun MyNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen()
        }

        composable(route = Screens.AuthorizationScreen.route) {
            AuthorizationScreen()
        }

        composable(route = Screens.AuthorizationServerScreen.route) {
            AuthorizationServerScreen()
        }

        composable(route = Screens.OrdersListScreen.route) {
            OrdersListScreen()
        }

        composable(route = Screens.ReportScreen.route) {
            ReportScreen()
        }

        composable(
            route = Screens.SettingScreen.route
        ) {
            SettingScreen()
        }

        composable(
            route = Screens.SettingAuthScreen.route
        ) {
            SettingAuthScreen()
        }

        composable(
            route = "${Screens.OrdersInfoScreen.route}/{orderId}/{invoiceBarcode}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("invoiceBarcode") { type = NavType.StringType }
            )
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            val barCode = backStack.arguments?.getString("invoiceBarcode")
            OrderInfoScreen(
                orderId = orderId ?: "",
                invoiceBarcode = barCode ?: ""
            )
        }

        composable(
            route = "${Screens.CompleteOrdersInfoScreen.route}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            CompleteOrdersInfoScreen(
                orderId = orderId ?: ""
            )
        }

        composable(
            route = "${Screens.CompleteOrdersContentScreen.route}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            CompleteOrdersContentScreen(
                orderId = orderId ?: ""
            )
        }

        composable(
            route = "${Screens.OrdersContentScreen.route}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            OrdersContentScreen(
                orderId = orderId ?: ""
            )
        }

        composable(
            route = "${Screens.OrdersDebtScreen.route}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            OrderDebtScreen(
                orderId = orderId ?: ""
            )
        }

        composable(
            route = "${Screens.OrderForPaymentScreen.route}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStack ->
            val orderId = backStack.arguments?.getString("orderId")
            OrderForPaymentScreen(
                orderId = orderId ?: ""
            )
        }

        composable(
            route = "${Screens.CompletedOrdersScreen.route}/{dateRequest}",
            arguments = listOf(navArgument("dateRequest") { type = NavType.StringType })
        ) {backStack ->
            val date = backStack.arguments?.getString("dateRequest")
            CompletedOrdersScreen(
                dateRequest = date?: "empty"
            )
        }

        composable(
            route = Screens.ScannerScreen.route
        ) {
            QrScanningScreen()
        }

        composable(
            route = Screens.UnshippedOrdersScreen.route
        ) {
            UnshippedOrdersScreen()
        }
    }
}