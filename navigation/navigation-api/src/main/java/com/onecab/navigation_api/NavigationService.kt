package com.onecab.navigation_api


interface NavigationService {
    fun onClickArrowBack()
    fun navigateWithoutArgument(route: String)
    fun navigateWithIntArgument(route: String, nameValue: String, argumentValue: Int)
    fun navigateWithStringArgument(route: String, nameValue: String, argumentValue: String)
    fun navigateWithStringArgumentInclusivePopUp(
        route: String,
        popUpTo: String,
        orderId: String,
        invoiceBarcode: String
    )
    fun navigateToOrderInfoScreen(
        route: String,
        orderId: String,
        invoiceBarcode: String
    )
    suspend fun openNavigationDrawer()
    suspend fun closeNavigationDrawer()
}