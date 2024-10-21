package com.onecab.features.navigation_route

sealed class Screens(
    val route: String,
    val title: String
) {
    data object SplashScreen : Screens(
        route = "splashScreen",
        title = "Splash"
    )

    data object AuthorizationScreen : Screens(
        route = "authScreen",
        title = "Авторизация"
    )

    data object AuthorizationServerScreen : Screens(
        route = "authServerScreen",
        title = "Авторизация"
    )

    // Меню
    // Главная
    data object OrdersListScreen : Screens(
        route = "ordersListScreen",
        title = "Заказы"
    )

    data object CompletedOrdersScreen: Screens(
        route = "completedOrdersScreen",
        title = "Завершенные заказы"
    )

    data object CompleteOrdersInfoScreen : Screens(
        route = "completeOrdersInfoScreen",
        title = "Заказ № "
    )

    data object CompleteOrdersContentScreen : Screens(
        route = "completeOrdersContentScreen",
        title = "Состав заказа"
    )

    data object ReportScreen: Screens(
        route = "reportScreen",
        title = "Сводка"
    )

    data object SettingScreen: Screens(
        route = "settingScreen",
        title = "Настройки"
    )

    data object SettingAuthScreen: Screens(
        route = "settingAuthScreen",
        title = "Настройки"
    )

    // Остальные экраны
    data object OrdersInfoScreen : Screens(
        route = "ordersInfoScreen",
        title = "Заказ № "
    )

    data object OrdersContentScreen :
        Screens(
            route = "ordersContentScreen",
            title = "Состав заказа"
        )

    data object OrdersDebtScreen :
        Screens(
            route = "orderDebtScreen",
            title = "Задолженность"
        )

    data object OrderForPaymentScreen : Screens(
        route = "orderPaymentScreen",
        title = "Оплата заказа"
    )

    data object ScannerScreen: Screens(
        route = "scannerScreen",
        title = "Сканировать Qr код"
    )

    data object UnshippedOrdersScreen: Screens(
        route = "UnshippedOrdersScreen",
        title = "Неотправленные заказы"
    )
}