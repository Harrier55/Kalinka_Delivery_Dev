package com.onecab.feature_qr_scaner.scanner_screen

import android.util.Log
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QrScanViewModel @Inject constructor(
    private val navigationService: NavigationService,
): ViewModel() {

    private val _uiState: MutableStateFlow<QrScanUIState> = MutableStateFlow(QrScanUIState())
    val uiState: StateFlow<QrScanUIState> = _uiState

    fun onQrCodeDetected(result: String) {
        Log.d("QR Scanner", result)
        _uiState.update { it.copy(detectedQR = result) }
    }

    fun onTargetPositioned(rect: Rect) {
        _uiState.update { it.copy(targetRect = rect) }
    }

    fun navigateToNextScreen(invoiceBarcode: String) {
        navigationService.navigateWithStringArgumentInclusivePopUp(
            route = "ordersInfoScreen",
            popUpTo = "scannerScreen",
            orderId = "empty",
            invoiceBarcode = invoiceBarcode
        )
    }
}