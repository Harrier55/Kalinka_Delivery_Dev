package com.onecab.maps_yandex

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MapScreenViewModel"

data class MapScreenUiState(
    val mapView: MapView? = null,
    val position: Point? = null,
    val isLoading: Boolean = false,
    val textBox: String = "",
)

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val mapView: MapView,
) : ViewModel() {

    private val _state = MutableStateFlow(MapScreenUiState())
    val state = _state.asStateFlow()

    fun fetchScreen(address: String) {
        viewModelScope.launch {

            if (address.isNotEmpty() && address != "empty") {
                _state.value = state.value.copy(
                    textBox = address,
                    isLoading = true
                )

                delay(1000)

                searchManager.submit(
                    address,
                    VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
                    searchOption,
                    searchSessionListener,
                )
            }else{
                _state.value = state.value.copy(
                    textBox = "Адрес отсутствует"
                )
            }
        }
    }

    private val searchManager =
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

    private val searchOption = SearchOptions().apply {
        searchTypes = 2
        resultPageSize = 5
    }

    private val searchSessionListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            Log.d(TAG, "--onSearchResponse: $response")

            val geoObject = response.collection.children
                .mapNotNull { it.obj }
            val geometryList = geoObject.firstOrNull()?.geometry
            val point = geometryList?.firstOrNull()?.point

            _state.value = state.value.copy(
                position = point,
                isLoading = false
            )
        }

        override fun onSearchError(error: Error) {
            Log.d(TAG, "--onSearchError: $error")
            _state.value = state.value.copy(
                textBox = "Возникла ошибка при обращении к картам ( $error )",
                isLoading = false
            )
        }
    }

    override fun onCleared() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onCleared()
    }
}