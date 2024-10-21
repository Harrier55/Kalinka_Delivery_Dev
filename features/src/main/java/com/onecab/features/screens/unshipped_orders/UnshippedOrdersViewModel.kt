package com.onecab.features.screens.unshipped_orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.domain.entity.UnshippedOrdersEntity
import com.onecab.domain.repository.OrderRepository
import com.onecab.features.di.IoDispatcher
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UnshippedOrdersUiState(
    val unshippedOrdersEntityList: List<UnshippedOrdersEntity> = emptyList()
)

@HiltViewModel
class UnshippedOrdersViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val navigationService: NavigationService,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UnshippedOrdersUiState())
    val state = _state.asStateFlow()

    fun fetchScreen() {
        viewModelScope.launch(ioDispatcher) {
            val list = orderRepository.getUnshippedOrderFromDb()

            println(" list = $list")
            _state.value = state.value.copy(
                unshippedOrdersEntityList = list
            )
        }
    }

    fun clickArrowBack() {
        navigationService.onClickArrowBack()
    }
}