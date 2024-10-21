package com.onecab.features.screens.order_info

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.bottom_sheet.ErrorBottomSheet
import com.onecab.features.common_component.buttons.BlueButton
import com.onecab.features.common_component.buttons.ClearButton
import com.onecab.features.common_component.dialogs.CombineAlertDialog
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens

private const val TAG = "OrderInfoScreen"
private const val COMMON_WIDTH = 320

@Composable
fun OrderInfoScreen(
    orderId: String,
    invoiceBarcode: String,
    modifier: Modifier = Modifier,
    viewModel: OrderInfoScreenViewModel? = hiltViewModel()
) {
    Log.i(TAG, "+++ OrderInfoScreen: orderId = $orderId / invoiceBarcode = $invoiceBarcode")

    val context = LocalContext.current
    val permission = Manifest.permission.CALL_PHONE

    // values
    val orderNumber = remember { mutableStateOf("1111") }
    val address =
        remember { mutableStateOf("город Новосибирск, 3 улица Строителей ул. Красного Урала, д. 3") }
    val client = remember { mutableStateOf("ИП Иванов") }
    val date = remember { mutableStateOf("10.10.2024") }
    val debt = remember { mutableStateOf("----") }
    val consist = remember { mutableStateOf("3") }
    val weight = remember { mutableStateOf("17") }
    val comment = remember { mutableStateOf("comment") }
    val amount = remember { mutableStateOf("15000") }
    val phone = remember { mutableStateOf("000") }

    // Flags
    val showErrorPermission = remember { mutableStateOf(false) }
    val showCallPhoneDialog = remember { mutableStateOf(false) }
    val showMarkBottomSheet = remember { mutableStateOf(false) }
    val showSelectMapsSheet = remember { mutableStateOf(false) }

    // Управление комбинированным алерт диалогом
    val openDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val tickYes = remember { mutableStateOf(false) }
    val tickNo = remember { mutableStateOf(false) }

    // если заказ не найден - показать соотв. диалог
    val showEmptyOrderDialog= remember { mutableStateOf(false) }

    viewModel?.let { vm ->
        val state = vm.state.collectAsState().value

        orderNumber.value = state.orderEntity.orderNumber
        address.value = state.orderEntity.deliveryAddress
        client.value = state.orderEntity.purchaserName
        date.value = state.orderEntity.deliveryDate
        debt.value = state.debt
        consist.value = state.consist
        weight.value = state.weight
        comment.value = state.orderEntity.comment
        amount.value = state.amountCalculated
        phone.value = state.orderEntity.purchaserPhone
        openDialog.value = state.openDialog
        isLoading.value = state.isLoading
        tickYes.value = state.tickYes
        tickNo.value = state.tickNo
        showSelectMapsSheet.value = state.showSelectMapsSheet
        showEmptyOrderDialog.value = state.showEmptyOrderDialog
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fillScreen(orderId = orderId, invoiceBarcode = invoiceBarcode)
    }

    //Запрашиваем разрешение на выполнение звонков
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                makeCall(context = context, phoneNumber = phone.value)
            } else {
                // Разрешение не предоставлено
                showErrorPermission.value = true
            }
        }
    )

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithArrowBack(
                textAppBar = Screens.OrdersInfoScreen.title + orderNumber.value,
                onClickArrowBack = { viewModel?.clickArrowBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = paddingValues.calculateTopPadding())
                .width(COMMON_WIDTH.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = date.value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .width(COMMON_WIDTH.dp),
                textAlign = TextAlign.End
            )

            TextBlockWithIconButton(
                textValue = "Адрес: ${address.value}",
                iconId = com.onecab.core.R.drawable.icon_address,
                isActive = true,
                onClickIcon = { viewModel?.openSelectMapsSheet() }
            )

            TextBlockWithIconButton(
                textValue = "Клиент: ${client.value}",
                iconId = com.onecab.core.R.drawable.icon_phone,
                isActive = phone.value.isNotEmpty(),
                onClickIcon = {
                    if (phone.value.isNotEmpty()) showCallPhoneDialog.value = true
                }
            )

            TextBlockWithIconButton(
                textValue = "Задолженность: ${debt.value} руб.",
                iconId = com.onecab.core.R.drawable.icon_debt,
                isActive = true,
                onClickIcon = {
                    viewModel?.navToDebtScreen()
                }
            )

            TextBlockWithIconButton(
                textValue = "Состав: ${consist.value} позиции, ${weight.value} кг.",
                iconId = com.onecab.core.R.drawable.icon_consist,
                isActive = true,
                onClickIcon = {
                    viewModel?.navToOrdersContent()
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
                    .padding(top = 8.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
            ) {
                Text(
                    text = "Комментарий: ",
                    style = MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.surface),
                )
                Text(
                    text = comment.value,
                    style = MaterialTheme.typography.labelLarge
                        .copy(color = MaterialTheme.colorScheme.surface),
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Итого: ${amount.value} руб.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            BlueButton(
                textButton = "ПРИНЯТЬ ОПЛАТУ",
                widthButton = 320,
                enabled = true,
                onClick = {
                    viewModel?.navToPaymentScreen()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClearButton(
                textButton = "ДОСТАВЛЕНО",
                widthButton = 320,
                onClick = {
                    showMarkBottomSheet.value = true
                }
            )

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Show error permission call_phone
        ErrorBottomSheet(
            openSheet = showErrorPermission.value,
            closeSheet = { showErrorPermission.value = false },
            title = "Нет разрешения на звонки",
            textError = "Вы не предоставили приложению разрешений \nна совершение" +
                    " звонков. \nНо вы можете позвонить самостоятельно по" +
                    " номеру \n ${phone.value}"
        )

        // Шторка, для звонка по телефону
        PhoneDialogBottomSheet(
            openSheet = showCallPhoneDialog.value,
            closeSheet = { showCallPhoneDialog.value = false },
            subTitle = client.value,
            phoneNumber = phone.value,
            onClickYes = {
                showCallPhoneDialog.value = false
                launcher.launch(permission)
            },
            onClickNo = { showCallPhoneDialog.value = false }
        )

        // Шторка выбора карты для отображения адреса
        SelectMapsSheet(
            openSheet = showSelectMapsSheet.value,
            address = address.value,
            dismiss = { viewModel?.closeSelectMapsSheet() },
            onClickItem = {
                viewModel?.showSelectedMaps(
                    context = context,
                    item = it,
                    address = address.value
                )
            }
        )
        //Шторка для отметки Доставлено
        MarkAsDeliveredBottomSheet(
            openSheet = showMarkBottomSheet.value,
            closeSheet = { showMarkBottomSheet.value = false },
            onClickNo = { showMarkBottomSheet.value = false },
            onClickYes = {
                showMarkBottomSheet.value = false
                viewModel?.onClickDelivery()
            }
        )
        // Показать комбинированый диалог (загрузка-успех-неуспех)
        CombineAlertDialog(
            openDialog = openDialog.value,
            isLoading = isLoading.value,
            tickYes = tickYes.value,
            tickNo = tickNo.value
        )

        // Показать окно диалога, если заказ не найден
        EmptyOrderDialog(
            show = showEmptyOrderDialog.value,
            closeDialog = {viewModel?.closeEmptyOrderDialog()}
        )
    }
}


private fun makeCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $phoneNumber"))
    context.startActivity(intent)
}

@Composable
private fun TextBlockWithIconButton(
    textValue: String,
    iconId: Int,
    isActive: Boolean,
    onClickIcon: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .width(COMMON_WIDTH.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = textValue,
            style = MaterialTheme.typography.labelLarge
                .copy(color = Color.Black),
            modifier = Modifier.weight(0.5f)
        )

        Card(
            onClick = { onClickIcon.invoke() },
            modifier = Modifier
                .width(50.dp)
                .height(40.dp),
            shape = ShapeDefaults.Small,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "address",
                    tint = if (isActive)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewOrderInfo() {
    KalinkaDeliveryTheme {
        OrderInfoScreen(
            orderId = "11",
            invoiceBarcode = "555",
            viewModel = null
        )
    }
}
