package com.onecab.domain.entity

/**
 * Сущность для отправки информации об оплаченных заказаз на НАШ сервер
 * */

data class AddOrderPayEntity(
    val order_id:            String = "",
    val pay_date:            String = "",        // дата д/б в таком формате 20240912070000
    val pay_amount:          Double = 0.0,       // сумма оплаты
    val check_destination:   String = "",        // телефон или email для отправки чека
    val check_way:           String = "",        //  способ оплаты чека (email, sms)
    val pay_api_info:        String = "",        //  информация об оплате, полученная от api банка, т.е. это результат об успешности оплаты
    val pay_sr_info:         String = "",        //  комментарий, который введен торговым представителем при оплате
    val adjustments_data:    String = "",        // даные по корректировке накладной. Просто json  с данными что скорректировали (до/после)
)
