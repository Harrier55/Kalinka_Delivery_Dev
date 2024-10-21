package com.onecab.feature_qr_scaner.scanner_screen

import androidx.camera.core.CameraSelector
import androidx.compose.ui.geometry.Rect

data class QrScanUIState(
    val loading: Boolean = false,
    val detectedQR: String = "",
    val targetRect: Rect = Rect.Zero,
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
)