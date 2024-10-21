package com.onecab.domain.entity

/**
 * Сущность для одноименного метода для регистрации Доставленных заказов
 * и отправки на НАШ сервер
 */

data class AddOrderStatusEntity(
    val order_id:        String = "",
    val delivery_date:   String = "",   // дата доставки д/б в таком формате 20240912070000
    val delivery_note:   String = "",   // комментарий
    val status:          String = "",   // статус доставки - delivered/nodelivered
)
