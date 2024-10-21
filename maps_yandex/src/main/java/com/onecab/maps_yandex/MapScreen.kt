package com.onecab.maps_yandex

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private const val TAG = "MapScreen"

@Composable
fun MapScreen(
    address: String,
    viewModel: MapScreenViewModel? = hiltViewModel()
) {
    Log.d(TAG, "+++ MapScreen: start___orderId = $address")

    val context = LocalContext.current
    val imageId = R.drawable.icon_location
    val imageProvider = ImageProvider.fromResource(context, imageId)

    val state = viewModel?.state?.collectAsStateWithLifecycle()?.value
    val mapView = remember {
        mutableStateOf(state?.mapView)
    }
    val position = remember(state) {
        mutableStateOf(state?.position)
    }
    val textBox = remember(state) {
        mutableStateOf(state?.textBox ?: "Аднес не загрузился")
    }
    val isLoading = remember(state) {
        mutableStateOf(state?.isLoading ?: false)
    }
    val showPermissionsDialog = remember{ mutableStateOf(false) }
    // запрос разрешений
    val permissionGranted = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { grantedPermissions ->
            val response = grantedPermissions.all { it.value }
            if (response){
                permissionGranted.value = true
                viewModel?.fetchScreen(address = address)
            }
            else{
                showPermissionsDialog.value = true
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        if (permissionGranted.value.not()) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (permissionGranted.value) {
            AndroidView(
                factory = { context: Context ->
                    MapView(context).apply {
                        MapKitFactory.getInstance().onStart()
                        mapView.value?.onStart()
                    }
                },
                update = { view ->
                    position.value?.let { pos ->
                        view.map.move(
                            CameraPosition(pos, 14.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 5f),
                            null
                        )

                        view.map.mapObjects.addPlacemark().apply {
                            geometry = pos
                            setIcon(imageProvider)
                        }
                    }
                },
                modifier = Modifier
            )
        }

        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .fillMaxSize()
                    .padding(),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 3.dp
            )
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                .height(100.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.6f)
            )

        ) {
            Text(
                text = textBox.value,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    // Диалог если не выдали разрешения
    PermissionsDialog(
        showDialog = showPermissionsDialog.value,
        dismiss = {showPermissionsDialog.value = false}
    )
}

@Composable
private fun PermissionsDialog(
    showDialog: Boolean,
    dismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { dismiss.invoke()},
            confirmButton = { dismiss.invoke() },
            title = {
                Text(text = "Геопозиционирование")
            },
            text = {
                Text(
                    text = "Вы не выдали необходимые разрешения на использование геопозиции." +
                            "\nПерейдите в настройки и разрешить картам их использовать"
                )
            },
            icon = {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MapScreen(address = "", viewModel = null)
}