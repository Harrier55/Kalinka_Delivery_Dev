package com.onecab.features.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.core.utils.DateFormatterMyApplication
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_lond_to_string_4
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_lond_to_string_5
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_lond_to_string_7
import com.onecab.domain.repository.OrderRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.features.di.IoDispatcher
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportScreenUiState(

    val currentDateForButton: String = "",
    val currentDateForRequest: String = "",
    val currentDateOriginal: String = "",
    val countOrders: String = "-",
    val paymentAmount: String = "-",
    val paymentOrders: String = "-",
    val unsentPayments: String = "-",
    val isLoading: Boolean = false,
    val showDateSelectDialog: Boolean = false,
)

@HiltViewModel
class ReportScreenViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
    private val registerRepository: RegisterRepository,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(ReportScreenUiState())
    val state = _state.asStateFlow()

    init {
        _state.value = state.value.copy(
            currentDateForButton = DateFormatterMyApplication.getCurrentDateForCalendarButton(),
            currentDateForRequest = DateFormatterMyApplication.getCurrentDateForRequest()
        )
    }

    fun fetchScreen() {


        viewModelScope.launch {

            _state.value = state.value.copy(isLoading = true)

            delay(500)

            val listOrders = async(ioDispatcher) {
                orderRepository.getOrdersFromServer(
                    date = state.value.currentDateForRequest,
                    token = registerRepository.getAuthToken().token?: ""
                )
            }.await()

            val unsentOrders = async(ioDispatcher) {
                orderRepository.getUnshippedOrderFromDb()
            }.await()

            val listOrdersPayment = listOrders
                .filter { orderEntity -> orderEntity.paymentAmount > 0.0 }
            val countOrders = listOrders.size
            val paymentOrders = listOrdersPayment.size
            val paymentAmount = listOrdersPayment
                .sumOf { orderEntity -> orderEntity.paymentAmount }
            val unsentPayments = unsentOrders.size

            _state.value = state.value.copy(
                isLoading = false,
                countOrders = countOrders.toString(),
                paymentAmount = paymentAmount.toString(),
                paymentOrders = paymentOrders.toString(),
                unsentPayments = unsentPayments.toString()
            )
        }
    }

    fun openDateSelectDialog() {
        _state.value = state.value.copy(
            showDateSelectDialog = true
        )
    }

    fun closeDateSelectDialog() {
        _state.value = state.value.copy(
            showDateSelectDialog = false
        )
    }

    fun onClickButtonSelectDate(selectDate: Long?) {
        closeDateSelectDialog()
        selectDate?.let {
            _state.value = state.value.copy(
                currentDateOriginal = it.date_formatter_lond_to_string_7(),
                currentDateForButton = it.date_formatter_lond_to_string_4(),
                currentDateForRequest = it.date_formatter_lond_to_string_5()
            )
        }
        fetchScreen()
    }

    suspend fun openNavigationDrawer() {
        navigationService.openNavigationDrawer()
    }

    fun navToCompleteOrders() {
        navigationService.navigateWithStringArgument(
            route = Screens.CompletedOrdersScreen.route,
            nameValue = "dateRequest",
            argumentValue = state.value.currentDateOriginal
        )
    }

    fun navToUnshippedOrders() {
        navigationService.navigateWithoutArgument(route = Screens.UnshippedOrdersScreen.route)
    }
}