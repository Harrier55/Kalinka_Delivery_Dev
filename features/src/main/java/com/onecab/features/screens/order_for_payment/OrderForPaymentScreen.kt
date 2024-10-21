package com.onecab.features.screens.order_for_payment

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.LoadIndicator
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.bottom_sheet.ErrorBottomSheet
import com.onecab.features.common_component.buttons.BlueButton
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.common_component.text_input.TextInputFieldOutline
import com.onecab.features.navigation_route.Screens

/**
 * В данном экране долольно много логики:
 *
 * --можно изменять сумму оплаты, при этом появится ранее скрытое поле для
 * пояснений  причины коррекции
 * -- все поля валидируются при нажатии кнопки Сформировать QR
 * -- идет запрос на сервер о формировании QR
 *
 * -- после формирования QR кода заказ добавляется в БД, типа как НА ОБРАБОТКУ
 * затем по определенному алгоритму отправляем запросы на сервер банка об успешной/неуспешной
 * оплате. Если оплата успешна - выполняем отправку этого заказа на сервер, как оплаченный
 * и как домтавленный. Если запись на наш сервер выполнена успешно - удаляем этот заказ
 * из БД НА ОБРАБОТКУ
 * Логику работы этой базы смотри в экране "Неотправленные заказы"
 *
 *
 */

private const val TAG = "OrderForPaymentScreen"
private const val COMMON_WIDTH = 300

@Composable
fun OrderForPaymentScreen(
    orderId: String,
    modifier: Modifier = Modifier,
    viewModel: OrderPaymentViewModel? = hiltViewModel()
) {
    Log.i(TAG, "+++OrderForPaymentScreen: start______ orderId = $orderId")

    val state = viewModel?.state?.collectAsState()?.value

    // сумма заказа к оплате
    val paymentAmount = remember(state) {
        mutableStateOf(state?.paymentAmount ?: "123")
    }
    val orderNumber = remember(state) {
        mutableStateOf(state?.orderNum ?: "555")
    }
    // сюда придет QR код
    val image = remember(state) {
        mutableStateOf(state?.image)
    }
    val isLoading = remember(state) {
        mutableStateOf(state?.isLoading ?: false)
    }
    val showQrCodeSheet = remember(state) {
        mutableStateOf(state?.showQrCodeSheet ?: false)
    }
    // показать ошибку "Заполните все обязательные поля"
    val showTextError = remember(state) {
        mutableStateOf(state?.showTextError ?: false)
    }
    // показать шторку для ввода эл.почты
    val showInputEmailBottomSheetState = remember(state) {
        mutableStateOf(state?.showMailSheet ?: false)
    }
    // показать шторку с текстом ошибки
    val showErrorSheet = remember(state) {
        mutableStateOf(state?.showErrorSheet ?: false)
    }
    // заголовок ошибки для показа в шторке ошибок
    val titleError = remember(state) {
        mutableStateOf(state?.titleError ?: "")
    }
    // текст ошибки для показа в шторке ошибок
    val textError = remember(state) {
        mutableStateOf(state?.textError ?: "")
    }
    // показать поля ввода коррекции
    val showCorrectionField = remember(state) {
        mutableStateOf(state?.showCorrectionField ?: false)
    }
    // показать ошибку в поле ввода коррекции
    val showErrorBorderCorrectionField = remember(state) {
        mutableStateOf(state?.showErrorBorderCorrectionField ?: false)
    }
    // покажет ошибку поля ввода оплаты
    val showErrorBorderPaymentField = remember(state) {
        mutableStateOf(state?.showErrorBorderPaymentField ?: false)
    }
    val eMail = remember(state) {
        mutableStateOf(state?.eMail ?: "e-mail")
    }
    // покажет ошибку поля СМС/На почту
    val isErrorBorderEmail = remember(state) {
        mutableStateOf(state?.showErrorBorderEmail ?: false)
    }
    // Покажет ошибку некорректного ввода почты
    val showErrorValidEmail = remember(state) {
        mutableStateOf(state?.showErrorValidEmail ?: false)
    }
    // Показать шторку коррекции суммы заказа
    val showInputAmountSheet = remember {
        mutableStateOf(false)
    }
    // для шторки QR кода показать заголовок
    val titleSheet = remember(state) {
        mutableStateOf(state?.titleForSheet ?: "")
    }
    // покажет таймер обратного отсчета для QR кода
    val timerCounter = remember(state) {
        mutableStateOf(state?.timerCounter ?: "")
    }
    // просто покажет таймер отсчета
    val showTimer = remember(state) {
        mutableStateOf(state?.showTimer ?: false)
    }
    // если код еще актуален и загружен из базы - покажет стоимость, сколько оплатили по нему ранее
    val correctAmount = remember(state) {
        mutableStateOf(state?.correctAmount ?: "")
    }
    // по результату работы сервиса покажем диалог
    val showResponseDialog = remember(state) {
        mutableStateOf(state?.showResponseDialog ?: false)
    }
    val payStatusResponseDialog = remember(state) {
        mutableStateOf(state?.payStatusResponseDialog ?: false)
    }
    val titleResponseDialog = remember(state) {
        mutableStateOf(state?.titleResponseDialog ?: "")
    }
    val isLoadingResponseDialog = remember(state) {
        mutableStateOf(state?.isLoadingResponseDialog ?: false)
    }


    LaunchedEffect(key1 = true) {
        viewModel?.fetchScreen(orderId = orderId)
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithArrowBack(
                textAppBar = Screens.OrderForPaymentScreen.title + " № " + orderNumber.value,
                onClickArrowBack = { viewModel?.clickArrowBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .width(COMMON_WIDTH.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Сумма оплаты*",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)

                )

                Box(
                    modifier = Modifier
                        .clickable { showInputAmountSheet.value = true }
                        .width(160.dp)
                        .height(40.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .border(
                            width = 1.dp,
                            color = if (showErrorBorderPaymentField.value) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${paymentAmount.value}  руб.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (showCorrectionField.value) {
                Text(
                    text = "Коррекция *",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier
                        .width(COMMON_WIDTH.dp)
                        .padding(top = 30.dp, bottom = 10.dp)
                )

                DropdownMenuScreen4(
                    isError = showErrorBorderCorrectionField.value,
                    callbackSelectedItem = { viewModel?.setSelectedItem(selectedItem = it) }
                )
            }

            Text(
                text = "Эл. почта для получения чека об оплате",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
                    .padding(top = 30.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .clickable { viewModel?.openMailSheet() }
                    .width(300.dp)
                    .height(40.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .border(
                        width = 1.dp,
                        color = if (isErrorBorderEmail.value) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        shape = MaterialTheme.shapes.small
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = eMail.value.ifEmpty { "Внесите адрес" },
                    style = if (eMail.value.isNotEmpty()) MaterialTheme.typography.bodyLarge
                    else
                        MaterialTheme.typography.bodyLarge
                            .copy(color = MaterialTheme.colorScheme.outline),
                    modifier = Modifier.padding(start = 15.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.icon_edit_pen),
                    contentDescription = "edit email",
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .size(20.dp)
                        .clickable { viewModel?.openMailSheet() }
                )
            }

            TextInputFieldOutline(
                spacerDown = 0.dp,
                width = COMMON_WIDTH.dp,
                placeholder = "Оставьте комментарий",
                isError = false,
                callback = { viewModel?.setComment(text = it) }
            )

            LoadIndicator(isLoading = isLoading.value)

            Spacer(modifier = Modifier.weight(1f))

            Text_Error_Block(showTextError = showTextError.value)

            BlueButton(
                textButton = "СФОРМИРОВАТЬ QR",
                widthButton = COMMON_WIDTH,
                enabled = !isLoading.value,
                onClick = { viewModel?.buttonHandler() }
            )

            Spacer(modifier = Modifier.height(60.dp))
        }

        // Показать шторку для ввода E-mail, если он отсутствует
        InputEmailBottomSheet(
            openSheet = showInputEmailBottomSheetState.value,
            isValidEmail = showErrorValidEmail.value,
            closeSheet = { viewModel?.closeMailSheet() },
            callback = { viewModel?.saveEmail(eMail = it) }
        )
        // Показать QR код оплаты
        QrCodeBottomSheet(
            openSheet = showQrCodeSheet.value,
            title = titleSheet.value,
            closeSheet = { viewModel?.closeQrSheet() },
            image = image.value,
            paymentAmount = paymentAmount.value,
            correctAmount = correctAmount.value,
            timerCount = timerCounter.value,
            showTimer = showTimer.value,
            checkPayment = { viewModel?.checkPayment() }
        )
        // шторка для отображения ошибок
        ErrorBottomSheet(
            openSheet = showErrorSheet.value,
            closeSheet = { viewModel?.closeErrorSheet() },
            title = titleError.value,
            textError = textError.value
        )
        // шторка для коррекции стоимости заказ
        InputNewAmountSheet(
            openSheet = showInputAmountSheet.value,
            closeSheet = { showInputAmountSheet.value = false },
            callback = {
                showInputAmountSheet.value = false
                viewModel?.updatePaymentAmount(text = it)
            }
        )
        // Диалог, после запроса статуса оплаты
        ResponseDialog(
            showDialog = showResponseDialog.value,
            isLoading = isLoadingResponseDialog.value,
            payStatus = payStatusResponseDialog.value,
            title = titleResponseDialog.value,
            dismiss = { viewModel?.closeResponseDialog() },
            onClickButton = { viewModel?.onClickDelivery() }
        )
    }
}

@Preview
@Composable
private fun PreviewOrderPay() {
    KalinkaDeliveryTheme {
        OrderForPaymentScreen(
            viewModel = null,
            orderId = "000"
        )
    }
}

@Composable
private fun Text_Error_Block(
    showTextError: Boolean
) {
    if (showTextError) {
        Text(
            text = "Заполните все обязательные поля",
            style = MaterialTheme.typography.labelLarge
                .copy(color = MaterialTheme.colorScheme.error),
            modifier = Modifier.padding(bottom = 5.dp)
        )
    }

}
