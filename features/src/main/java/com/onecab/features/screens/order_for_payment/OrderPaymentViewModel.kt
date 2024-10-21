package com.onecab.features.screens.order_for_payment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Patterns
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.onecab.core.utils.DateFormatterMyApplication
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_1
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_6
import com.onecab.domain.bank_entity.NewQrResponse
import com.onecab.domain.bank_entity.PayResultResponse
import com.onecab.domain.entity.AddOrderPayEntity
import com.onecab.domain.entity.AddOrderPayRequestEntity
import com.onecab.domain.entity.AddOrderStatusEntity
import com.onecab.domain.entity.AdjustmentsDataEntity
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.entity.PaymentParamsEntity
import com.onecab.domain.entity.QrCodeCheckerEntity
import com.onecab.domain.entity.UnshippedOrdersEntity
import com.onecab.domain.enums.Destination
import com.onecab.domain.enums.QrCodeStatus
import com.onecab.domain.repository.CheckQrCodeRepository
import com.onecab.domain.repository.OrderRepository
import com.onecab.domain.repository.ParamsRepository
import com.onecab.domain.repository.PaymentRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.domain.response_entity.KalinkaResultResponse
import com.onecab.features.common_component.mergingListGoods
import com.onecab.features.di.IoDispatcher
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

data class OrderForPaymentScreenUiState(
    val paymentAmount: String = "0",
    val orderId: String = "",
    val orderNum: String = "",
    val purchaserName: String = "",
    val image: ImageBitmap? = null,
    val comment: String = "",
    val eMail: String = "",
    val companyId: String = "",
    val isLoading: Boolean = false,
    val showQrCodeSheet: Boolean = false,
    val showTextError: Boolean = false,
    val showMailSheet: Boolean = false,
    val showErrorSheet: Boolean = false,
    val showErrorBorderPaymentField: Boolean = false,
    val showErrorBorderEmail: Boolean = false,
    val titleError: String = "",
    val textError: String = "",
    val showCorrectionField: Boolean = false,
    val selectedItemCorrection: String = "",
    val showErrorBorderCorrectionField: Boolean = false,

    val payload: String? = null,
    val qrdIdSbp: String? = null,
    val titleForSheet: String = "",
    val timerCounter: String = "",
    val showTimer: Boolean = false,
    val correctAmount: String = "",

    val showErrorValidEmail: Boolean = false,

    // для работы ResponseDialog
    val showResponseDialog: Boolean = false,
    val payStatusResponseDialog: Boolean = false,
    val titleResponseDialog: String = "",
    val isLoadingResponseDialog: Boolean = false,
)

private const val TAG = "OrderPaymentViewModel"

@HiltViewModel
class OrderPaymentViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val navigationService: NavigationService,
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val registerRepository: RegisterRepository,
    private val paramsRepository: ParamsRepository,
    private val checkQrCodeRepository: CheckQrCodeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(OrderForPaymentScreenUiState())
    val state = _state.asStateFlow()

    /**
    В экране есть логика: если пользователь будет изменять исходную сумму оплаты
    то должен появиться выпадающий список с причинами коррекции
    переменная var paymentAmount будет хранить оригинальное значение и текущее значение
     */
    private var originalValuePaymentAmount: Double = 0.0
    private var currentValuePaymentAmount: Double = 0.0

    fun fetchScreen(orderId: String) {

        val orderEntity = orderRepository.getOrderByIdFromCash(orderId = orderId)
        val goodsList = orderEntity.goodsFromServer
        val calculateAmount = goodsList.sumOf { it.quantity * it.price }

        // закэшируем первоначальные значения стоимости заказа
        originalValuePaymentAmount = calculateAmount
        currentValuePaymentAmount = calculateAmount

        _state.value = state.value.copy(
            orderId = orderId,
            orderNum = orderEntity.orderNumber,
            paymentAmount = calculateAmount.toMyFormat(),
            eMail = orderEntity.purchaserEmail,
            purchaserName = orderEntity.purchaserName,
            companyId = orderEntity.companyId,
        )
    }

    // Обработка нажатия кнопки Сформировать QR
    /**
    Логика работы - вначале проверяем, есть ли еще актыальные по времени коды,
    если есть - показываем код, который еще актуален,
    если нет - генерируем новый код
    Также показываем счетчик обратного отсчета
     */
    fun buttonHandler() {
        viewModelScope.launch(ioDispatcher) {
            // получить текущую дату
            val currentDate = DateFormatterMyApplication.getCurrentLocalDateTime()

            // список кодов из базы  на текущую дату
            val listQr = checkQrCodeRepository
                .getCode(currentDate = currentDate.date_formatter_1())
                .filter { it.qrdIdSbp != QrCodeStatus.ACWP.name } // убрать оплаченные коды

            // последнее вхождение кода в списке
            val qrCode = listQr.findLast { it.orderId == state.value.orderId }

            // дата/время сохранения кода в БД
            val inputDateTimeString = qrCode?.dateTime ?: "" // test  "2024-10-08T06:50:00"
            // Получить данные для оплаты
            val paymentParams = paramsRepository
                .getPaymentParamsByCompanyId(
                    companyId = state.value.companyId
                )
            // время жизни QR кода (мин)
            val myIncrement = (paymentParams?.qrTtl ?: 120).toLong() / 60

            val difference = qrCode?.let {
                DateFormatterMyApplication.countdownTimer(
                    dateFromDb = it.dateTime,
                    increaseMin = myIncrement
                )
            } ?: ""

            if (qrCode != null && difference.isNotEmpty()) {
                /*-----  если код еще актуален по времени - покажем его ----*/

                _state.value = state.value.copy(
                    isLoading = false,
                    showQrCodeSheet = true,
                    image = qrCode.image.fromBase64ToBitMap()!!.asImageBitmap(),
                    correctAmount = qrCode.paymentAmount.toMyFormat(),
                    titleForSheet = "Для этого заказа уже был сформирован QR-код",
                )

                showCountdownTimer(
                    dateFromDb = inputDateTimeString,
                    increaseMin = myIncrement
                )

            } else {
                /** ----  если уже не актуален - генерируем новый код ---*/

                // Проверка валидности полей
                val checkValidate = checkValidateAllField()

                // если введенные данные норм и данные для оплаты тоже - генерируем код
                if (checkValidate && paymentParams != null) {
                    try {
                        // преобразовать payment amount из String в Double и загрузить QR код
                        val amount: Double = state.value.paymentAmount.toDouble()
                        if (amount > 0.0) {
                            viewModelScope.launch(ioDispatcher) {
                                // загружаем QR код и записываем его в базу данных
                                loadQrCode(paymentAmount = amount, paymentParams = paymentParams)
                                // запустим обратный счетчик
                                launch {
                                    showCountdownTimer(
                                        dateFromDb = DateFormatterMyApplication.getCurrentLocalDateTime(),
                                        increaseMin = myIncrement
                                    )
                                }
                                // ждем немного и выполняем проверку оплаты
//                                delay(5000)
//                                processCheckPay()  // todo ОТКЛЮЧЕНО
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    // Для отображения обратного счетчика на шторке
    private suspend fun showCountdownTimer(dateFromDb: String, increaseMin: Long) {
        var diff = DateFormatterMyApplication.countdownTimer(
            dateFromDb = dateFromDb,
            increaseMin = increaseMin
        )
        while (diff.isNotEmpty()) {
            val diff1 = DateFormatterMyApplication.countdownTimer(
                dateFromDb = dateFromDb,
                increaseMin = increaseMin
            )
            delay(1000)

            _state.value = state.value.copy(
                showTimer = true,
                timerCounter = diff1
            )

            diff = diff1
        }
        _state.value = state.value.copy(
            showTimer = false,
            titleForSheet = "Время действия кода истекло"
        )
    }

    // проверка правильности заполнения полей , соответственно-отображение ошибок на экране
    private fun checkValidateAllField(): Boolean {

        val checkPayment = state.value.paymentAmount.toDouble() > 0.0
        val checkEmail = state.value.eMail.isNotEmpty()
        val checkReasonForCorrection =
            if (originalValuePaymentAmount != currentValuePaymentAmount) {
                state.value.selectedItemCorrection.isNotEmpty()
            } else {
                true
            }

        // Отображение необходимых ошибок на экране
        _state.value = state.value.copy(
            showErrorBorderPaymentField = !checkPayment,
            showErrorBorderEmail = !checkEmail,
            showErrorBorderCorrectionField = !checkReasonForCorrection,
            showTextError = !(checkEmail && checkPayment && checkReasonForCorrection)
        )
        return checkEmail && checkPayment && checkReasonForCorrection
    }


    fun closeQrSheet() {
        _state.value = state.value.copy(showQrCodeSheet = false)
    }

    fun openMailSheet() {
        _state.value = state.value.copy(showMailSheet = true)
    }

    fun closeMailSheet() {
        _state.value = state.value.copy(showMailSheet = false)
    }

    fun closeErrorSheet() {
        _state.value = state.value.copy(showErrorSheet = false)
    }

    // установить значение введенного комментария в State
    fun setComment(text: String) {
        _state.value = state.value.copy(comment = text)
    }

    // Сохранить E-mail в State
    fun saveEmail(eMail: String) {
        val valid = isValidEmail(eMail)

        if (valid) {
            _state.value = state.value.copy(
                showMailSheet = false,
                eMail = eMail,
                showErrorValidEmail = false
            )
        } else {
            _state.value = state.value.copy(
                showErrorValidEmail = true
            )
        }

        checkValidateAllField()
    }

    // функция проверки валидности ввода эл.адреса
    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    // установить значение выбранной причины коррекции в State
    fun setSelectedItem(selectedItem: String) {
        _state.value = state.value.copy(selectedItemCorrection = selectedItem)
        checkValidateAllField()
    }

    // Обновить значение введенной суммы платежа в State и в кэше
    fun updatePaymentAmount(text: String) {
        try {
            currentValuePaymentAmount = text.toDouble()

            _state.value = state.value.copy(
                paymentAmount = text,
                showCorrectionField = originalValuePaymentAmount != currentValuePaymentAmount
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

        checkValidateAllField()
    }

    fun clickArrowBack() {
        navigationService.onClickArrowBack()
    }

    fun closeResponseDialog() {
        _state.value = state.value.copy(showResponseDialog = false)
    }

    /*-----------  Private ----------------------*/

    // Получение QR кода
    private suspend fun loadQrCode(
        paymentAmount: Double,
        paymentParams: PaymentParamsEntity
    ) {

        _state.value = state.value.copy(isLoading = true)

        val result = paymentRepository
            .getNewQr(
                paymentAmount = paymentAmount,
                paymentParams = paymentParams
            )

        when (result) {

            is PayResultResponse.Success -> {
                val image = result.data.imageContent
                val payload = result.data.payload
                val qrdIdSbp = result.data.QRDIDSBP

                image?.let {
                    _state.value = state.value.copy(
                        isLoading = false,
                        showQrCodeSheet = true,
                        image = it.fromBase64ToBitMap()!!.asImageBitmap(),
                        payload = payload,
                        qrdIdSbp = qrdIdSbp,
                        titleForSheet = "Покажите QR-код клиенту"
                    )
                }

                _state.value = state.value.copy(
                    isLoading = false
                )

                // отправить информацию о полученном QR на наш сервер
                addOrderPayRequestToServer(
                    paymentAmount = paymentAmount,
                    newQrResponse = result.data
                )

                // сохранить данные об этом коде в БД кодов
                checkQrCodeRepository.saveCode(
                    QrCodeCheckerEntity(
                        qrdIdSbp = qrdIdSbp ?: "null",
                        orderId = state.value.orderId,
                        paymentAmount = paymentAmount,
                        date = DateFormatterMyApplication.getCurrentLocalDateTime()
                            .date_formatter_1(),
                        dateTime = DateFormatterMyApplication.getCurrentLocalDateTime(),
                        image = image ?: "",
                        status = QrCodeStatus.NTST.name
                    )
                )
            }

            is PayResultResponse.Error -> {
                _state.value = state.value.copy(
                    isLoading = false,
                    titleError = "Ошибка при обращении к серверу",
                    textError = "Ошибка ${result.httpCode} \n ${result.message}",
                    showErrorSheet = true
                )
            }
        }
    }

    // Кнопка "Проверить оплату" - вручную
    fun checkPayment() {
        // переменная для кэширования результатов запроса оплаты
        var sbpStatus = ""
        // Максимальное время работы запроса
        val maxWaitTime = 30000L // 30 секунд
        val startTime = System.currentTimeMillis()
        // Время задержки между запросами
        val maxDelay = 5000L  // 5 сек

        viewModelScope.launch(ioDispatcher) {
            _state.value = state.value.copy(
                showQrCodeSheet = false,
                showResponseDialog = true,
                isLoadingResponseDialog = true,
                titleResponseDialog = "Запрос в банк ..."
            )

            delay(500)

            while (isActive && System.currentTimeMillis() - startTime < maxWaitTime) {
                // Делаем запрос к платежному сервису, чтобы проверить статус
                val paymentStatus = checkPaymentStatus(qrdIdSbp = state.value.qrdIdSbp ?: "")
                Log.d(TAG, "-- Проверка статуса оплаты $paymentStatus")

                if (paymentStatus == QrCodeStatus.ACWP.name) {
                    // сервер ответил, что оплата успешна

                    sbpStatus = paymentStatus

                    _state.value = state.value.copy(
                        showResponseDialog = true,
                        isLoadingResponseDialog = false,
                        payStatusResponseDialog = true,
                        titleResponseDialog = "Оплата успешна"
                    )

                    // отправим обновления на наш сервер - отметить, как оплаченный
                    val addPay = loadOrderPayToServer(payApiInfo = paymentStatus)

                    // обновим информацию о событиях в БД ожидания
                    val newItem = UnshippedOrdersEntity(
                        orderId = state.value.orderId,
                        purchaserName = state.value.purchaserName,
                        qrd_id_sbp = state.value.qrdIdSbp ?: "",
                        orderNumber = state.value.orderNum,
                        paymentAmount = state.value.paymentAmount.toDouble(),
                        paymentDate = DateFormatterMyApplication.getCurrentLocalDateTime()
                            .date_formatter_6(),
                        pay_api_info = paymentStatus,
                        doneAddDelivery = false,
                        doneSbp = true,
                        doneAddPay = addPay,
                        pay_sr_info = state.value.comment,
                        check_destination = state.value.eMail,
                        delivery_note = state.value.comment,
                        check_way = Destination.email.name,
                        adjustments_data = createAdjustmentsData(orderId = state.value.orderId),
                        status = Destination.delivered.name
                    )
                    orderRepository.updateUnshippedOrderFromDb(unshippedOrdersEntity = newItem)

                    // обновить информацию в БД для QR кодов
                    state.value.qrdIdSbp?.let {
                        checkQrCodeRepository.updateStatus(
                            qrdIdSbp = it,
                            newStatus = QrCodeStatus.ACWP.name
                        )
                    }

                    break
                } else {
                    sbpStatus = paymentStatus
                }
                delay(maxDelay)  // задержка между запросами
            }

            if (isActive && sbpStatus != QrCodeStatus.ACWP.name) {
                Log.d(TAG, ">> время ожидания истекло или запрос завершился ошибкой")
                _state.value = state.value.copy(
                    showResponseDialog = true,
                    isLoadingResponseDialog = false,
                    payStatusResponseDialog = false,
                    titleResponseDialog = "Оплата не выполнена"
                )

                // обновим эту запись в базе ожидания, хотя ответ от банка отрицательный
                val newItem = UnshippedOrdersEntity(
                    orderId = state.value.orderId,
                    purchaserName = state.value.purchaserName,
                    qrd_id_sbp = state.value.qrdIdSbp ?: "",
                    orderNumber = state.value.orderNum,
                    paymentAmount = state.value.paymentAmount.toDouble(),
                    paymentDate = DateFormatterMyApplication.getCurrentLocalDateTime()
                        .date_formatter_6(),
                    pay_api_info = sbpStatus,
                    doneAddDelivery = false,
                    doneSbp = false,
                    doneAddPay = false,
                    pay_sr_info = state.value.comment,
                    check_destination = state.value.eMail,
                    delivery_note = state.value.comment,
                    check_way = Destination.email.name,
                    adjustments_data = createAdjustmentsData(orderId = state.value.orderId),
                    status = Destination.delivered.name
                )
                val resultSave =
                    orderRepository.updateUnshippedOrderFromDb(unshippedOrdersEntity = newItem)
                Log.d(TAG, "-- checkPayment resultSave = $resultSave ")

                // обновить информацию в БД для QR кодов
                state.value.qrdIdSbp?.let {
                    checkQrCodeRepository.updateStatus(
                        qrdIdSbp = it,
                        newStatus = sbpStatus
                    )
                }
            }
        }
    }

    // отметить заказ, как доставленный
    fun onClickDelivery() {
        viewModelScope.launch(ioDispatcher) {

            // отправить заказ на сервер
            val addDeliv = loadDeliveryOrderToSever()

            // Отметить этот заказ в локальной базе данных
            if (addDeliv) {
                val newItem = UnshippedOrdersEntity(
                    orderId = state.value.orderId,
                    purchaserName = state.value.purchaserName,
                    qrd_id_sbp = state.value.qrdIdSbp ?: "",
                    orderNumber = state.value.orderNum,
                    paymentAmount = state.value.paymentAmount.toDouble(),
                    paymentDate = DateFormatterMyApplication.getCurrentLocalDateTime()
                        .date_formatter_6(),
                    pay_api_info = QrCodeStatus.ACWP.name,
                    doneAddDelivery = true,
                    doneSbp = true,
                    doneAddPay = true,
                    pay_sr_info = state.value.comment,
                    check_destination = state.value.eMail,
                    delivery_note = state.value.comment,
                    check_way = Destination.email.name,
                    adjustments_data = createAdjustmentsData(orderId = state.value.orderId),
                    status = Destination.delivered.name
                )
                val resultSave =
                    orderRepository.updateUnshippedOrderFromDb(unshippedOrdersEntity = newItem)

                Log.d(TAG, "-- onClickDelivery resultSave = $resultSave")

                // закрыть экран и перейти на главный
                withContext(Dispatchers.Main) {
                    // возврат на главную
                    // чтобы автоматически обновить данные с сервера - сбросим счетчик загрузок
                    orderRepository.resetCountInstance()
                    // и вернемся на главную страницу
                    navigationService.navigateWithoutArgument(
                        route = Screens.OrdersListScreen.route
                    )
                }
            }
        }
    }

    // Проверить статус платежа
    private suspend fun checkPaymentStatus(qrdIdSbp: String): String {

        val response = paymentRepository.getStatusQrd(
            qrdIdSbp = qrdIdSbp,
            paymentParams = paramsRepository
                .getPaymentParamsByCompanyId(
                    companyId = state.value.companyId
                )?: PaymentParamsEntity()
        )
        Log.d(TAG, "-- Ответ от сервера по статусу кода $response для qrdIdSbp = $qrdIdSbp")

        return when (response) {
            is PayResultResponse.Success -> {
                response.data.paymentStatus
            }

            is PayResultResponse.Error -> {
                "${response.httpCode} ${response.message}"
            }
        }
    }

    // отправить информацию о полученном QR на наш сервер
    private suspend fun addOrderPayRequestToServer(
        paymentAmount: Double,
        newQrResponse: NewQrResponse
    ) {
        val addOrderPayRequestEntity = AddOrderPayRequestEntity(
            orderId = state.value.orderId,
            operationDate = DateFormatterMyApplication.getCurrentLocalDateTime().date_formatter_6(),
            operationAmount = paymentAmount,
            qrdIdSbp = newQrResponse.QRDIDSBP ?: "no_data",
            sbpRequest = Gson().toJson(newQrResponse),
            sbpResult = "временно не используется",
        )

        orderRepository.addOrderPayRequest(
            token = registerRepository.getAuthToken().token ?: "",
            addOrderPayRequestEntity = addOrderPayRequestEntity
        )
    }

    // загрузка оплаченных заказов на сервер
    private suspend fun loadOrderPayToServer(payApiInfo: String): Boolean {

        val token = registerRepository.getAuthToken().token ?: ""
        val newPay = AddOrderPayEntity(
            order_id = state.value.orderId,
            pay_date = DateFormatterMyApplication.getCurrentLocalDateTime().date_formatter_6(),
            pay_amount = state.value.paymentAmount.toDouble(),
            check_destination = state.value.eMail,
            check_way = Destination.email.name,
            pay_api_info = payApiInfo,
            pay_sr_info = state.value.comment,
            adjustments_data = createAdjustmentsData(orderId = state.value.orderId)
        )

        val response = orderRepository.addOrderPay(
            token = token,
            addOrderPayEntity = newPay
        )
        Log.d(TAG, "-- loadOrderPayToServer ответ сервера === $response")

        return when (response) {
            is KalinkaResultResponse.Success -> {
                true
            }

            is KalinkaResultResponse.Error -> {
                false
            }
        }
    }

    // получить список измененных при модификации товаров для отправки на наш сервер
    // и преобразовать это все в строку
    private fun createAdjustmentsData(orderId: String): String {
        try {
            // получить список измененных товаров
            val list = orderRepository.getOrdersFromCash()
            val order = list.find { it.orderId == orderId } ?: OrderEntity()
            val resultMergeGoodsList = mergingListGoods(
                originalGoodsList = order.goodsFromServer,
                modifyGoodsList = order.goodsModified
            )
            val adjustmentsDataList = resultMergeGoodsList
                .map { goodsEntity ->
                    AdjustmentsDataEntity(
                        orderId = orderId,
                        commodityId = goodsEntity.commodityId,
                        beforeQuantity = goodsEntity.quantity,
                        afterQuantity = goodsEntity.modifyQuantity,
                        modifyDate = goodsEntity.modifyDate,
                    )
                }

            return Gson().toJson(adjustmentsDataList)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    // загрузить доставленные заказы на сервер
    private suspend fun loadDeliveryOrderToSever(): Boolean {

        val token = registerRepository.getAuthToken().token ?: ""
        val newPay = AddOrderStatusEntity(
            order_id = state.value.orderId,
            delivery_date = DateFormatterMyApplication.getCurrentLocalDateTime().date_formatter_6(),
            delivery_note = state.value.comment,
            status = Destination.delivered.name
        )
        val response = orderRepository.addOrderStatus(
            token = token,
            addOrderStatusEntity = newPay
        )
        Log.d(TAG, "-- loadDeliveryOrderToSever ответ сервера === $response")

        return when (response) {
            is KalinkaResultResponse.Success -> {
                true
            }

            is KalinkaResultResponse.Error -> {
                false
            }
        }
    }

    /*------------  Extentions ---------------------*/

    private fun String.fromBase64ToBitMap(): Bitmap? {
        try {
            val decodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun Double.toMyFormat(): String {
        return String.format(Locale.UK, "%.2f", this)
    }
}