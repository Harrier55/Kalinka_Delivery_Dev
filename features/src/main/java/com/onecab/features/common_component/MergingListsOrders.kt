package com.onecab.features.common_component

import com.onecab.domain.entity.OrderEntity

/**
 *      Специальная функция для объединения двух списков
 *   Обобщим два списка ( с сервера и с модифицированными товарами)
 *   Если есть ли вхождение модифицированного заказа в список заказов с сервера
 *   то добавляем в список модифицированные заказы
 */

fun mergingListOrders(
    serverOrders: List<OrderEntity>,
    modifyOrders: List<OrderEntity>
): List<OrderEntity> {
    val completeOrders = serverOrders
        .map { serverOrderEntity ->
            val orderModified = modifyOrders
                .find { it.orderId == serverOrderEntity.orderId }

            orderModified?.let { modifyOrderEntity ->
                serverOrderEntity.copy(
                    goodHasBeenModified = true,
                    modifyDate = modifyOrderEntity.modifyDate,
                    goodsModified = modifyOrderEntity.goodsModified
                )
            } ?: serverOrderEntity
        }
    return completeOrders
}