package com.onecab.domain.entity

/**
 * Сущность для регистрации и отправки результатов запросов QR кодов на НАШ сервер
 */

data class AddOrderPayRequestEntity(
    val orderId         : String = "",
    val operationDate   : String = "",
    val operationAmount : Double = 0.0,
    val qrdIdSbp        : String = "", // это тот идентификатор ссылки из 32-х символов
    val sbpRequest      : String = "", // это Json от ответом QR кода
    val sbpResult       : String = "", // что если будет ошибка при оплате,
                                       // то передавать уже повторно с указанием order_id,
                                       // qrd_id_sbp и текстом ошибки/результата в поле sbp_result
)
