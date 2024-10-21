package com.onecab.features.common_component

import com.onecab.domain.entity.GoodsEntity

/**
 *      Специальная функция для объединения двух списков товаров
 *   Обобщим два списка (оригинальный  с сервера и с модифицированными товарами)
 *   Если есть ли вхождение модифицированного товара в список товаров с сервера
 *   то добавляем в список модифицированные товары
 */

fun mergingListGoods(
    originalGoodsList: List<GoodsEntity>,
    modifyGoodsList: List<GoodsEntity>,
): List<GoodsEntity>{

    val completeOrders = originalGoodsList
        .map { serverGoodsEntity ->

            val goodModified = modifyGoodsList
                .find { it.commodityId == serverGoodsEntity.commodityId }

            goodModified?.let { entity ->
                serverGoodsEntity.copy(
                    goodHasBeenModified = true,
                    modifyDate = entity.modifyDate,
                    modifyQuantity = entity.modifyQuantity
                )
            } ?: serverGoodsEntity
        }
    return completeOrders
}