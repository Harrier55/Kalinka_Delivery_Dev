package com.onecab.features.screens.order_debt

import androidx.lifecycle.ViewModel
import com.onecab.domain.entity.DebtEntity
import com.onecab.domain.repository.DebtRepository
import com.onecab.domain.repository.OrderRepository
import com.onecab.features.di.IoDispatcher
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject

data class OrderDebtScreenUiState(
    val commonDebt: String = "",
    val debtList: List<DebtEntity> = emptyList(),
)

@HiltViewModel
class OrderDebtScreenViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
    private val debtRepository: DebtRepository,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(OrderDebtScreenUiState())
    val state = _state.asStateFlow()

    fun clickArrowBack() {
        navigationService.onClickArrowBack()
    }

    fun fetchScreen() {
        val debtList = debtRepository.getCashDebtList()
        val commonDebt = debtList.sumOf { entity -> entity.amount }

        _state.value = state.value.copy(
            commonDebt = commonDebt.toMyFormat(),
            debtList = debtList
        )
    }

    private fun Double.toMyFormat(): String {
        return String.format(Locale.UK, "%.3f", this)
    }
}